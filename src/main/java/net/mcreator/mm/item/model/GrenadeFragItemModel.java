package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GrenadeFragItem;

public class GrenadeFragItemModel extends GeoModel<GrenadeFragItem> {
	@Override
	public ResourceLocation getAnimationResource(GrenadeFragItem animatable) {
		return new ResourceLocation("mm", "animations/grenade_frag.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GrenadeFragItem animatable) {
		return new ResourceLocation("mm", "geo/grenade_frag.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GrenadeFragItem animatable) {
		return new ResourceLocation("mm", "textures/item/grenade_frag.png");
	}
}
