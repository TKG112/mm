package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackBackpack1Item;

public class BlackBackpackT1Model extends GeoModel<BlackBackpack1Item> {
	@Override
	public ResourceLocation getAnimationResource(BlackBackpack1Item object) {
		return new ResourceLocation("mm", "animations/black_backpack_t1.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackBackpack1Item object) {
		return new ResourceLocation("mm", "geo/black_backpack_t1.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackBackpack1Item object) {
		return new ResourceLocation("mm", "textures/item/black_backpack.png");
	}
}
