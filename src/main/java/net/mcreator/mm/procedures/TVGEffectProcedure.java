package net.mcreator.mm.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.mm.network.MmModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TVGEffectProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).TVG_Shader == true) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false));
		} else {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.NIGHT_VISION);
		}
	}
}
