package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackKevlarBodyItem;

public class BlackKevlarBodyModel extends GeoModel<BlackKevlarBodyItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackKevlarBodyItem object) {
		return new ResourceLocation("mm", "animations/black_kevlar_clothing.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackKevlarBodyItem object) {
		return new ResourceLocation("mm", "geo/black_kevlar_clothing.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackKevlarBodyItem object) {
		return new ResourceLocation("mm", "textures/item/black_kevlar_clothing.png");
	}
}
