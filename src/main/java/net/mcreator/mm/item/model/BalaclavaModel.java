package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BalaclavaItem;

public class BalaclavaModel extends GeoModel<BalaclavaItem> {
	@Override
	public ResourceLocation getAnimationResource(BalaclavaItem object) {
		return new ResourceLocation("mm", "animations/balaclava.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BalaclavaItem object) {
		return new ResourceLocation("mm", "geo/balaclava.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BalaclavaItem object) {
		return new ResourceLocation("mm", "textures/item/other_black.png");
	}
}
