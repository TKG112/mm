package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackNVG21Item;

public class BlackNVG21Model extends GeoModel<BlackNVG21Item> {
	@Override
	public ResourceLocation getAnimationResource(BlackNVG21Item object) {
		return new ResourceLocation("mm", "animations/black_nvg21.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackNVG21Item object) {
		return new ResourceLocation("mm", "geo/black_nvg21.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackNVG21Item object) {
		return new ResourceLocation("mm", "textures/item/black_nvg21.png");
	}
}
