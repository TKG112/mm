package net.mcreator.mm.procedures;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

import net.mcreator.mm.init.MmModItems;

public class GPNVGOverlayDisplayOverlayIngameProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		boolean HasNVG = false;
		if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_GPNVG.get(), lv).isPresent() : false) {
			if (Minecraft.getInstance().options.getCameraType().isFirstPerson() == true) {
				if (entity instanceof LivingEntity lv) {
					CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_GPNVG.get()).forEach(item -> {
						ItemStack itemstackiterator = item.stack();
						if (itemstackiterator.getOrCreateTag().getBoolean("NvgCheck") == true) {
							HasNVG = true;
						}
					});
				}
			}
		}
		if (HasNVG == true) {
			return true;
		}
		return false;
	}
}
