package net.tkg.ModernMayhem.client.event;

import com.eliotlash.mclib.math.functions.limit.Min;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.facewear.UltraGamerGPNVGItem;
import net.tkg.ModernMayhem.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.mixinaccessor.PostChainAccess;
import net.tkg.ModernMayhem.util.CuriosUtil;
import net.tkg.ModernMayhem.util.NVGConfigs;

import java.util.Objects;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RenderNVG {
    private static final ResourceLocation NVG_SHADER_PATH = new ResourceLocation(ModernMayhemMod.ID, "shaders/post/night-vision.json");
    private static int lastWidth = -1;
    private static int lastHeight = -1;
    public static PostPass passe = null;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderNVGOverlay(RenderGuiEvent.Pre event) {
        int screenWidth = event.getWindow().getGuiScaledWidth();
        int screenHeight = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            if (CuriosUtil.hasNVGEquipped(player)) {
                ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
                if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                    if (GenericNVGGogglesItem.getNVGMode(facewearItem) == 1 && Minecraft.getInstance().options.getCameraType().isFirstPerson() && GenericNVGGogglesItem.getCurrentConfig(facewearItem).getOverlay() != null) {
                        event.getGuiGraphics().blit(GenericNVGGogglesItem.getCurrentConfig(facewearItem).getOverlay(), 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
                    }
                }
            }
            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }


    @SubscribeEvent
    public static void renderNVGEffect(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) {
            return;
        }
        boolean hasNvgItem = false;
        boolean nvgOn = false;
        GenericNVGGogglesItem.NVGConfig nvgItemConfig = null;
        ItemStack facewearItem = null;
        // Check if the player has an item inheriting from GenericNVGGooglesItem
        // and if it is turned on
        if (CuriosUtil.hasNVGEquipped(player)) {
             facewearItem = CuriosUtil.getFaceWearItem(player);
            if (facewearItem.getItem() instanceof GenericNVGGogglesItem genericNVGGogglesItem) {
                hasNvgItem = true;
                nvgOn = GenericNVGGogglesItem.getNVGMode(CuriosUtil.getFaceWearItem(player)) == 1;
                nvgItemConfig = GenericNVGGogglesItem.getCurrentConfig(facewearItem);
            }
        }

        float nightVisionEnabled = (hasNvgItem && nvgOn) ? 1.0f : 0.0f;
        if (mc.gameRenderer.currentEffect() == null || !Objects.requireNonNull(mc.gameRenderer.currentEffect()).getName().equals(NVG_SHADER_PATH.toString())) {
            if (mc.options.getCameraType().isFirstPerson()) {
                initializeShader(mc);
            }
        }

        if (mc.gameRenderer.currentEffect() != null && Objects.requireNonNull(mc.gameRenderer.currentEffect()).getName().equals(NVG_SHADER_PATH.toString())) {
            updateShaderUniforms(nightVisionEnabled, nvgItemConfig, (facewearItem != null && facewearItem.getItem() instanceof UltraGamerGPNVGItem));
        }
    }

    private static void initializeShader(Minecraft mc) {
        try {
            lastWidth = mc.getWindow().getWidth();
            lastHeight = mc.getWindow().getHeight();
            mc.gameRenderer.loadEffect(NVG_SHADER_PATH);
            Objects.requireNonNull(mc.gameRenderer.currentEffect()).resize(lastWidth, lastHeight);
            passe = ((PostChainAccess) Objects.requireNonNull(mc.gameRenderer.currentEffect())).test_master$getPasses().get(0);
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.error("Failed to load night vision shader : ", e);
        }
    }

    private static void updateShaderUniforms(float nightVisionEnabled, GenericNVGGogglesItem.NVGConfig config, boolean isUltraGamer) {
        Minecraft mc = Minecraft.getInstance();
        try {
            int width = mc.getWindow().getWidth();
            int height = mc.getWindow().getHeight();
            if ((width != lastWidth || height != lastHeight) && mc.gameRenderer.currentEffect() != null) {
                lastWidth = width;
                lastHeight = height;
                Objects.requireNonNull(mc.gameRenderer.currentEffect()).resize(lastWidth, lastHeight);
            }
            if (passe != null) {
                if (passe.getEffect().getUniform("NightVisionEnabled") != null) {
                    if (nightVisionEnabled > 0.0f) {
                        if (!isUltraGamer) {
                            passe.getEffect().safeGetUniform("NightVisionEnabled").set(nightVisionEnabled);
                            passe.getEffect().safeGetUniform("Brightness").set(config.getBrightness());
                            passe.getEffect().safeGetUniform("RedValue").set(config.getRedValue());
                            passe.getEffect().safeGetUniform("BlueValue").set(config.getBlueValue());
                            passe.getEffect().safeGetUniform("GreenValue").set(config.getGreenValue());
                        } else {
                            passe.getEffect().safeGetUniform("NightVisionEnabled").set(nightVisionEnabled);
                            passe.getEffect().safeGetUniform("Brightness").set(config.getBrightness());
                            passe.getEffect().safeGetUniform("RedValue").set(NVGConfigs.getUltraGamerRedValue());
                            passe.getEffect().safeGetUniform("BlueValue").set(NVGConfigs.getUltraGamerBlueValue());
                            passe.getEffect().safeGetUniform("GreenValue").set(NVGConfigs.getUltraGamerGreenValue());
                        }
                    } else {
                        passe.getEffect().safeGetUniform("NightVisionEnabled").set(nightVisionEnabled);
                        passe.getEffect().safeGetUniform("Brightness").set(1.0f);
                    }
                }
            }
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.warn("Failed to update night vision shader uniforms : ", e);
        }
    }
}