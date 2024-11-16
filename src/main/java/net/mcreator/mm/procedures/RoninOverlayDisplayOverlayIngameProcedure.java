package net.mcreator.mm.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

import net.mcreator.mm.init.MmModItems;

public class RoninOverlayDisplayOverlayIngameProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MmModItems.RONIN_HELMET.get()) {
			if (Minecraft.getInstance().options.getCameraType().isFirstPerson() == true) {
				return true;
			}
		}
		return false;
	}
}
