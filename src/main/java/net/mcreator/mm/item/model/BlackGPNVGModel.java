package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackGPNVGItem;

public class BlackGPNVGModel extends GeoModel<BlackGPNVGItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackGPNVGItem object) {
		return new ResourceLocation("mm", "animations/black_gpnvgs.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackGPNVGItem object) {
		return new ResourceLocation("mm", "geo/black_gpnvgs.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackGPNVGItem object) {
		return new ResourceLocation("mm", "textures/item/black_gpnvgs.png");
	}
}
