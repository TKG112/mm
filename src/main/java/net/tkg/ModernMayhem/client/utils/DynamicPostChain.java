package net.tkg.ModernMayhem.client.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A modified version of {@link net.minecraft.client.renderer.PostChain} that allows dynamic management of post-processing passes and render targets.
 */
public final class DynamicPostChain implements AutoCloseable {
    private static final String MAIN_RENDER_TARGET = "minecraft:main";
    private final RenderTarget screenTarget;
    private final ResourceManager resourceManager;
    private final String name;
    private final List<PostPass> passes = Lists.newArrayList();
    private final Map<String, RenderTarget> customRenderTargets = Maps.newHashMap();
    private final List<RenderTarget> fullSizedTargets = Lists.newArrayList();

    private final Set<String> externalTargets = new HashSet<>();

    private Matrix4f shaderOrthoMatrix;
    private int screenWidth;
    private int screenHeight;
    private float time;
    private float lastStamp;

    public DynamicPostChain(TextureManager textureManager, ResourceManager resourceManager, RenderTarget screenTarget, ResourceLocation name) throws IOException {
        this.resourceManager = resourceManager;
        this.screenTarget = screenTarget;
        this.time = 0.0F;
        this.lastStamp = 0.0F;
        this.screenWidth = screenTarget.viewWidth;
        this.screenHeight = screenTarget.viewHeight;
        this.name = name.toString();
        this.updateOrthoMatrix();
        this.load(textureManager, name);
    }

    public DynamicPostChain(TextureManager textureManager, ResourceManager resourceManager, RenderTarget screenTarget, ResourceLocation name, Map<String, RenderTarget> preRegisteredExternalTargets) throws IOException {
        this.resourceManager = resourceManager;
        this.screenTarget = screenTarget;
        this.time = 0.0F;
        this.lastStamp = 0.0F;
        this.screenWidth = screenTarget.viewWidth;
        this.screenHeight = screenTarget.viewHeight;
        this.name = name.toString();
        this.updateOrthoMatrix();
        this.externalTargets.addAll(preRegisteredExternalTargets.keySet());
        this.customRenderTargets.putAll(preRegisteredExternalTargets);
        this.load(textureManager, name);
    }

    public List<PostPass> getPasses() { return this.passes; }
    public String getName() { return this.name; }

    @Nullable
    public RenderTarget getTempTarget(String name) { return this.customRenderTargets.get(name); }

    public void setExternalTarget(String name, RenderTarget target) {
        // If we previously owned a target under this name, clean it up first
        if (customRenderTargets.containsKey(name) && !externalTargets.contains(name)) {
            customRenderTargets.get(name).destroyBuffers();
            fullSizedTargets.remove(customRenderTargets.get(name));
        }
        customRenderTargets.put(name, target);
        externalTargets.add(name);
        // Intentionally not added to fullSizedTargets — the owner controls its lifecycle
    }

    public boolean hasUniform(String name) { return !this.findUniforms(name).isEmpty(); }

    public void setUniform1f(String name, float a) {
        for (Uniform uniform : this.findUniforms(name)) uniform.set(a);
    }

    public void setUniform2f(String name, float a, float b) {
        for (Uniform uniform : this.findUniforms(name)) uniform.set(a, b);
    }

    public void setUniform3f(String name, float a, float b, float c) {
        for (Uniform uniform : this.findUniforms(name)) uniform.set(a, b, c);
    }

    public void setUniform4f(String name, float a, float b, float c, float d) {
        for (Uniform uniform : this.findUniforms(name)) uniform.set(a, b, c, d);
    }

    private List<Uniform> findUniforms(String name) {
        List<Uniform> result = new ArrayList<>();
        for (PostPass pass : this.passes) {
            Uniform uniform = pass.getEffect().getUniform(name);
            if (uniform != null) result.add(uniform);
        }
        return result;
    }

