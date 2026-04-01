package net.tkg.ModernMayhem.client.shaderController;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.server.item.curios.facewear.TVGGogglesItem;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;

import java.util.function.Function;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public final class ThermalHighlightShaderController {

    public enum ThermalHighlightMode {
        OFF,
        OUTLINE,
        FILL,
        FILL_AND_OUTLINE
    }

    public enum ThermalHighlightStyle {
        WHITE_HOT,
        BLACK_HOT,
        CUSTOM_COLOR,
        HEALTH_BASED
    }

    private static final Minecraft MC = Minecraft.getInstance();

    private static boolean enabled = false;
    private static ThermalHighlightMode mode = ThermalHighlightMode.OFF;
    private static ThermalHighlightStyle style = ThermalHighlightStyle.WHITE_HOT;

    private static float colorR = 1.0f;
    private static float colorG = 1.0f;
    private static float colorB = 1.0f;
    private static float colorA = 1.0f;

    private static float outlineAlpha = 1.0f;
    private static float fillAlpha = 0.45f;
    private static float glowStrength = 1.0f;
    private static float blurRadius = 2.0f;

    private static Predicate<Entity> predicate = entity -> {
        if (!(entity instanceof LivingEntity)) return false;
        if (entity instanceof ItemEntity) return false;
        if (entity instanceof ThrowableProjectile) return false;
        if (entity instanceof HangingEntity) return false;
        if (entity instanceof ArmorStand) return false;
        return true;
    };

    private static Function<Entity, Integer> colorProvider = null;

    private ThermalHighlightShaderController() {}

    public static boolean isEnabled() { return enabled; }
    public static ThermalHighlightMode getMode() { return mode; }
    public static ThermalHighlightStyle getStyle() { return style; }

    public static float getColorR() { return colorR; }
    public static float getColorG() { return colorG; }
    public static float getColorB() { return colorB; }
    public static float getColorA() { return colorA; }

    public static float getOutlineAlpha() { return outlineAlpha; }
    public static float getFillAlpha() { return fillAlpha; }
    public static float getGlowStrength() { return glowStrength; }
    public static float getBlurRadius() { return blurRadius; }

    public static Predicate<Entity> getPredicate() { return predicate; }
    public static Function<Entity, Integer> getColorProvider() { return colorProvider; }

    public static void recompute() {
        LocalPlayer player = MC.player;
        if (player == null || MC.level == null) {
            disable();
            return;
        }

        enabled = false;
        mode = ThermalHighlightMode.OFF;
        style = ThermalHighlightStyle.WHITE_HOT;
        colorProvider = null;

        if (!MC.options.getCameraType().isFirstPerson()) {
            return;
        }

//        ItemStack facewear = CuriosUtil.getFaceWearItem(player);
//        if (facewear == null) {
//            return;
//        }
//
//        if (!(facewear.getItem() instanceof TVGGogglesItem thermalItem)) {
//            return;
//        }
//
//        if (!thermalItem.shouldRenderShader()) {
//            return;
//        }
//
//        if (GenericSpecialGogglesItem.getNVGMode(facewear) != 1) {
//            return;
//        }

        enabled = true;

        mode = ThermalHighlightMode.FILL_AND_OUTLINE;
        style = ThermalHighlightStyle.WHITE_HOT;

        colorR = 1.0f;
        colorG = 1.0f;
        colorB = 1.0f;
        colorA = 1.0f;

        fillAlpha = 0.40f;
        outlineAlpha = 1.0f;
        glowStrength = 1.0f;
        blurRadius = 2.0f;
    }

    public static void setPredicate(Predicate<Entity> newPredicate) {
        predicate = newPredicate;
    }

    public static void setStaticColor(float r, float g, float b, float a) {
        colorR = r;
        colorG = g;
        colorB = b;
        colorA = a;
        colorProvider = null;
        style = ThermalHighlightStyle.CUSTOM_COLOR;
    }

    public static void setColorProvider(Function<Entity, Integer> provider) {
        colorProvider = provider;
    }

    public static void setMode(ThermalHighlightMode newMode) {
        mode = newMode;
    }

    public static void setStyle(ThermalHighlightStyle newStyle) {
        style = newStyle;
    }

    private static void disable() {
        enabled = false;
        mode = ThermalHighlightMode.OFF;
        colorProvider = null;
    }
}