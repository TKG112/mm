package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tacz.guns.api.item.IGun;
import com.github.argon4w.acceleratedrendering.features.items.AcceleratedItemRenderingFeature;
import com.github.argon4w.acceleratedrendering.features.entities.AcceleratedEntityRenderingFeature;
import net.minecraftforge.fml.ModList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;
import net.tkg.ModernMayhem.client.renderer.custom.NVGFirstPersonRenderer;
import net.tkg.ModernMayhem.server.registry.ItemRegistryMM;
import net.tkg.ModernMayhem.server.util.CuriosUtil;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderNVGFirstPerson {
    private static final Minecraft MC = Minecraft.getInstance();
    private static NVGFirstPersonFakeItem DUMMY_ITEM = null;
    private static final NVGFirstPersonRenderer RENDERER = new NVGFirstPersonRenderer();
    private static boolean initialized = false;
    private static boolean isRendering = false;
    public static boolean shouldRenderLeftArm = true;

    // [NEW] Cache AR status to avoid checking ModList every frame
    private static final boolean IS_AR_LOADED = ModList.get().isLoaded("acceleratedrendering");

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    public static void onRenderOverlay(RenderHandEvent event) {
        if (!initialized) return;
        if (!shouldRender()) return;
        renderNVGFirstPersonModel(event);
    }

    private static void renderNVGFirstPersonModel(RenderHandEvent event) {
        if (isRendering) return;
        LocalPlayer player = MC.player;
        if (player == null) return;

        MultiBufferSource.BufferSource buffer = MC.renderBuffers().bufferSource();
        float partialTicks = event.getPartialTick();

        if (event.getHand() == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty()) return;
        if (event.getHand() == InteractionHand.OFF_HAND && player.getOffhandItem().isEmpty()) return;

        event.setCanceled(true);
        isRendering = true;

        ItemStack mainHandStack = player.getMainHandItem();
        boolean isHoldingGun = IGun.getIGunOrNull(mainHandStack) != null;

        PoseStack handStack = isHoldingGun ? new PoseStack() : event.getPoseStack();
        ItemInHandRenderer itemInHandRenderer = MC.gameRenderer.itemInHandRenderer;

        // [FIX START] ================================================================
        // Force Accelerated Rendering to use the Vanilla Pipeline for this specific pass.
        // This disables "Ring Buffering" and ensures the gun is drawn IMMEDIATELY,
        // so it exists in the depth buffer BEFORE we clear it.
        if (IS_AR_LOADED) {
            try {
                // We disable acceleration for both Items (Guns) and Entities
                AcceleratedItemRenderingFeature.useVanillaPipeline();
                AcceleratedEntityRenderingFeature.useVanillaPipeline();
            } catch (Throwable ignored) {
                // Fail silently if API changes or linking errors occur
            }
        }

        if (shouldRenderLeftArm || event.getHand() == InteractionHand.MAIN_HAND) {
            itemInHandRenderer.renderHandsWithItems(
                    partialTicks,
                    handStack,
                    buffer,
                    player,
                    event.getPackedLight()
            );
        }

        // Flush standard buffers (still good practice)
        buffer.endBatch();

        // Restore AR pipeline state so the rest of the game renders normally
        if (IS_AR_LOADED) {
            try {
                AcceleratedItemRenderingFeature.resetPipeline();
                AcceleratedEntityRenderingFeature.resetPipeline();
            } catch (Throwable ignored) {}
        }
        // [FIX END] ==================================================================

        // Now safe to clear depth - the gun is definitely drawn.
        RenderSystem.clear(256, Minecraft.ON_OSX);

        PoseStack nvgStack = new PoseStack();
        nvgStack.pushPose();

        var model = RENDERER.getGeoModel();
        var bakedModel = model.getBakedModel(model.getModelResource(DUMMY_ITEM));
        var texture = RENDERER.getTextureLocation(DUMMY_ITEM);

        var renderType = RenderType.itemEntityTranslucentCull(texture);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);

        RENDERER.actuallyRender(
                nvgStack,
                DUMMY_ITEM,
                bakedModel,
                renderType,
                buffer,
                buffer.getBuffer(renderType),
                false,
                MC.getFrameTime(),
                event.getPackedLight(),
                OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f
        );

        buffer.endBatch();

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();

        nvgStack.popPose();

        isRendering = false;
    }

    private static boolean shouldRender() {
        LocalPlayer player = MC.player;
        if (player == null) return false;
        return CuriosUtil.hasNVGEquipped(player);
    }

    public static void initialiseFirstPersonRenderer() {
        ModernMayhemMod.LOGGER.info("["+ ModernMayhemMod.ID + "] Initializing NVG First Person Renderer");
        if (initialized) return;
        DUMMY_ITEM = (NVGFirstPersonFakeItem) ItemRegistryMM.FIRST_PERSON_NVG.get();
        RENDERER.initCurrentItemStack(DUMMY_ITEM);
        initialized = true;
    }
}