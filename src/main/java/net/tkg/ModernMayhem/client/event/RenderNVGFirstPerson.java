package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
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
    public static boolean shouldRenderLeftArm = true; // Whether to render the left arm (prevent the player to grow a third arm if the nvg animation shows the left arm)

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT) // Just to be safe, this event is only fired on the client side (even if it's only a client event)
    // We use RenderHandEvent because it allows us to render the NVG goggles in first-person view in front of the player's hand.
    public static void onRenderOverlay(RenderHandEvent event) {
        // Don't render anything if the item is not registered yet (prevents null pointer exceptions)
        if (!initialized) return;
        if (!shouldRender()) return;
        renderNVGFirstPersonModel(event);
    }

    private static void renderNVGFirstPersonModel(RenderHandEvent event) {
        if (isRendering) return; // Prevent recursive rendering
        LocalPlayer player = MC.player;
        if (player == null) return; // Just to be safe, because if it somehow happened the game might crash (shouldn't be null here)
        PoseStack poseStack = new PoseStack(); // Don't use the event's pose stack to avoid head bob
        MultiBufferSource.BufferSource buffer = MC.renderBuffers().bufferSource();
        float partialTicks = event.getPartialTick();

        // Only render once, either in the main hand event if the offhand is empty, or in the offhand event if the offhand is not empty (because the offhand event is fired after the main hand event)
        if (event.getHand() == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty()) return; // Don't render if the player is holding something in the offhand (we will render it in the offhand event instead)
        if (event.getHand() == InteractionHand.OFF_HAND && player.getOffhandItem().isEmpty()) return; // Don't render if the player is not holding anything in the offhand (we already rendered it in the main hand event)

        event.setCanceled(true); // Cancel the original hand rendering to force render our own before our NVG model
        isRendering = true; // Set the flag to prevent recursive rendering

        // Render the default hand
        ItemInHandRenderer itemInHandRenderer = MC.gameRenderer.itemInHandRenderer;

        // Render the hand first
        if (shouldRenderLeftArm || event.getHand() == InteractionHand.MAIN_HAND) {
            itemInHandRenderer.renderHandsWithItems(
                    partialTicks,
                    event.getPoseStack(),
                    buffer,
                    player,
                    event.getPackedLight()
            );
        }

        poseStack.pushPose();

        var model = RENDERER.getGeoModel();
        var bakedModel = model.getBakedModel(model.getModelResource(DUMMY_ITEM));
        var texture = RENDERER.getTextureLocation(DUMMY_ITEM);

        var renderType = RenderType.itemEntityTranslucentCull(texture);
        buffer.endBatch();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);

        RENDERER.actuallyRender(
                poseStack,
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

        poseStack.popPose();

        isRendering = false; // Reset the flag
    }


    private static boolean shouldRender() {
        // Check if the player is wearing NVG goggles and if they are in first-person view
        LocalPlayer player = MC.player;
        if (player == null) return false;
//        if (!MC.options.getCameraType().isFirstPerson()) return false;
        return CuriosUtil.hasNVGEquipped(player);
    }

    public static void initialiseFirstPersonRenderer() {
        ModernMayhemMod.LOGGER.info("["+ ModernMayhemMod.ID + "] Initializing NVG First Person Renderer");
        if (initialized) return;
        // This is just to ensure that the renderer is initialized
        DUMMY_ITEM = (NVGFirstPersonFakeItem) ItemRegistryMM.FIRST_PERSON_NVG.get();
        RENDERER.initCurrentItemStack(DUMMY_ITEM);
        initialized = true;
    }
}
