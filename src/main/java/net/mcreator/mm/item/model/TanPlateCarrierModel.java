package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.TanPlateCarrierItem;

public class TanPlateCarrierModel extends GeoModel<TanPlateCarrierItem> {
	@Override
	public ResourceLocation getAnimationResource(TanPlateCarrierItem object) {
		return new ResourceLocation("mm", "animations/black_plate_carrier.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TanPlateCarrierItem object) {
		return new ResourceLocation("mm", "geo/black_plate_carrier.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TanPlateCarrierItem object) {
		return new ResourceLocation("mm", "textures/item/black_plate_carrier.png");
	}
}
