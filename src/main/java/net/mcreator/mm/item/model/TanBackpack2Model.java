package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.TanBackpack2Item;

public class TanBackpack2Model extends GeoModel<TanBackpack2Item> {
	@Override
	public ResourceLocation getAnimationResource(TanBackpack2Item object) {
		return new ResourceLocation("mm", "animations/tan_backpack_t2.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TanBackpack2Item object) {
		return new ResourceLocation("mm", "geo/tan_backpack_t2.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TanBackpack2Item object) {
		return new ResourceLocation("mm", "textures/item/tan_backpack.png");
	}
}
