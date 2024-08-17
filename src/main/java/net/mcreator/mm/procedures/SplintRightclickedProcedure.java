package net.mcreator.mm.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.mm.init.MmModMobEffects;

public class SplintRightclickedProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if ((entity instanceof Player _plrCldCheck1 && _plrCldCheck1.getCooldowns().isOnCooldown(itemstack.getItem())) == false) {
			if (entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(MmModMobEffects.BROKEN_LEG.get())) {
				if (entity instanceof LivingEntity _entity)
					_entity.removeEffect(MmModMobEffects.BROKEN_LEG.get());
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(itemstack.getItem(), 100);
				itemstack.shrink(1);
			}
		}
	}
}
