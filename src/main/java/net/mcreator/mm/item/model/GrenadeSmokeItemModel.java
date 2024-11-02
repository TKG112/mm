package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GrenadeSmokeItem;

public class GrenadeSmokeItemModel extends GeoModel<GrenadeSmokeItem> {
	@Override
	public ResourceLocation getAnimationResource(GrenadeSmokeItem animatable) {
		return new ResourceLocation("mm", "animations/grenade_smoke.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GrenadeSmokeItem animatable) {
		return new ResourceLocation("mm", "geo/grenade_smoke.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GrenadeSmokeItem animatable) {
		return new ResourceLocation("mm", "textures/item/grenade_smoke.png");
	}
}
