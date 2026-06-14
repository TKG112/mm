package net.tkg.ModernMayhem.client.thermal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.tkg.ModernMayhem.client.thermal.render.ThermalRenderer;
import net.tkg.ModernMayhem.client.shaderController.NVGShaderController;
import net.tkg.ModernMayhem.client.shaderController.TVGShaderController;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;

/**
 * Example usage of the ThermalRenderer system
 */
public class Thermal {

    /**
     * Initialize outline rendering with custom settings
     * Call this during your mod initialization
     */
    public static void setupThermal() {

        registerFacewearModeListener();
        ThermalRenderer.setUseBlackOutline(false);
        ThermalRenderer.setOutlinePredicate(entity -> {
            if (!(entity instanceof LivingEntity)) return false;

            if (entity instanceof ItemEntity) return false;

            if (entity instanceof ThrowableProjectile) return false;

            if (entity instanceof HangingEntity) return false;

            if (entity instanceof ArmorStand) return false;

            return true;
        });
    }

    public static void registerHelmetModeListener() {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase != TickEvent.Phase.END) return;

            updateModeFromHelmet();
        });
    }

    public static void updateModeFromHelmet() {
        if (Minecraft.getInstance().player == null) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OFF);
            return;
        }

        ItemStack head = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD);

        ThermalRenderer.setOutlineColorProvider(null);

        if (head.isEmpty()) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OFF);
        }

        else if (head.getItem() == Items.IRON_HELMET) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OUTLINE);
            ThermalRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 2.0f);
        }

        else if (head.getItem() == Items.DIAMOND_HELMET) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OVERLAY);
            ThermalRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 2.0f);
        }

        else if (head.getItem() == Items.GOLDEN_HELMET) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OUTLINE);
            float hue = (System.currentTimeMillis() % 2000L) / 2000.0f;
            int rgb = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
            float r = ((rgb >> 16) & 0xFF) / 255.0f;
            float g = ((rgb >> 8) & 0xFF) / 255.0f;
            float b = (rgb & 0xFF) / 255.0f;
            ThermalRenderer.setOutlineColor(r, g, b, 2.0f);
        }

        else if (head.getItem() == Items.NETHERITE_HELMET) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OUTLINE);
            ThermalRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 2.0f);
            ThermalRenderer.setOutlineColorProvider(entity -> {
                if (entity instanceof LivingEntity living) {
                    float health = living.getHealth();
                    float max = living.getMaxHealth();
                    float pct = Math.max(0.0f, Math.min(1.0f, health / max));
                    return java.awt.Color.HSBtoRGB(pct * 0.33f, 1.0f, 1.0f);
                }
                return 0xFFFFFFFF;
            });
        }

        else {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OFF);
        }
    }

    public static void registerFacewearModeListener() {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase != TickEvent.Phase.END) return;

            updateModeFromFacewear();
        });
    }

    public static void updateModeFromFacewear() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OFF);
            return;
        }

        ThermalRenderer.setOutlineColorProvider(null);

        if (TVGShaderController.isEnabled() && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OVERLAY);
            ThermalRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 2.0f);
        }

        else if (NVGShaderController.isEnabled() && Minecraft.getInstance().options.getCameraType().isFirstPerson() && isCotiEnabledOnPlayer(player)) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OUTLINE);
            ThermalRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 2.0f);
        }

        else {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OFF);
        }
    }

    public static void registerCombinedModeListener() {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase != TickEvent.Phase.END) return;

            updateModeCombined();
        });
    }

    public static void updateModeCombined() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OFF);
            return;
        }

        ThermalRenderer.setOutlineColorProvider(null);

        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);

        if (head.getItem() == Items.IRON_HELMET) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OUTLINE);
            ThermalRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 1.0f);
            return;
        }

        if (head.getItem() == Items.DIAMOND_HELMET) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OVERLAY);
            ThermalRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 1.0f);
            return;
        }

        if (head.getItem() == Items.GOLDEN_HELMET) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OUTLINE);
            float hue = (System.currentTimeMillis() % 2000L) / 2000.0f;
            int rgb = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
            float r = ((rgb >> 16) & 0xFF) / 255.0f;
            float g = ((rgb >> 8) & 0xFF) / 255.0f;
            float b = (rgb & 0xFF) / 255.0f;
            ThermalRenderer.setOutlineColor(r, g, b, 1.0f);
            return;
        }

        if (head.getItem() == Items.NETHERITE_HELMET) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OUTLINE);
            ThermalRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 1.0f);
            ThermalRenderer.setOutlineColorProvider(entity -> {
                if (entity instanceof LivingEntity living) {
                    float health = living.getHealth();
                    float max = living.getMaxHealth();
                    float pct = Math.max(0.0f, Math.min(1.0f, health / max));
                    return java.awt.Color.HSBtoRGB(pct * 0.33f, 1.0f, 1.0f);
                }
                return 0xFFFFFFFF;
            });
            return;
        }

        boolean firstPerson = Minecraft.getInstance().options.getCameraType().isFirstPerson();

        if (TVGShaderController.isEnabled() && firstPerson) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OVERLAY);
            ThermalRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 1.0f);
            return;
        }

        if (NVGShaderController.isEnabled() && firstPerson && isCotiEnabledOnPlayer(player)) {
            ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OUTLINE);
            ThermalRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 1.0f);
            return;
        }

        ThermalRenderer.setRenderMode(ThermalRenderer.RenderMode.OFF);
    }

    private static boolean isCotiEnabledOnPlayer(LocalPlayer player) {
        ItemStack stack = CuriosUtil.getFaceWearItem(player);
        if (stack == null) return false;
        return stack.getItem() instanceof NVGGogglesItem && GenericSpecialGogglesItem.isCotiEnabled(stack);
    }
}