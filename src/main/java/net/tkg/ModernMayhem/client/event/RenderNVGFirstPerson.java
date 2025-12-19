package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tacz.guns.api.item.IGun;
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
    public static boolean shouldRenderLeftArm = true; // Whether to render the left arm (prevent the player to grow a third arm if the nvg animation shows the left arm)

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT) // Just to be safe, this event is only fired on the client side (even if it's only a client event)
    // We use RenderHandEvent because it allows us to render the NVG goggles in first-person view in front of the player's hand.
    public static void onRenderOverlay(RenderHandEvent event) {
        // Don't render anything if the item is not registered yet (prevents null pointer exceptions)
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
        if (shouldRenderLeftArm || event.getHand() == InteractionHand.MAIN_HAND) {
            itemInHandRenderer.renderHandsWithItems(
                    partialTicks,
                    handStack,
                    buffer,
                    player,
                    event.getPackedLight()
            );
        }

        RenderSystem.clear(256, Minecraft.ON_OSX); // Needed so nvg model and gun model doesn't clip through each other when nvg animation plays

        PoseStack nvgStack = new PoseStack();

        nvgStack.pushPose();

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
//        if (!MC.options.getCameraType().isFirstPerson()) return false;
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