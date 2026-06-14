package net.tkg.ModernMayhem.client.shaderRenderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.utils.DynamicPostChain;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.Collections;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public abstract class ShaderRendererBase {
    private final Minecraft MC = Minecraft.getInstance();
    private final ResourceLocation POST_CHAIN_LOCATION;

    protected DynamicPostChain postChain;
    private int lastWidth = -1;
    private int lastHeight = -1;
    private boolean processErrorLogged = false;

    public ShaderRendererBase(ResourceLocation shaderLocation) { this.POST_CHAIN_LOCATION = shaderLocation; }

    public void render() {
        if (MC.level == null || MC.player == null) {
            close();
            return;
        }

        if (MC.getMainRenderTarget() == null) {
            return;
        }

        ensureLoaded();
        if (postChain == null) {
            return;
        }

        syncUniforms();
        MC.renderBuffers().bufferSource().endBatch();
        try {
            postChain.process(MC.getFrameTime());
        } catch (Exception e) {
            if (!processErrorLogged) {
                processErrorLogged = true;
                ModernMayhemMod.LOGGER.error("Post chain '{}' failed during process()", POST_CHAIN_LOCATION.getPath(), e);
            }
        } finally {
            restoreHudState();
        }
    }

    private void restoreHudState() {
        RenderTarget main = MC.getMainRenderTarget();
        main.bindWrite(false);
        RenderSystem.viewport(0, 0, main.width, main.height);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        for (int unit = 0; unit < 8; unit++) {
            GlStateManager._activeTexture(GL13.GL_TEXTURE0 + unit);
            GlStateManager._bindTexture(0);
        }
        GlStateManager._activeTexture(GL13.GL_TEXTURE0);
        BufferUploader.reset();
    }

    private void ensureLoaded() {
        int width = MC.getMainRenderTarget().width;
        int height = MC.getMainRenderTarget().height;

        if (postChain == null) {
            try {
                postChain = new DynamicPostChain(
                        MC.getTextureManager(),
                        MC.getResourceManager(),
                        MC.getMainRenderTarget(),
                        POST_CHAIN_LOCATION,
                        getExternalTargets()
                );
                postChain.resize(width, height);
                lastWidth = width;
                lastHeight = height;
            } catch (Exception exception) {
                ModernMayhemMod.LOGGER.error("Failed to create {} dynamic post chain", POST_CHAIN_LOCATION.getPath(), exception);
                close();
            }
            return;
        }

        if (width != lastWidth || height != lastHeight) {
            postChain.resize(width, height);
            lastWidth = width;
            lastHeight = height;
        }
    }

    protected Map<String, RenderTarget> getExternalTargets() {
        return Collections.emptyMap();
    }

    protected abstract void syncUniforms();

    public void close() {
        if (postChain != null) {
            postChain.close();
            postChain = null;
        }

        lastWidth = -1;
        lastHeight = -1;
        processErrorLogged = false;
    }
}