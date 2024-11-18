package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GreenNVG21Item;

public class GreenNVG21Model extends GeoModel<GreenNVG21Item> {
	@Override
	public ResourceLocation getAnimationResource(GreenNVG21Item object) {
		return new ResourceLocation("mm", "animations/green_nvg21.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GreenNVG21Item object) {
		return new ResourceLocation("mm", "geo/green_nvg21.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GreenNVG21Item object) {
		return new ResourceLocation("mm", "textures/item/green_nvg21.png");
	}
}
