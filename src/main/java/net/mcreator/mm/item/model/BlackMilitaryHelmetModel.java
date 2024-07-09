package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackMilitaryHelmetItem;

public class BlackMilitaryHelmetModel extends GeoModel<BlackMilitaryHelmetItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackMilitaryHelmetItem object) {
		return new ResourceLocation("mm", "animations/helmet.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackMilitaryHelmetItem object) {
		return new ResourceLocation("mm", "geo/helmet.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackMilitaryHelmetItem object) {
		return new ResourceLocation("mm", "textures/item/helmets_black.png");
	}
}
