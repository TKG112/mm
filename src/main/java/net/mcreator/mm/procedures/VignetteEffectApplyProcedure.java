package net.mcreator.mm.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.mm.init.MmModMobEffects;

public class VignetteEffectApplyProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(MmModMobEffects.VIGNETTE.get(), 60, 0, false, false));
	}
}
