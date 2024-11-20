package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GreenKevlarPantsItem;

public class GreenKevlarPantsModel extends GeoModel<GreenKevlarPantsItem> {
	@Override
	public ResourceLocation getAnimationResource(GreenKevlarPantsItem object) {
		return new ResourceLocation("mm", "animations/green_kevlar_clothing.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GreenKevlarPantsItem object) {
		return new ResourceLocation("mm", "geo/green_kevlar_clothing.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GreenKevlarPantsItem object) {
		return new ResourceLocation("mm", "textures/item/green_kevlar_clothing.png");
	}
}
