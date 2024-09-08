package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.MilitaryGlassesItem;

public class MilitaryGlassesModel extends GeoModel<MilitaryGlassesItem> {
	@Override
	public ResourceLocation getAnimationResource(MilitaryGlassesItem object) {
		return new ResourceLocation("mm", "animations/black_glasses.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MilitaryGlassesItem object) {
		return new ResourceLocation("mm", "geo/black_glasses.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MilitaryGlassesItem object) {
		return new ResourceLocation("mm", "textures/item/black_glasses.png");
	}
}
