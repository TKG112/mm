package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.NVGItem;

public class NVGModel extends GeoModel<NVGItem> {
	@Override
	public ResourceLocation getAnimationResource(NVGItem object) {
		return new ResourceLocation("mm", "animations/nvg.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(NVGItem object) {
		return new ResourceLocation("mm", "geo/nvg.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(NVGItem object) {
		return new ResourceLocation("mm", "textures/item/eyewear_black.png");
	}
}
