package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackPlateCarrierPouchesItem;

public class BlackPlateCarrierPouchesModel extends GeoModel<BlackPlateCarrierPouchesItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackPlateCarrierPouchesItem object) {
		return new ResourceLocation("mm", "animations/black_plate_carrier_pouches.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackPlateCarrierPouchesItem object) {
		return new ResourceLocation("mm", "geo/black_plate_carrier_pouches.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackPlateCarrierPouchesItem object) {
		return new ResourceLocation("mm", "textures/item/black_plate_carrier.png");
	}
}
