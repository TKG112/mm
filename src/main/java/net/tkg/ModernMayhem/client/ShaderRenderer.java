package net.tkg.ModernMayhem.client;


import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tkg.ModernMayhem.ModernMayhemMod;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * ShaderRenderer is responsible for managing and rendering a specific shader in Minecraft.
 * It initializes the shader using a PostChain and provides methods to activate, deactivate, and render the shader.
 * This class is designed to be used in the client-side rendering pipeline, specifically for custom shaders.
 * * Note: Ensure that the shader file exists in the specified path and is correctly formatted for Minecraft's shader system.
 * @author CookieG
 */
public class ShaderRenderer {
    private final ResourceLocation shaderLocation;
    private PostChain postChain = null;
    private boolean isActive = false;
    private int lastFrameScreenWidth = -1;
    private int lastFrameScreenHeight = -1;

    private static final Minecraft mc = Minecraft.getInstance();

    /**
     * Constructs a ShaderRenderer for a specific shader.
     * @param shaderLocation The ResourceLocation of the shader to be rendered.
     */
    public ShaderRenderer(@NotNull ResourceLocation shaderLocation) {
        this.shaderLocation = shaderLocation;
    }

    /**
     * Gets the ResourceLocation of the shader.
     * @return The ResourceLocation of the shader.
     */
    public ResourceLocation getShaderLocation() { return shaderLocation; }

    /**
     * Checks if the shader is currently active.
     * @return true if the shader is active, false otherwise.
     */
    public boolean isActive() { return isActive; }

    /**
     * Gets the name of the shader without the file extension.
     * @return The name of the shader.
     */
    public String getShaderName() {
        return shaderLocation.getNamespace() + ResourceLocation.NAMESPACE_SEPARATOR + shaderLocation.getPath().substring(shaderLocation.getPath().lastIndexOf('/') + 1, shaderLocation.getPath().lastIndexOf('.'));
    }

    /**
     * Activate the shader, allowing it to be rendered.
     * Note: You still need to call render() in your rendering loop to see the shader effect.
     */
    public void activate() {
        // We initialize the PostChain here to ensure it is ready when the shader is activated.
        try {
            this.initShader();
            lastFrameScreenWidth = mc.getWindow().getGuiScaledWidth();
            lastFrameScreenHeight = mc.getWindow().getGuiScaledHeight();
            isActive = true;
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.error("Failed to load shader: {}", shaderLocation, e);
        }
    }

    /**
     * Deactivate the shader, preventing it from being rendered.
     * This is useful for performance optimization when the shader is not needed.
     */
    public void deactivate() {
        isActive = false;
        if (postChain != null) {
            postChain.close();
            postChain = null;
        }
    }

    /**
     * Toggles the active state of the shader.
     * If the shader is currently active, it will be deactivated, and vice versa.
     * Note: You still need to call render() in your rendering loop to see the shader effect.
     */
    public void toggleActive() {
        if (isActive) {
            deactivate();
        } else {
            activate();
        }
    }

    /**
     * Renders the shader effect.
     * This method should be called in the rendering loop, typically in the render method of your mod's client-side event handler.
     * @param partialTicks - The partial ticks for smooth rendering.
     */
    public void render(float partialTicks) {
        if (!isActive) return;
        if (postChain == null) {
            ModernMayhemMod.LOGGER.warn("Cannot render shader {} because postChain is null", getShaderName());
            return;
        }
        int currentScreenWidth = mc.getWindow().getGuiScaledWidth();
        int currentScreenHeight = mc.getWindow().getGuiScaledHeight();

        if (lastFrameScreenWidth != currentScreenWidth || lastFrameScreenHeight != currentScreenHeight) {
            System.out.println("Resizing shader " + getShaderName() + " to " + currentScreenWidth + "x" + currentScreenHeight);
            try {
                this.initShader();
            } catch (IOException e) {
                ModernMayhemMod.LOGGER.error("Failed to reinitialize shader: {}", shaderLocation, e);
                return;
            }
        }
        postChain.process(partialTicks);

        RenderSystem.setShader(() -> null);
        mc.getMainRenderTarget().bindWrite(false);

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }

    public Uniform getUniform(String name) {
        if (postChain == null) {
            ModernMayhemMod.LOGGER.warn("Cannot get uniform {} because postChain is null", name);
            return null;
        }
        try {
            Field passesField = PostChain.class.getDeclaredField("passes");
            passesField.setAccessible(true);
            List<PostPass> passes = (List<PostPass>) passesField.get(postChain);

            for (PostPass pass : passes) {
                System.out.println("Checking pass: " + pass.getName() + " for shader: " + getShaderName());
                if (pass.getName().equals(getShaderName())) {
                    Uniform uniform = pass.getEffect().getUniform(name);
                    if (uniform != null) {
                        return uniform;
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            ModernMayhemMod.LOGGER.error("Failed to access passes field in PostChain", e);
            return null;
        }
        return null;
    }

    public void setFloatUniform(String name, float value) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(value);
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    public void setIntUniform(String name, int value) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(value);
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    public void setBooleanUniform(String name, boolean value) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(value ? 1 : 0);
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    public void setVec2Uniform(String name, float x, float y) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(x, y);
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    public void setVec3Uniform(String name, float x, float y, float z) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z);
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    public void setVec4Uniform(String name, float x, float y, float z, float w) {
        Uniform uniform = getUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z, w);
        } else {
            ModernMayhemMod.LOGGER.warn("Uniform {} not found in shader {}", name, getShaderName());
        }
    }

    @Override
    public String toString() {
        return "ShaderRenderer{" +
                "shaderLocation=" + shaderLocation +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShaderRenderer)) return false;
        ShaderRenderer that = (ShaderRenderer) o;
        return shaderLocation.equals(that.shaderLocation);
    }

    /**
     * Initializes the shader by creating a PostChain instance.
     * This method is called when the shader is activated or when the screen size changes.
     * Note: This method will close any existing PostChain instance before creating a new one.
     * @throws IOException if there is an error loading the shader resource.
     */
    private void initShader() throws IOException {
        TextureManager textureManager = mc.getTextureManager();
        ResourceManager resourceManager = mc.getResourceManager();
        RenderTarget renderTarget = mc.getMainRenderTarget();

        if (postChain != null) {
            postChain.close();
            postChain = null;
        }

        postChain = new PostChain(
                textureManager,
                resourceManager,
                renderTarget,
                shaderLocation
        );
        postChain.resize(
                renderTarget.width,
                renderTarget.height
        );
    }
}
