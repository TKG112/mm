package net.mcreator.mm.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.mm.init.MmModMobEffects;

public class FlashbangOverlayReturnProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MmModMobEffects.FLASH.get())) {
			return true;
		}
		return false;
	}
}
