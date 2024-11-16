package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackVisorItem;

public class BlackVisorModel extends GeoModel<BlackVisorItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackVisorItem object) {
		return new ResourceLocation("mm", "animations/black_visor.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackVisorItem object) {
		return new ResourceLocation("mm", "geo/black_visor.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackVisorItem object) {
		return new ResourceLocation("mm", "textures/item/black_visor.png");
	}
}
