package net.tkg.ModernMayhem.client.shaderRenderer;

import net.tkg.ModernMayhem.client.thermal.ThermalPalettes;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.utils.DynamicPostChain;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public abstract class ShaderRendererBase {
    private final Minecraft MC = Minecraft.getInstance();
    private final ResourceLocation POST_CHAIN_LOCATION;

    protected DynamicPostChain postChain;
    /** Chain the current {@link #postChain} was built from; rebuilt when the wanted chain changes. */
    private ResourceLocation activeChainLocation;
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

        // A content pack can supply its own chain, so the wanted chain may change when gear changes.
        ResourceLocation wanted = resolveChainLocation();
        if (postChain != null && !wanted.equals(activeChainLocation)) {
            close();
        }

        if (postChain == null) {
            try {
                postChain = new DynamicPostChain(
                        MC.getTextureManager(),
                        MC.getResourceManager(),
                        MC.getMainRenderTarget(),
                        wanted,
                        getExternalTargets()
                );
                postChain.resize(width, height);
                lastWidth = width;
                lastHeight = height;
                activeChainLocation = wanted;
                onChainCreated(postChain);
                if (!wanted.equals(POST_CHAIN_LOCATION)) {
                    warnAboutMissingUniforms(postChain, wanted);
                }
            } catch (Exception exception) {
                ModernMayhemMod.LOGGER.error("Failed to create {} dynamic post chain", wanted, exception);
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

    /**
     * Called once after the post chain is built, for attaching anything the chain's JSON can't express
     * -- chain JSON can only reference render targets, not textures generated at runtime.
     * <p>
     * By default this binds the thermal palette LUT, which both the night-vision and thermal chains
     * need: they each include a {@code mm:thermal_composite} pass.
     */
    protected void onChainCreated(DynamicPostChain chain) {
        ThermalPalettes.bindTo(chain);
    }

    /**
     * Which post chain to use right now. Defaults to the one this renderer was built with; overridden
     * so an equipped goggle can substitute a chain supplied by a content pack.
     */
    protected ResourceLocation resolveChainLocation() {
        return POST_CHAIN_LOCATION;
    }

    /** The equipped goggle's own chain, or this renderer's default when it doesn't supply one. */
    protected ResourceLocation chainFromEquippedGoggles() {
        if (MC.player == null) {
            return POST_CHAIN_LOCATION;
        }
        ItemStack face = CuriosUtil.getFaceWearItem(MC.player);
        if (face != null && face.getItem() instanceof GenericSpecialGogglesItem goggles) {
            ResourceLocation custom = goggles.getPostChain();
            if (custom != null) {
                return custom;
            }
        }
        return POST_CHAIN_LOCATION;
    }

    /**
     * Uniforms this renderer drives. A custom chain missing one of these still loads and renders, but
     * that feature silently stops working -- so we name what's missing rather than let an author guess.
     */
    protected String[] drivenUniforms() {
        return new String[0];
    }

    private void warnAboutMissingUniforms(DynamicPostChain chain, ResourceLocation location) {
        List<String> missing = new ArrayList<>();
        for (String uniform : drivenUniforms()) {
            if (!chain.hasUniform(uniform)) {
                missing.add(uniform);
            }
        }
        if (missing.isEmpty()) {
            ModernMayhemMod.LOGGER.info("[MM] Using custom post chain '{}'", location);
            return;
        }
        ModernMayhemMod.LOGGER.warn(
                "[MM] Custom post chain '{}' has no pass accepting {} -- those effects will not work. "
                        + "If you did not intend to write your own shader passes, remove \"post_chain\" "
                        + "from the goggle definition to use ModernMayhem's default.",
                location, missing);
    }

    public void close() {
        if (postChain != null) {
            postChain.close();
            postChain = null;
        }
        activeChainLocation = null;

        lastWidth = -1;
        lastHeight = -1;
        processErrorLogged = false;
    }
}