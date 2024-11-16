package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BandoleerItem;

public class BandoleerModel extends GeoModel<BandoleerItem> {
	@Override
	public ResourceLocation getAnimationResource(BandoleerItem object) {
		return new ResourceLocation("mm", "animations/tan_bandoleer.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BandoleerItem object) {
		return new ResourceLocation("mm", "geo/tan_bandoleer.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BandoleerItem object) {
		return new ResourceLocation("mm", "textures/item/tan_bandoleer.png");
	}
}
