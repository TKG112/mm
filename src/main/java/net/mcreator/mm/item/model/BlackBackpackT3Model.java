package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackBackpack3Item;

public class BlackBackpackT3Model extends GeoModel<BlackBackpack3Item> {
	@Override
	public ResourceLocation getAnimationResource(BlackBackpack3Item object) {
		return new ResourceLocation("mm", "animations/black_backpack_t3.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackBackpack3Item object) {
		return new ResourceLocation("mm", "geo/black_backpack_t3.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackBackpack3Item object) {
		return new ResourceLocation("mm", "textures/item/black_backpack.png");
	}
}
