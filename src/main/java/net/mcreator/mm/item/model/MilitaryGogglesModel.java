package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.MilitaryGogglesItem;

public class MilitaryGogglesModel extends GeoModel<MilitaryGogglesItem> {
	@Override
	public ResourceLocation getAnimationResource(MilitaryGogglesItem object) {
		return new ResourceLocation("mm", "animations/military_goggles.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MilitaryGogglesItem object) {
		return new ResourceLocation("mm", "geo/military_goggles.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MilitaryGogglesItem object) {
		return new ResourceLocation("mm", "textures/item/black_goggles.png");
	}
}
