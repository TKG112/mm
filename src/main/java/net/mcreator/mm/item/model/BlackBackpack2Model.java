package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackBackpack2Item;

public class BlackBackpack2Model extends GeoModel<BlackBackpack2Item> {
	@Override
	public ResourceLocation getAnimationResource(BlackBackpack2Item object) {
		return new ResourceLocation("mm", "animations/black_backpack_t2.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackBackpack2Item object) {
		return new ResourceLocation("mm", "geo/black_backpack_t2.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackBackpack2Item object) {
		return new ResourceLocation("mm", "textures/item/black_backpack.png");
	}
}
