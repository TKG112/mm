package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.TanPlateCarrierAmmoItem;

public class TanPlateCarrierAmmoModel extends GeoModel<TanPlateCarrierAmmoItem> {
	@Override
	public ResourceLocation getAnimationResource(TanPlateCarrierAmmoItem object) {
		return new ResourceLocation("mm", "animations/black_plate_carrier_ammo.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TanPlateCarrierAmmoItem object) {
		return new ResourceLocation("mm", "geo/black_plate_carrier_ammo.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TanPlateCarrierAmmoItem object) {
		return new ResourceLocation("mm", "textures/item/black_plate_carrier.png");
	}
}
