package net.mcreator.mm.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.mm.init.MmModMobEffects;

public class MedkitRightclickedProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (!(entity instanceof Player _plrCldCheck1 && _plrCldCheck1.getCooldowns().isOnCooldown(itemstack.getItem()))) {
			if (entity instanceof LivingEntity _entity)
				_entity.setHealth((float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + 8));
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MmModMobEffects.BLEEDING.get());
			if (entity instanceof Player _player)
				_player.getCooldowns().addCooldown(itemstack.getItem(), 100);
			itemstack.shrink(1);
		}
	}
}
