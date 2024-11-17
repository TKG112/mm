package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackKevlarPantsItem;

public class BlackKevlarPantsModel extends GeoModel<BlackKevlarPantsItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackKevlarPantsItem object) {
		return new ResourceLocation("mm", "animations/black_kevlar_clothing.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackKevlarPantsItem object) {
		return new ResourceLocation("mm", "geo/black_kevlar_clothing.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackKevlarPantsItem object) {
		return new ResourceLocation("mm", "textures/item/black_kevlar_clothing.png");
	}
}
