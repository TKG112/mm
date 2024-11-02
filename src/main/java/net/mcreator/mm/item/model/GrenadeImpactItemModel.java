package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GrenadeImpactItem;

public class GrenadeImpactItemModel extends GeoModel<GrenadeImpactItem> {
	@Override
	public ResourceLocation getAnimationResource(GrenadeImpactItem animatable) {
		return new ResourceLocation("mm", "animations/grenade_impact.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GrenadeImpactItem animatable) {
		return new ResourceLocation("mm", "geo/grenade_impact.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GrenadeImpactItem animatable) {
		return new ResourceLocation("mm", "textures/item/grenade_impact.png");
	}
}
