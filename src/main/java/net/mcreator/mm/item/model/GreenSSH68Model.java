package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GreenSSH68Item;

public class GreenSSH68Model extends GeoModel<GreenSSH68Item> {
	@Override
	public ResourceLocation getAnimationResource(GreenSSH68Item object) {
		return new ResourceLocation("mm", "animations/green_ssh68.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GreenSSH68Item object) {
		return new ResourceLocation("mm", "geo/green_ssh68.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GreenSSH68Item object) {
		return new ResourceLocation("mm", "textures/item/green_ssh68.png");
	}
}
