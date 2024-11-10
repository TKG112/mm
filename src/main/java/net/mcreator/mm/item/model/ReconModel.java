package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.ReconItem;

public class ReconModel extends GeoModel<ReconItem> {
	@Override
	public ResourceLocation getAnimationResource(ReconItem object) {
		return new ResourceLocation("mm", "animations/recon.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(ReconItem object) {
		return new ResourceLocation("mm", "geo/recon.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ReconItem object) {
		return new ResourceLocation("mm", "textures/item/recon.png");
	}
}
