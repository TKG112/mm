package net.mcreator.mm.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.mm.init.MmModMobEffects;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class ApplyBleedingEffectProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getEntity(), event.getAmount());
        }
    }

    public static void execute(Entity entity, double amount) {
        execute(null, entity, amount);
    }

    private static void execute(@Nullable Event event, Entity entity, double amount) {
        if (entity == null)
            return;
        if (entity instanceof Player) {
            if (amount >= 4) {
                // Calculate chance based on damage amount
                double chance = Math.min((amount - 4) / 16.0, 1.0); // Scales from 0 to 1

                if (Math.random() < chance) {
                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                        _entity.addEffect(new MobEffectInstance(MmModMobEffects.BLEEDING.get(), -1, 0, false, false));
                }
            }
        }
    }
}
