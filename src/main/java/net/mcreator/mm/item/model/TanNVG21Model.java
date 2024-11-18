package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.TanNVG21Item;

public class TanNVG21Model extends GeoModel<TanNVG21Item> {
	@Override
	public ResourceLocation getAnimationResource(TanNVG21Item object) {
		return new ResourceLocation("mm", "animations/tan_nvg21.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TanNVG21Item object) {
		return new ResourceLocation("mm", "geo/tan_nvg21.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TanNVG21Item object) {
		return new ResourceLocation("mm", "textures/item/tan_nvg21.png");
	}
}
