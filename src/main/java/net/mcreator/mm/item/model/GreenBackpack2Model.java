package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GreenBackpack2Item;

public class GreenBackpack2Model extends GeoModel<GreenBackpack2Item> {
	@Override
	public ResourceLocation getAnimationResource(GreenBackpack2Item object) {
		return new ResourceLocation("mm", "animations/green_backpack_t2.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GreenBackpack2Item object) {
		return new ResourceLocation("mm", "geo/green_backpack_t2.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GreenBackpack2Item object) {
		return new ResourceLocation("mm", "textures/item/green_backpack.png");
	}
}
