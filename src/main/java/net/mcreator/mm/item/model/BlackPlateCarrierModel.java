package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackPlateCarrierItem;

public class BlackPlateCarrierModel extends GeoModel<BlackPlateCarrierItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackPlateCarrierItem object) {
		return new ResourceLocation("mm", "animations/black_plate_carrier.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackPlateCarrierItem object) {
		return new ResourceLocation("mm", "geo/black_plate_carrier.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackPlateCarrierItem object) {
		return new ResourceLocation("mm", "textures/item/black_plate_carrier.png");
	}
}
