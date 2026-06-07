package net.tkg.ModernMayhem.client.shaderRenderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.utils.DynamicPostChain;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public abstract class ShaderRendererBase {
    private final Minecraft MC = Minecraft.getInstance();
    private final ResourceLocation POST_CHAIN_LOCATION;

    protected DynamicPostChain postChain;
    private int lastWidth = -1;
    private int lastHeight = -1;

    /**
     * Creates a new ShaderRendererBase with the specified shader location. The shader location should point to a valid post-processing shader JSON file.
     * @param shaderLocation The resource location of the shader JSON file to use for this renderer's post-processing effect.
     * @implNote When implementing a subclass of ShaderRendererBase you should make it a singleton so you can easily call the render method
     */
    public ShaderRendererBase(ResourceLocation shaderLocation) { this.POST_CHAIN_LOCATION = shaderLocation; }

    /**
     * Renders the shader effect. This method should be called every frame (e.g. from a RenderGuiEvent.Pre or RenderGuiEvent.Post if you want to affect the gui or not) to apply the shader effect continuously.
     */
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
        postChain.process(MC.getFrameTime());
        restoreHudState();
    }

    /**
     * Restores a known, HUD-ready GL state after the post chain has run.
     * <p>
     * A {@link net.minecraft.client.renderer.PostChain} leaves GL state dirty: the viewport is
     * sized to its last pass's output target (smaller than the window if any pass downsamples),
     * depthFunc is left at GL_ALWAYS, blend/shader state is whatever the last pass set, and
     * unbindWrite only ever binds framebuffer 0. When more than one post effect runs on the same
     * RenderGuiEvent.Pre (e.g. TVG and NVG, whose listener order Forge does not guarantee), the
     * HUD inherits the leftover state of whichever effect ran last - which made HUD elements
     * vanish unpredictably. Resetting after every effect makes the run order irrelevant.
     */
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
    }

    /// Ensures the post chain is loaded and ready to use. If the main render target's size has changed since the last time it was loaded, it will resize the post chain's buffers to match the new size.
    private void ensureLoaded() {
        int width = MC.getMainRenderTarget().width;
        int height = MC.getMainRenderTarget().height;

        if (postChain == null) {
            try {
                postChain = new DynamicPostChain(
                        MC.getTextureManager(),
                        MC.getResourceManager(),
                        MC.getMainRenderTarget(),
                        POST_CHAIN_LOCATION
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

        // Handle resizing - if the main render target size changes, we need to resize the post chain's buffers
        if (width != lastWidth || height != lastHeight) {
            postChain.resize(width, height);
            lastWidth = width;
            lastHeight = height;
        }
    }

    /**
     * Syncs any necessary uniforms for the shader.
     * This method is called every frame before processing the post chain, so it can be used to update any dynamic values that the shader needs (e.g. player health, time, etc.).
     * Implementations should set the appropriate uniforms on the post chain's shader(s) based on the current game state.
     */
    protected abstract void syncUniforms();

    /**
     * Closes the shader renderer and releases any resources it is using.
     * After calling this method, the shader renderer will no longer be active and will need to be reloaded (e.g. by calling ensureLoaded) before it can be used again.
     */
    public void close() {
        if (postChain != null) {
            postChain.close();
            postChain = null;
        }

        lastWidth = -1;
        lastHeight = -1;
    }
}