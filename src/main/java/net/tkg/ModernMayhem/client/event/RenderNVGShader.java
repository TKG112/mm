package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.shaderRenderer.NVGShaderRenderer;
import net.tkg.ModernMayhem.client.shaderController.NVGShaderController;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderNVGShader {

    public static boolean oculusShaderEnabled = false;

//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void onRenderHand(RenderHandEvent event) {
//        if (oculusShaderEnabled) return;
//        try {
//            NVGShaderRenderer.INSTANCE.render();
//        } catch (Exception e) {
//            ModernMayhemMod.LOGGER.error("Error rendering NVG shader", e);
//        }
//    }
//
//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void onRenderLevel(RenderLevelStageEvent event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
//        if (!oculusShaderEnabled) return;
//        try {
//            NVGShaderRenderer.INSTANCE.render();
//        } catch (Exception e) {
//            ModernMayhemMod.LOGGER.error("Error rendering NVG shader", e);
//        }
//    }


    @SubscribeEvent
    public static void onRenderScreenEffects(RenderGuiEvent.Pre event) {
        NVGShaderRenderer.INSTANCE.render();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderNVGOverlay(RenderGuiEvent.Pre event) {
        try {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            if (!NVGShaderController.isEnabled()) return;

            int screenWidth = event.getWindow().getGuiScaledWidth();
            int screenHeight = event.getWindow().getGuiScaledHeight();

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
            if (facewearItem != null && facewearItem.getItem() instanceof NVGGogglesItem nvgGogglesItem) {

                if (nvgGogglesItem.shouldRenderShader() &&
                        GenericSpecialGogglesItem.getNVGMode(facewearItem) == 1 &&
                        Minecraft.getInstance().options.getCameraType().isFirstPerson()) {

                    GenericSpecialGogglesItem.NVGConfig config = GenericSpecialGogglesItem.getCurrentConfig(facewearItem);
                    if (config != null && config.getOverlay() != null) {
                        event.getGuiGraphics().blit(config.getOverlay(),
                                0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
                    }
                }
            }

            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.error("Error rendering NVG overlay", e);
        }
    }
}