package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GrenadeFlashbangItem;

public class GrenadeFlashbangItemModel extends GeoModel<GrenadeFlashbangItem> {
	@Override
	public ResourceLocation getAnimationResource(GrenadeFlashbangItem animatable) {
		return new ResourceLocation("mm", "animations/grenade_flashbang.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GrenadeFlashbangItem animatable) {
		return new ResourceLocation("mm", "geo/grenade_flashbang.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GrenadeFlashbangItem animatable) {
		return new ResourceLocation("mm", "textures/item/grenade_flashbang.png");
	}
}
