package net.tkg.ModernMayhem.content;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.tkg.ModernMayhem.content.def.WornEffect;
import net.tkg.ModernMayhem.content.item.DataArmorItem;

/**
 * Applies the mob effects an armor piece grants while it is worn.
 * <p>
 * Effects are refreshed every tick with a short duration rather than applied once, so they simply
 * lapse a moment after the piece comes off -- no bookkeeping of what was granted, and nothing left
 * behind if the player dies, changes dimension or is stripped of the armor by another mod.
 */
public final class WornArmorEffects {

    /**
     * Long enough that a tick of lag can't make the effect flicker, short enough that it is gone
     * almost immediately once the armor is removed.
     */
    private static final int DURATION_TICKS = 40;

    /** Re-applying every tick would spam; a quarter of the duration keeps it comfortably topped up. */
    private static final int REFRESH_INTERVAL = 10;

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private WornArmorEffects() {}

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide()) return;
        if (player.tickCount % REFRESH_INTERVAL != 0) return;

        String fullSetMaterial = fullSetMaterial(player);

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            if (!(player.getItemBySlot(slot).getItem() instanceof DataArmorItem armor)) continue;

            for (WornEffect worn : armor.definition().effects()) {
                if (worn.requiresFullSet() && !armor.definition().material().equals(fullSetMaterial)) {
                    continue;
                }
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(worn.effect());
                // Belongs to a mod that isn't installed -- skip rather than break the item.
                if (effect == null) continue;

                player.addEffect(new MobEffectInstance(effect, DURATION_TICKS, worn.amplifier(),
                        true, worn.showParticles(), worn.showIcon()));
            }
        }
    }

    /**
     * The shared {@code material} of the player's armor when all four slots hold data-driven pieces of
     * the same one, else null.
     * <p>
     * Using {@code material} as the set identity means a set is declared the same way its pieces are
     * already grouped, rather than by listing item ids somewhere separate that can drift out of sync.
     */
    private static String fullSetMaterial(Player player) {
        String material = null;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!(stack.getItem() instanceof DataArmorItem armor)) return null;

            if (material == null) {
                material = armor.definition().material();
            } else if (!material.equals(armor.definition().material())) {
                return null;
            }
        }
        return material;
    }
}
