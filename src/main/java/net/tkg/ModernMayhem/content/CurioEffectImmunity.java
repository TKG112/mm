package net.tkg.ModernMayhem.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.tkg.ModernMayhem.content.item.DataCurioItem;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Makes the wearer immune to the mob effects a curio filters out -- a gas mask against gas.
 * <p>
 * Which effects are blocked comes from an <b>item tag of mob effects</b> named by the curio's
 * definition, rather than a list inside the definition itself, so anyone can extend it with a
 * datapack: a mod adding its own poison can make ModernMayhem's gas mask protect against it without
 * either side knowing about the other. Same reasoning as the COTI and helmet-conflict tags.
 */
public final class CurioEffectImmunity {

    /** Checked periodically rather than every tick; immunity is not time-critical. */
    private static final int SWEEP_INTERVAL = 20;

    private CurioEffectImmunity() {}

    /** Blocks an effect before it is ever applied. */
    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (isImmune(event.getEntity(), event.getEffectInstance().getEffect())) {
            event.setResult(Event.Result.DENY);
        }
    }

    /**
     * Clears effects the wearer has become immune to.
     * <p>
     * {@link #onEffectApplicable} only guards the moment of application, so without this an effect
     * caught before the mask went on would keep running underneath it.
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide()) return;
        if (event.player.tickCount % SWEEP_INTERVAL != 0) return;

        List<MobEffect> toRemove = null;
        for (MobEffectInstance active : event.player.getActiveEffects()) {
            if (isImmune(event.player, active.getEffect())) {
                if (toRemove == null) toRemove = new ArrayList<>();
                toRemove.add(active.getEffect());
            }
        }
        if (toRemove == null) return;

        for (MobEffect effect : toRemove) {
            event.player.removeEffect(effect);
        }
    }

    private static boolean isImmune(LivingEntity entity, MobEffect effect) {
        ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        if (effectId == null) return false;

        return CuriosApi.getCuriosInventory(entity).map(inventory -> {
            for (var handler : inventory.getCurios().values()) {
                var stacks = handler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    if (blocks(stacks.getStackInSlot(i), effect)) return true;
                }
            }
            return false;
        }).orElse(false);
    }

    private static boolean blocks(ItemStack stack, MobEffect effect) {
        if (!(stack.getItem() instanceof DataCurioItem curio)) return false;
        ResourceLocation tagId = curio.definition().immuneToEffects();
        if (tagId == null) return false;

        return ForgeRegistries.MOB_EFFECTS.tags() != null
                && ForgeRegistries.MOB_EFFECTS.tags()
                        .getTag(TagKey.create(Registries.MOB_EFFECT, tagId))
                        .contains(effect);
    }
}