    private void load(TextureManager textureManager, ResourceLocation resourceLocation) throws IOException, JsonSyntaxException {
        Resource resource = this.resourceManager.getResourceOrThrow(resourceLocation);
        try {
            try (Reader reader = resource.openAsReader()) {
                JsonObject jsonObject = GsonHelper.parse(reader);
                if (GsonHelper.isArrayNode(jsonObject, "targets")) {
                    JsonArray targets = jsonObject.getAsJsonArray("targets");
                    int i = 0;
                    for (JsonElement element : targets) {
                        try {
                            this.parseTargetNode(element);
                        } catch (Exception exception) {
                            ChainedJsonException chained = ChainedJsonException.forException(exception);
                            chained.prependJsonKey("targets[" + i + "]");
                            throw chained;
                        }
                        i++;
                    }
                }
                if (GsonHelper.isArrayNode(jsonObject, "passes")) {
                    JsonArray passes = jsonObject.getAsJsonArray("passes");
                    int i = 0;
                    for (JsonElement element : passes) {
                        try {
                            this.parsePassNode(textureManager, element);
                        } catch (Exception exception) {
                            ChainedJsonException chained = ChainedJsonException.forException(exception);
                            chained.prependJsonKey("passes[" + i + "]");
                            throw chained;
                        }
                        i++;
                    }
                }
            }
        } catch (Exception exception) {
            ChainedJsonException chained = ChainedJsonException.forException(exception);
            chained.setFilenameAndFlush(resourceLocation.getPath() + " (" + resource.sourcePackId() + ")");
            throw chained;
        }
    }

    private void parseTargetNode(JsonElement json) throws ChainedJsonException {
        if (GsonHelper.isStringValue(json)) {
            this.addTempTarget(json.getAsString(), this.screenWidth, this.screenHeight);
            return;
        }
        JsonObject object = GsonHelper.convertToJsonObject(json, "target");
        String name = GsonHelper.getAsString(object, "name");
        int width = GsonHelper.getAsInt(object, "width", this.screenWidth);
        int height = GsonHelper.getAsInt(object, "height", this.screenHeight);
        if (this.customRenderTargets.containsKey(name)) {
            throw new ChainedJsonException(name + " is already defined");
        }
        this.addTempTarget(name, width, height);
    }

