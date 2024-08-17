package net.mcreator.mm.procedures;

import software.bernie.geckolib.animatable.GeoItem;

import net.minecraft.world.item.ItemStack;

import net.mcreator.mm.item.BlackMilitaryHelmetItem;

public class NVGWhileBaubleIsEquippedTickProcedure {
	public static void execute(ItemStack itemstack) {
		if (itemstack.getItem() instanceof BlackMilitaryHelmetItem armor && armor instanceof GeoItem)
			itemstack.getOrCreateTag().putString("geckoAnim", "animation");
	}
}
