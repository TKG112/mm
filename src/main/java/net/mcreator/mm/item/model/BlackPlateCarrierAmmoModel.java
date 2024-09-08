package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackPlateCarrierAmmoItem;

public class BlackPlateCarrierAmmoModel extends GeoModel<BlackPlateCarrierAmmoItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackPlateCarrierAmmoItem object) {
		return new ResourceLocation("mm", "animations/black_plate_carrier_ammo.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackPlateCarrierAmmoItem object) {
		return new ResourceLocation("mm", "geo/black_plate_carrier_ammo.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackPlateCarrierAmmoItem object) {
		return new ResourceLocation("mm", "textures/item/black_plate_carrier.png");
	}
}