    private void parsePassNode(TextureManager textureManager, JsonElement json) throws IOException {
        JsonObject object = GsonHelper.convertToJsonObject(json, "pass");
        String programName = GsonHelper.getAsString(object, "name");
        String inTargetName = GsonHelper.getAsString(object, "intarget");
        String outTargetName = GsonHelper.getAsString(object, "outtarget");
        RenderTarget inTarget = this.getRenderTarget(inTargetName);
        RenderTarget outTarget = this.getRenderTarget(outTargetName);
        if (inTarget == null) throw new ChainedJsonException("Input target '" + inTargetName + "' does not exist");
        if (outTarget == null) throw new ChainedJsonException("Output target '" + outTargetName + "' does not exist");

        PostPass postPass = this.addPass(programName, inTarget, outTarget);
        JsonArray auxTargets = GsonHelper.getAsJsonArray(object, "auxtargets", null);
        if (auxTargets != null) {
            int i = 0;
            for (JsonElement element : auxTargets) {
                try {
                    JsonObject auxObject = GsonHelper.convertToJsonObject(element, "auxtarget");
                    String uniformName = GsonHelper.getAsString(auxObject, "name");
                    String id = GsonHelper.getAsString(auxObject, "id");
                    boolean depth;
                    String targetId;
                    if (id.endsWith(":depth")) {
                        depth = true;
                        targetId = id.substring(0, id.lastIndexOf(':'));
                    } else {
                        depth = false;
                        targetId = id;
                    }
                    RenderTarget auxTarget = this.getRenderTarget(targetId);
                    if (auxTarget == null) {
                        if (depth) throw new ChainedJsonException("Render target '" + targetId + "' can't be used as depth buffer");
                        ResourceLocation rl = ResourceLocation.tryParse(targetId);
                        ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), "textures/effect/" + rl.getPath() + ".png");
                        this.resourceManager.getResource(textureLocation).orElseThrow(() ->
                                new ChainedJsonException("Render target or texture '" + targetId + "' does not exist")
                        );
                        RenderSystem.setShaderTexture(0, textureLocation);
                        textureManager.bindForSetup(textureLocation);
                        AbstractTexture texture = textureManager.getTexture(textureLocation);
                        int width = GsonHelper.getAsInt(auxObject, "width");
                        int height = GsonHelper.getAsInt(auxObject, "height");
                        boolean bilinear = GsonHelper.getAsBoolean(auxObject, "bilinear");
                        if (bilinear) {
                            RenderSystem.texParameter(3553, 10241, 9729);
                            RenderSystem.texParameter(3553, 10240, 9729);
                        } else {
                            RenderSystem.texParameter(3553, 10241, 9728);
                            RenderSystem.texParameter(3553, 10240, 9728);
                        }
                        postPass.addAuxAsset(uniformName, texture::getId, width, height);
                    } else if (depth) {
                        postPass.addAuxAsset(uniformName, auxTarget::getDepthTextureId, auxTarget.width, auxTarget.height);
                    } else {
                        postPass.addAuxAsset(uniformName, auxTarget::getColorTextureId, auxTarget.width, auxTarget.height);
                    }
                } catch (Exception exception) {
                    ChainedJsonException chained = ChainedJsonException.forException(exception);
                    chained.prependJsonKey("auxtargets[" + i + "]");
                    throw chained;
                }
                i++;
            }
        }

        JsonArray uniforms = GsonHelper.getAsJsonArray(object, "uniforms", null);
        if (uniforms != null) {
            int i = 0;
            for (JsonElement element : uniforms) {
                try {
                    this.parseUniformNode(element);
                } catch (Exception exception) {
                    ChainedJsonException chained = ChainedJsonException.forException(exception);
                    chained.prependJsonKey("uniforms[" + i + "]");
                    throw chained;
                }
                i++;
            }
        }
    }

    private void parseUniformNode(JsonElement json) throws ChainedJsonException {
        JsonObject object = GsonHelper.convertToJsonObject(json, "uniform");
        String name = GsonHelper.getAsString(object, "name");
        Uniform uniform = this.passes.get(this.passes.size() - 1).getEffect().getUniform(name);
        if (uniform == null) throw new ChainedJsonException("Uniform '" + name + "' does not exist");
        float[] values = new float[4];
        int i = 0;
        for (JsonElement element : GsonHelper.getAsJsonArray(object, "values")) {
            try {
                values[i] = GsonHelper.convertToFloat(element, "value");
            } catch (Exception exception) {
                ChainedJsonException chained = ChainedJsonException.forException(exception);
                chained.prependJsonKey("values[" + i + "]");
                throw chained;
            }
            i++;
        }
        switch (i) {
            case 1 -> uniform.set(values[0]);
            case 2 -> uniform.set(values[0], values[1]);
            case 3 -> uniform.set(values[0], values[1], values[2]);
            case 4 -> uniform.set(values[0], values[1], values[2], values[3]);
        }
    }

    public void addTempTarget(String name, int width, int height) {
        RenderTarget target = new TextureTarget(width, height, true, Minecraft.ON_OSX);
        target.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        if (screenTarget.isStencilEnabled()) target.enableStencil();
        this.customRenderTargets.put(name, target);
        if (width == this.screenWidth && height == this.screenHeight) {
            this.fullSizedTargets.add(target);
        }
    }

    public PostPass addPass(String programName, RenderTarget input, RenderTarget output) throws IOException {
        PostPass postPass = new PostPass(this.resourceManager, programName, input, output);
        this.passes.add(postPass);
        return postPass;
    }

    private void updateOrthoMatrix() {
        this.shaderOrthoMatrix = new Matrix4f().setOrtho(
                0.0F, (float) this.screenTarget.width,
                0.0F, (float) this.screenTarget.height,
                0.1F, 1000.0F
        );
    }

    public void resize(int width, int height) {
        this.screenWidth = this.screenTarget.width;
        this.screenHeight = this.screenTarget.height;
        this.updateOrthoMatrix();
        for (PostPass postPass : this.passes) {
            postPass.setOrthoMatrix(this.shaderOrthoMatrix);
        }
        for (RenderTarget target : this.fullSizedTargets) {
            target.resize(width, height, Minecraft.ON_OSX);
        }
    }

    public void process(float partialTicks) {
        if (partialTicks < this.lastStamp) {
            this.time += 1.0F - this.lastStamp;
            this.time += partialTicks;
        } else {
            this.time += partialTicks - this.lastStamp;
        }
        this.lastStamp = partialTicks;
        while (this.time > 20.0F) this.time -= 20.0F;
        for (PostPass postPass : this.passes) {
            postPass.process(this.time / 20.0F);
        }
    }

    @Override
    public void close() {
        for (Map.Entry<String, RenderTarget> entry : this.customRenderTargets.entrySet()) {
            if (!externalTargets.contains(entry.getKey())) {
                entry.getValue().destroyBuffers();
            }
        }
        for (PostPass postPass : this.passes) postPass.close();
        this.passes.clear();
        this.customRenderTargets.clear();
        this.fullSizedTargets.clear();
        this.externalTargets.clear();
    }

    @Nullable
    private RenderTarget getRenderTarget(@Nullable String target) {
        if (target == null) return null;
        if (MAIN_RENDER_TARGET.equals(target)) return this.screenTarget;
        return this.customRenderTargets.get(target);
    }
}