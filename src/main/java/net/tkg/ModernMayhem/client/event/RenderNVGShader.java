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
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.NVGConfigs;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderNVGShader {

    private static final ShaderRenderer NVG_SHADER_RENDERER = new ShaderRenderer(ResourceLocation.fromNamespaceAndPath(ModernMayhemMod.ID, "shaders/post/night-vision.json"));
    public static boolean oculusShaderEnabled = false;

    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderHand(RenderHandEvent event) {
        if (oculusShaderEnabled) return;
        try {
            renderNVGShader(event.getPartialTick());
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.error("Error rendering NVG shader", e);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
        if (!oculusShaderEnabled) return;
        try {
            renderNVGShader(event.getPartialTick());
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.error("Error rendering NVG shader", e);
        }
    }

    private static void renderNVGShader(float partialTick) {
        LocalPlayer player = mc.player;
        if (player == null) return;

        boolean shouldRender = false;
        GenericSpecialGogglesItem.NVGConfig nvgItemConfig = null;
        ItemStack facewearItem = null;

        if (CuriosUtil.hasNVGEquipped(player)) {
            facewearItem = CuriosUtil.getFaceWearItem(player);
            if (facewearItem != null && facewearItem.getItem() instanceof NVGGogglesItem nvgGogglesItem) {
                if (nvgGogglesItem.shouldRenderShader()) {
                    shouldRender = GenericSpecialGogglesItem.getNVGMode(facewearItem) == 1;
                    try {
                        nvgItemConfig = GenericSpecialGogglesItem.getCurrentConfig(facewearItem);
                    } catch (Exception e) {
                        ModernMayhemMod.LOGGER.error("Error getting NVG config", e);
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

                boolean autoGainActive = false;
                boolean autoGatingActive = false;

                if (facewearItem.getItem() instanceof NVGGogglesItem nvgItem) {
                    autoGainActive = nvgItem.hasAutoGain() && NVGGogglesItem.isAutoGainEnabled(facewearItem);

                    if (!autoGainActive && nvgItem.hasAutoGating()) {
                        autoGatingActive = true;
                    }
                }

                // Set uniforms for the autogain pass
                NVG_SHADER_RENDERER.setFloatUniform("mm:autogain", "AutoGainEnabled", autoGainActive ? 1.0f : 0.0f);
                NVG_SHADER_RENDERER.setFloatUniform("mm:autogain", "MinGain", nvgItemConfig.getMinGain());
                NVG_SHADER_RENDERER.setFloatUniform("mm:autogain", "MaxGain", nvgItemConfig.getMaxGain());
                NVG_SHADER_RENDERER.setFloatUniform("mm:autogain", "AutoGainSpeed", nvgItemConfig.getAutoGainSpeed());

                // Set uniforms for the autogating pass
                NVG_SHADER_RENDERER.setFloatUniform("mm:autogating", "AutoGatingEnabled", autoGatingActive ? 1.0f : 0.0f);
                NVG_SHADER_RENDERER.setFloatUniform("mm:autogating", "AutoGatingOffset", nvgItemConfig.getAutoGatingOffset());
                NVG_SHADER_RENDERER.setFloatUniform("mm:autogating", "AutoGatingSpeed", nvgItemConfig.getAutoGatingSpeed());

                // Set uniforms for the night-vision pass
                NVG_SHADER_RENDERER.setFloatUniform("mm:night-vision", "AutoGainEnabled", autoGainActive ? 1.0f : 0.0f);
                NVG_SHADER_RENDERER.setFloatUniform("mm:night-vision", "AutoGatingEnabled", autoGatingActive ? 1.0f : 0.0f);
                NVG_SHADER_RENDERER.setFloatUniform("mm:night-vision", "Brightness", nvgItemConfig.getBrightness());
                NVG_SHADER_RENDERER.setFloatUniform("mm:night-vision", "RedValue", isUltraGamer ? NVGConfigs.getUltraGamerRedValue() : nvgItemConfig.getRedValue());
                NVG_SHADER_RENDERER.setFloatUniform("mm:night-vision", "GreenValue", isUltraGamer ? NVGConfigs.getUltraGamerGreenValue() : nvgItemConfig.getGreenValue());
                NVG_SHADER_RENDERER.setFloatUniform("mm:night-vision", "BlueValue", isUltraGamer ? NVGConfigs.getUltraGamerBlueValue() : nvgItemConfig.getBlueValue());
                NVG_SHADER_RENDERER.setFloatUniform("mm:night-vision", "NoiseMultiplier", nvgItemConfig.getNoiseMultiplier());

            } catch (Exception e) {
                ModernMayhemMod.LOGGER.error("Error setting shader uniforms", e);
            }
        }
    }

    public static boolean isNvActive() {
        return NVG_SHADER_RENDERER.isActive();
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