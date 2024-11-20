package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.TanBackpack1Item;

public class TanBackpack1Model extends GeoModel<TanBackpack1Item> {
	@Override
	public ResourceLocation getAnimationResource(TanBackpack1Item object) {
		return new ResourceLocation("mm", "animations/tan_backpack_t1.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TanBackpack1Item object) {
		return new ResourceLocation("mm", "geo/tan_backpack_t1.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TanBackpack1Item object) {
		return new ResourceLocation("mm", "textures/item/tan_backpack.png");
	}
}
