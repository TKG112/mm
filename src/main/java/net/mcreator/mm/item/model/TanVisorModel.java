package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.TanVisorItem;

public class TanVisorModel extends GeoModel<TanVisorItem> {
	@Override
	public ResourceLocation getAnimationResource(TanVisorItem object) {
		return new ResourceLocation("mm", "animations/tan_visor.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TanVisorItem object) {
		return new ResourceLocation("mm", "geo/tan_visor.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TanVisorItem object) {
		return new ResourceLocation("mm", "textures/item/tan_visor.png");
	}
}
