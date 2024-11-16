package net.mcreator.mm.procedures;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

import net.mcreator.mm.network.MmModVariables;
import net.mcreator.mm.init.MmModItems;

public class GPNVGOverlayDisplayOverlayIngameProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_GPNVG.get(), lv).isPresent() : false) {
			if ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).Black_GPNVG_Check == true) {
				if (Minecraft.getInstance().options.getCameraType().isFirstPerson() == true) {
					return true;
				}
			}
		}
		return false;
	}
}
