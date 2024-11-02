package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackKevlarBootsItem;

public class BlackKevlarBootsModel extends GeoModel<BlackKevlarBootsItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackKevlarBootsItem object) {
		return new ResourceLocation("mm", "animations/black_kevlar_armor.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackKevlarBootsItem object) {
		return new ResourceLocation("mm", "geo/black_kevlar_armor.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackKevlarBootsItem object) {
		return new ResourceLocation("mm", "textures/item/black_combat_armor.png");
	}
}
