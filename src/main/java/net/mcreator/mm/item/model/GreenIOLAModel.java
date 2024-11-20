package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GreenIOLAItem;

public class GreenIOLAModel extends GeoModel<GreenIOLAItem> {
	@Override
	public ResourceLocation getAnimationResource(GreenIOLAItem object) {
		return new ResourceLocation("mm", "animations/green_iola.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GreenIOLAItem object) {
		return new ResourceLocation("mm", "geo/green_iola.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GreenIOLAItem object) {
		return new ResourceLocation("mm", "textures/item/green_iola.png");
	}
}
