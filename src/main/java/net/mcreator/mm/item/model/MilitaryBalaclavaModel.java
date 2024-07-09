package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.MilitaryBalaclavaItem;

public class MilitaryBalaclavaModel extends GeoModel<MilitaryBalaclavaItem> {
	@Override
	public ResourceLocation getAnimationResource(MilitaryBalaclavaItem object) {
		return new ResourceLocation("mm", "animations/balaclava_military.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MilitaryBalaclavaItem object) {
		return new ResourceLocation("mm", "geo/balaclava_military.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MilitaryBalaclavaItem object) {
		return new ResourceLocation("mm", "textures/item/other_black.png");
	}
}
