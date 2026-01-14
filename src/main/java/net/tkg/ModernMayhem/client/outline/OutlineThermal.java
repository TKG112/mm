package net.tkg.ModernMayhem.client.outline;

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
import net.tkg.ModernMayhem.client.event.RenderNVGShader;
import net.tkg.ModernMayhem.client.event.RenderTVGShader;
import net.tkg.ModernMayhem.client.outline.render.OutlineRenderer;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;

/**
 * Example usage of the OutlineRenderer system
 */
public class OutlineThermal {

    /**
     * Initialize outline rendering with custom settings
     * Call this during your mod initialization
     */
    public static void setupOutlines() {

        registerFacewearModeListener();
        OutlineRenderer.setUseBlackOutline(false);
        OutlineRenderer.setOutlinePredicate(entity -> {
            if (!(entity instanceof LivingEntity)) return false;

            if (entity instanceof ItemEntity) return false;

            if (entity instanceof ThrowableProjectile) return false;

            if (entity instanceof HangingEntity) return false;

            if (entity instanceof ArmorStand) return false;

            // You can add more exclusions here if you notice more edge-cases:
            // if (entity instanceof SomeOtherType) return false;

            // Otherwise allow
            return true;
        });

        // Example 1: Outline all hostile mobs in red
        // OutlineRenderer.setOutlinePredicate(entity -> entity instanceof Monster);
        // OutlineRenderer.setOutlineColor(1.0f, 0.0f, 0.0f, 0.8f); // Red with 80% opacity

        // Example 2: Outline all players in blue
        // OutlineRenderer.setOutlinePredicate(entity -> entity instanceof Player);
        // OutlineRenderer.setOutlineColor(0.0f, 0.5f, 1.0f, 1.0f); // Blue

        // Example 3: Outline all animals in green
        // OutlineRenderer.setOutlinePredicate(entity -> entity instanceof Animal);
        // OutlineRenderer.setOutlineColor(0.0f, 1.0f, 0.0f, 0.7f); // Green with 70% opacity

        // Example 4: Custom predicate - outline entities within 10 blocks
        // OutlineRenderer.setOutlinePredicate(entity -> {
        //     Player player = Minecraft.getInstance().player;
        //     if (player == null) return false;
        //     return entity.distanceTo(player) < 10.0;
        // });
        // OutlineRenderer.setOutlineColor(1.0f, 1.0f, 0.0f, 1.0f); // Yellow

        // Example 5: Outline specific entity by UUID or name
        // UUID targetUUID = ...; // some UUID
        // OutlineRenderer.setOutlinePredicate(entity ->
        //     entity.getUUID().equals(targetUUID)
        // );
        // OutlineRenderer.setOutlineColor(1.0f, 0.0f, 1.0f, 1.0f); // Magenta

        // Example 6: Combine multiple conditions
        // OutlineRenderer.setOutlinePredicate(entity -> {
        //     if (entity instanceof Monster) return true;
        //     if (entity instanceof Player && !entity.equals(Minecraft.getInstance().player)) return true;
        //     return false;
        // });
        // OutlineRenderer.setOutlineColor(1.0f, 0.5f, 0.0f, 0.9f); // Orange
    }

    public static void registerHelmetModeListener() {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase != TickEvent.Phase.END) return;

            updateModeFromHelmet();
        });
    }

    public static void updateModeFromHelmet() {
        if (Minecraft.getInstance().player == null) {
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OFF);
            return;
        }

        ItemStack head = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD);

        OutlineRenderer.setOutlineColorProvider(null);

        if (head.isEmpty()) {
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OFF);
        }

        else if (head.getItem() == Items.IRON_HELMET) {
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OUTLINE);
            OutlineRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 2.0f);
        }

        else if (head.getItem() == Items.DIAMOND_HELMET) {
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OVERLAY);
            OutlineRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 2.0f);
        }

        else if (head.getItem() == Items.GOLDEN_HELMET) {
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OUTLINE);
            float hue = (System.currentTimeMillis() % 2000L) / 2000.0f;
            int rgb = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
            float r = ((rgb >> 16) & 0xFF) / 255.0f;
            float g = ((rgb >> 8) & 0xFF) / 255.0f;
            float b = (rgb & 0xFF) / 255.0f;
            OutlineRenderer.setOutlineColor(r, g, b, 2.0f);
        }

        else if (head.getItem() == Items.NETHERITE_HELMET) {
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OUTLINE);
            OutlineRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 2.0f);
            OutlineRenderer.setOutlineColorProvider(entity -> {
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
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OFF);
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
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OFF);
            return;
        }

        OutlineRenderer.setOutlineColorProvider(null);

        if (RenderTVGShader.isThermalActive() && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OVERLAY);
            OutlineRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 2.0f);
        }

        else if (RenderNVGShader.isNvActive() && Minecraft.getInstance().options.getCameraType().isFirstPerson() && isCotiEnabledOnPlayer(player)) {
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OUTLINE);
            OutlineRenderer.setOutlineColor(1.0f, 1.0f, 1.0f, 2.0f);
        }

        else {
            OutlineRenderer.setRenderMode(OutlineRenderer.RenderMode.OFF);
        }
    }

    private static boolean isCotiEnabledOnPlayer(LocalPlayer player) {
        ItemStack stack = CuriosUtil.getFaceWearItem(player);
        return stack.getItem() instanceof NVGGogglesItem && GenericNVGGogglesItem.isCotiEnabled(stack);
    }
}