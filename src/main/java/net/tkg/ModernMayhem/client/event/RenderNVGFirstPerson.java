package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
        LocalPlayer player = MC.player;
        PoseStack poseStack = new PoseStack(); // Don't use the event's pose stack to avoid head bob
        MultiBufferSource.BufferSource buffer = MC.renderBuffers().bufferSource();
        float partialTicks = event.getPartialTick();

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
