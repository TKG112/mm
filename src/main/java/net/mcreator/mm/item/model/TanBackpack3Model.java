package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.TanBackpack3Item;

public class TanBackpack3Model extends GeoModel<TanBackpack3Item> {
	@Override
	public ResourceLocation getAnimationResource(TanBackpack3Item object) {
		return new ResourceLocation("mm", "animations/tan_backpack_t3.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TanBackpack3Item object) {
		return new ResourceLocation("mm", "geo/tan_backpack_t3.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TanBackpack3Item object) {
		return new ResourceLocation("mm", "textures/item/tan_backpack.png");
	}
}
