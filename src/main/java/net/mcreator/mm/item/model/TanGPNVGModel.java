package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.TanGPNVGItem;

public class TanGPNVGModel extends GeoModel<TanGPNVGItem> {
	@Override
	public ResourceLocation getAnimationResource(TanGPNVGItem object) {
		return new ResourceLocation("mm", "animations/tan_gpnvgs.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TanGPNVGItem object) {
		return new ResourceLocation("mm", "geo/tan_gpnvgs.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TanGPNVGItem object) {
		return new ResourceLocation("mm", "textures/item/tan_gpnvgs.png");
	}
}
