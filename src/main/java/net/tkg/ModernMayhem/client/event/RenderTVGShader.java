package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.ShaderRenderer;
import net.tkg.ModernMayhem.server.item.curios.facewear.TVGGogglesItem;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderTVGShader {

    private static final ShaderRenderer THERMAL_SHADER_RENDERER = new ShaderRenderer(
            ResourceLocation.fromNamespaceAndPath(ModernMayhemMod.ID, "shaders/post/thermal-vision.json")
    );

    public static boolean oculusShaderEnabled = false;

    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderHand(RenderHandEvent event) {
        if (oculusShaderEnabled) return;
        try {
            renderThermalShader(event.getPartialTick());
        } catch (Exception e) {
            System.err.println("[ModernMayhem] Error rendering thermal shader: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
        if (!oculusShaderEnabled) return;
        try {
            renderThermalShader(event.getPartialTick());
        } catch (Exception e) {
            System.err.println("[ModernMayhem] Error rendering thermal shader: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void renderThermalShader(float partialTick) {
        LocalPlayer player = mc.player;
        if (player == null) return;

        boolean shouldRender = false;
        GenericSpecialGogglesItem.NVGConfig thermalConfig = null;
        ItemStack facewearItem = null;

        if (CuriosUtil.hasNVGEquipped(player)) {
            facewearItem = CuriosUtil.getFaceWearItem(player);
            if (facewearItem.getItem() instanceof TVGGogglesItem thermalItem) {
                if (thermalItem.shouldRenderShader()) {
                    shouldRender = GenericSpecialGogglesItem.getNVGMode(facewearItem) == 1;
                    thermalConfig = GenericSpecialGogglesItem.getCurrentConfig(facewearItem);
                }
            }
        }

        if (shouldRender && !THERMAL_SHADER_RENDERER.isActive()) {
            THERMAL_SHADER_RENDERER.activate();
        } else if (!shouldRender && THERMAL_SHADER_RENDERER.isActive()) {
            THERMAL_SHADER_RENDERER.deactivate();
        }

        THERMAL_SHADER_RENDERER.render();
    }

    public static boolean isThermalActive() {
        return THERMAL_SHADER_RENDERER.isActive();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderThermalOverlay(RenderGuiEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!THERMAL_SHADER_RENDERER.isActive()) return;

        int screenWidth = event.getWindow().getGuiScaledWidth();
        int screenHeight = event.getWindow().getGuiScaledHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        RenderSystem.setShaderColor(1, 1, 1, 1);

        ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
        if (facewearItem.getItem() instanceof TVGGogglesItem) {
            if (GenericSpecialGogglesItem.getNVGMode(facewearItem) == 1 &&
                    Minecraft.getInstance().options.getCameraType().isFirstPerson() &&
                    GenericSpecialGogglesItem.getCurrentConfig(facewearItem).getOverlay() != null) {

                event.getGuiGraphics().blit(
                        GenericSpecialGogglesItem.getCurrentConfig(facewearItem).getOverlay(),
                        0, 0, 0, 0,
                        screenWidth, screenHeight,
                        screenWidth, screenHeight
                );
            }
        }

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}