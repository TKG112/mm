package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackIOLAItem;

public class BlackIOLAModel extends GeoModel<BlackIOLAItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackIOLAItem object) {
		return new ResourceLocation("mm", "animations/black_iola.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackIOLAItem object) {
		return new ResourceLocation("mm", "geo/black_iola.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackIOLAItem object) {
		return new ResourceLocation("mm", "textures/item/black_iola.png");
	}
}
