package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;
import net.tkg.ModernMayhem.client.renderer.custom.NVGFirstPersonRenderer;
import net.tkg.ModernMayhem.client.renderer.ShaderCompatibleRenderTypes;
import net.tkg.ModernMayhem.client.registry.ClientItemRegistryMM;
import net.tkg.ModernMayhem.server.item.curios.facewear.VisorItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.compat.ARCompat;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib.cache.object.BakedGeoModel;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderNVGFirstPerson {
    private static final Minecraft MC = Minecraft.getInstance();
    private static NVGFirstPersonFakeItem DUMMY_ITEM = null;
    private static final NVGFirstPersonRenderer RENDERER = new NVGFirstPersonRenderer();
    private static boolean initialized = false;
    private static boolean isRendering = false;
    public static boolean shouldRenderLeftArm = true;

    private static final boolean TACZ_LOADED = ModList.get().isLoaded("tacz");

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
        boolean isHoldingGun = TACZ_LOADED && isTACZGun(mainHandStack);

        PoseStack handStack = isHoldingGun ? new PoseStack() : event.getPoseStack();
        ItemInHandRenderer itemInHandRenderer = MC.gameRenderer.itemInHandRenderer;

        ARCompat.disableAcceleration();

        if (shouldRenderLeftArm || event.getHand() == InteractionHand.MAIN_HAND) {
            itemInHandRenderer.renderHandsWithItems(
                    partialTicks,
                    handStack,
                    buffer,
                    player,
                    event.getPackedLight()
            );
        }

        buffer.endBatch();

        ARCompat.resetAcceleration();

        PoseStack nvgStack = new PoseStack();
        nvgStack.pushPose();

        var model = RENDERER.getGeoModel();
        var bakedModel = model.getBakedModel(model.getModelResource(DUMMY_ITEM));
        var texture = RENDERER.getTextureLocation(DUMMY_ITEM);

        ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
        boolean isVisor = facewearItem != null && facewearItem.getItem() instanceof VisorItem;

        if (RenderNVGShader.oculusShaderEnabled) {
            if (isVisor) {
                renderVisorWithShaderSupport(nvgStack, buffer, event, bakedModel, texture);
            } else {
                renderNVGWithShaderSupport(nvgStack, buffer, event, bakedModel, texture);
            }
        } else {
            renderWithoutShaders(nvgStack, buffer, event, bakedModel, texture, isVisor);
        }

        RenderSystem.disableDepthTest();

        nvgStack.popPose();

        isRendering = false;
    }

    private static void renderVisorWithShaderSupport(PoseStack nvgStack, MultiBufferSource.BufferSource buffer, RenderHandEvent event, BakedGeoModel bakedModel, ResourceLocation texture) {
        var renderType = ShaderCompatibleRenderTypes.visorTranslucent(texture);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

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

        RenderSystem.disableBlend();
    }

    private static void renderNVGWithShaderSupport(PoseStack nvgStack, MultiBufferSource.BufferSource buffer, RenderHandEvent event, BakedGeoModel bakedModel, ResourceLocation texture) {
        GL11.glColorMask(false, false, false, false);
        GL11.glDepthMask(true);
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        GL11.glDisable(GL11.GL_BLEND);

        var depthOnlyType = RenderType.entityTranslucentCull(texture);

        RENDERER.actuallyRender(
                nvgStack,
                DUMMY_ITEM,
                bakedModel,
                depthOnlyType,
                buffer,
                buffer.getBuffer(depthOnlyType),
                false,
                MC.getFrameTime(),
                event.getPackedLight(),
                OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f
        );

        buffer.endBatch();

        GL11.glColorMask(true, true, true, true);
        GL11.glDepthMask(false);
        GL11.glDepthFunc(GL11.GL_EQUAL);
        GL11.glDisable(GL11.GL_BLEND);

        var colorType = RenderType.entityTranslucentCull(texture);

        RENDERER.actuallyRender(
                nvgStack,
                DUMMY_ITEM,
                bakedModel,
                colorType,
                buffer,
                buffer.getBuffer(colorType),
                false,
                MC.getFrameTime(),
                event.getPackedLight(),
                OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f
        );

        buffer.endBatch();

        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_BLEND);
    }

    private static void renderWithoutShaders(PoseStack nvgStack, MultiBufferSource.BufferSource buffer, RenderHandEvent event, BakedGeoModel bakedModel, ResourceLocation texture, boolean isVisor) {
        var renderType = isVisor ? RenderType.entityTranslucent(texture) : RenderType.entityTranslucentCull(texture);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        GL11.glDepthFunc(GL11.GL_ALWAYS);
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

        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    private static boolean isTACZGun(ItemStack stack) {
        try {
            Class<?> iGunClass = Class.forName("com.tacz.guns.api.item.IGun");
            java.lang.reflect.Method method = iGunClass.getMethod("getIGunOrNull", ItemStack.class);
            Object result = method.invoke(null, stack);
            return result != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean shouldRender() {
        LocalPlayer player = MC.player;
        if (player == null) return false;
        return CuriosUtil.hasNVGEquipped(player);
    }

    public static void initialiseFirstPersonRenderer() {
        ModernMayhemMod.LOGGER.info("["+ ModernMayhemMod.ID + "] Initializing NVG First Person Renderer");
        if (initialized) return;
        DUMMY_ITEM = (NVGFirstPersonFakeItem) ClientItemRegistryMM.FIRST_PERSON_NVG.get();
        RENDERER.initCurrentItemStack(DUMMY_ITEM);
        initialized = true;
    }
}
