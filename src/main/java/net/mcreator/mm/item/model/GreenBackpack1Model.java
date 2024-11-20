package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GreenBackpack1Item;

public class GreenBackpack1Model extends GeoModel<GreenBackpack1Item> {
	@Override
	public ResourceLocation getAnimationResource(GreenBackpack1Item object) {
		return new ResourceLocation("mm", "animations/green_backpack_t1.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GreenBackpack1Item object) {
		return new ResourceLocation("mm", "geo/green_backpack_t1.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GreenBackpack1Item object) {
		return new ResourceLocation("mm", "textures/item/green_backpack.png");
	}
}
