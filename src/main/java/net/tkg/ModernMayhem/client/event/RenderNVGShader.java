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
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.NVGConfigs;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderNVGShader {

    private static final ShaderRenderer NVG_SHADER_RENDERER = new ShaderRenderer(ResourceLocation.fromNamespaceAndPath(ModernMayhemMod.ID, "shaders/post/night-vision.json"));
    public static boolean oculusShaderEnabled = false;

    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (oculusShaderEnabled) return;
        try {
            renderNVGShader(event.getPartialTick());
        } catch (Exception e) {
            // Log error but don't crash
            System.err.println("[ModernMayhem] Error rendering NVG shader: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
        if (!oculusShaderEnabled) return;
        try {
            renderNVGShader(event.getPartialTick());
        } catch (Exception e) {
            // Log error but don't crash
            System.err.println("[ModernMayhem] Error rendering NVG shader: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void renderNVGShader(float partialTick) {
        LocalPlayer player = mc.player;
        if (player == null) return;

        boolean shouldRender = false;
        GenericNVGGogglesItem.NVGConfig nvgItemConfig = null;
        ItemStack facewearItem = null;

        if (CuriosUtil.hasNVGEquipped(player)) {
            facewearItem = CuriosUtil.getFaceWearItem(player);
            if (facewearItem != null && facewearItem.getItem() instanceof NVGGogglesItem nvgGogglesItem) {
                if (nvgGogglesItem.shouldRenderShader()) {
                    shouldRender = GenericNVGGogglesItem.getNVGMode(facewearItem) == 1;
                    try {
                        nvgItemConfig = GenericNVGGogglesItem.getCurrentConfig(facewearItem);
                    } catch (Exception e) {
                        System.err.println("[ModernMayhem] Error getting NVG config: " + e.getMessage());
                        shouldRender = false;
                    }
                }
            }
        }

        if (shouldRender && !NVG_SHADER_RENDERER.isActive()) {
            NVG_SHADER_RENDERER.activate();
        } else if (!shouldRender && NVG_SHADER_RENDERER.isActive()) {
            NVG_SHADER_RENDERER.deactivate();
        }

        NVG_SHADER_RENDERER.render();

        if (NVG_SHADER_RENDERER.isActive() && nvgItemConfig != null && facewearItem != null) {
            try {
                boolean isUltraGamer = (facewearItem.getItem() instanceof NVGGogglesItem nvgGogglesItem && nvgGogglesItem.isGamerNVG());
                NVG_SHADER_RENDERER.setFloatUniform("Brightness", nvgItemConfig.getBrightness());
                NVG_SHADER_RENDERER.setFloatUniform("RedValue", isUltraGamer ? NVGConfigs.getUltraGamerRedValue() : nvgItemConfig.getRedValue());
                NVG_SHADER_RENDERER.setFloatUniform("GreenValue", isUltraGamer ? NVGConfigs.getUltraGamerGreenValue() : nvgItemConfig.getGreenValue());
                NVG_SHADER_RENDERER.setFloatUniform("BlueValue", isUltraGamer ? NVGConfigs.getUltraGamerBlueValue() : nvgItemConfig.getBlueValue());

                boolean autoGainActive = GenericNVGGogglesItem.isAutoGainEnabled(facewearItem);
                NVG_SHADER_RENDERER.setFloatUniform("AutoGainEnabled", autoGainActive ? 1.0f : 0.0f);
            } catch (Exception e) {
                System.err.println("[ModernMayhem] Error setting shader uniforms: " + e.getMessage());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderNVGOverlay(RenderGuiEvent.Pre event) {
        try {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            if (!NVG_SHADER_RENDERER.isActive()) return;

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
                        GenericNVGGogglesItem.getNVGMode(facewearItem) == 1 &&
                        Minecraft.getInstance().options.getCameraType().isFirstPerson()) {

                    GenericNVGGogglesItem.NVGConfig config = GenericNVGGogglesItem.getCurrentConfig(facewearItem);
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
            System.err.println("[ModernMayhem] Error rendering NVG overlay: " + e.getMessage());
        }
    }
}