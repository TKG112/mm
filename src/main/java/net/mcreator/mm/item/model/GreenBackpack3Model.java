package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GreenBackpack3Item;

public class GreenBackpack3Model extends GeoModel<GreenBackpack3Item> {
	@Override
	public ResourceLocation getAnimationResource(GreenBackpack3Item object) {
		return new ResourceLocation("mm", "animations/green_backpack_t3.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GreenBackpack3Item object) {
		return new ResourceLocation("mm", "geo/green_backpack_t3.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GreenBackpack3Item object) {
		return new ResourceLocation("mm", "textures/item/green_backpack.png");
	}
}
