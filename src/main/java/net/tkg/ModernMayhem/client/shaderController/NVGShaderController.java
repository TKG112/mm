package net.tkg.ModernMayhem.client.shaderController;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.NVGConfigs;

@OnlyIn(Dist.CLIENT)
public final class NVGShaderController {

    private static final Minecraft MC = Minecraft.getInstance();

    private static boolean enabled = false;

    // Auto gain uniforms
    private static float AutoGainEnabled = 0f;
    private static float MinGain = 0f;
    private static float MaxGain = 0f;
    private static float AutoGainSpeed = 0f;

    // Auto gating uniforms
    private static float AutoGatingEnabled = 0f;
    private static float AutoGatingOffset = 0f;
    private static float AutoGatingSpeed = 0f;

    // Night vision color uniforms
    private static float Brightness = 0f;
    private static float RedValue = 0f;
    private static float GreenValue = 0f;
    private static float BlueValue = 0f;
    private static float NoiseMultiplier = 0f;

    public static boolean isEnabled() { return enabled; }

    public static float getAutoGainEnabled() { return AutoGainEnabled; }
    public static float getMinGain() { return MinGain; }
    public static float getMaxGain() { return MaxGain; }
    public static float getAutoGainSpeed() { return AutoGainSpeed; }

    public static float getAutoGatingEnabled() { return AutoGatingEnabled; }
    public static float getAutoGatingOffset() { return AutoGatingOffset; }
    public static float getAutoGatingSpeed() { return AutoGatingSpeed; }

    public static float getBrightness() { return Brightness; }
    public static float getRedValue() { return RedValue; }
    public static float getGreenValue() { return GreenValue; }
    public static float getBlueValue() { return BlueValue; }
    public static float getNoiseMultiplier() { return NoiseMultiplier; }

    public static void recomputeUniforms() {
        LocalPlayer player = MC.player;
        if (player == null) return;

        boolean shouldRender = false;
        GenericSpecialGogglesItem.NVGConfig nvgItemConfig = null;
        ItemStack facewearItem = null;

        if (CuriosUtil.hasNVGEquipped(player)) {
            facewearItem = CuriosUtil.getFaceWearItem(player);
            if (facewearItem != null && facewearItem.getItem() instanceof GenericSpecialGogglesItem nvgGogglesItem
                    && nvgGogglesItem.getGoggleType() == GenericSpecialGogglesItem.GoggleType.NIGHT_VISION) {
                if (nvgGogglesItem.shouldRenderShader()) {
                    shouldRender = GenericSpecialGogglesItem.getNVGCheck(facewearItem);
                    try {
                        nvgItemConfig = GenericSpecialGogglesItem.getCurrentConfig(facewearItem);
                    } catch (Exception e) {
                        ModernMayhemMod.LOGGER.error("Error getting NVG config", e);
                        return;
                    }
                }
            }
        }

        enabled = shouldRender;

        if (isEnabled() && nvgItemConfig != null && facewearItem != null) {
            boolean isUltraGamer = facewearItem.getItem() instanceof GenericSpecialGogglesItem g && g.hasRainbowPhosphor();

            boolean autoGainActive = false;
            boolean autoGatingActive = false;

            if (facewearItem.getItem() instanceof GenericSpecialGogglesItem nvgItem) {
                autoGainActive = nvgItem.hasAutoGain() && GenericSpecialGogglesItem.isAutoGainEnabled(facewearItem);

                if (!autoGainActive && nvgItem.hasAutoGating()) {
                    autoGatingActive = true;
                }
            }

            AutoGainEnabled = autoGainActive ? 1.0f : 0.0f;
            MinGain = nvgItemConfig.getMinGain();
            MaxGain = nvgItemConfig.getMaxGain();
            AutoGainSpeed = nvgItemConfig.getAutoGainSpeed();

            AutoGatingEnabled = autoGatingActive ? 1.0f : 0.0f;
            AutoGatingOffset = nvgItemConfig.getAutoGatingOffset();
            AutoGatingSpeed = nvgItemConfig.getAutoGatingSpeed();

            Brightness = nvgItemConfig.getBrightness();
            if (isUltraGamer) {
                RedValue = NVGConfigs.getUltraGamerRedValue();
                GreenValue = NVGConfigs.getUltraGamerGreenValue();
                BlueValue = NVGConfigs.getUltraGamerBlueValue();
            } else {
                RedValue = nvgItemConfig.getRedValue();
                GreenValue = nvgItemConfig.getGreenValue();
                BlueValue = nvgItemConfig.getBlueValue();
            }
            NoiseMultiplier = nvgItemConfig.getNoiseMultiplier();
        }
    }
}
