package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.TanPlateCarrierPouchesItem;

public class TanPlateCarrierPouchesModel extends GeoModel<TanPlateCarrierPouchesItem> {
	@Override
	public ResourceLocation getAnimationResource(TanPlateCarrierPouchesItem object) {
		return new ResourceLocation("mm", "animations/black_plate_carrier_pouches.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TanPlateCarrierPouchesItem object) {
		return new ResourceLocation("mm", "geo/black_plate_carrier_pouches.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TanPlateCarrierPouchesItem object) {
		return new ResourceLocation("mm", "textures/item/black_plate_carrier.png");
	}
}
