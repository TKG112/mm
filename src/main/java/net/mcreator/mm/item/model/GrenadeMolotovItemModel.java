package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.GrenadeMolotovItem;

public class GrenadeMolotovItemModel extends GeoModel<GrenadeMolotovItem> {
	@Override
	public ResourceLocation getAnimationResource(GrenadeMolotovItem animatable) {
		return new ResourceLocation("mm", "animations/grenade_molotov.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GrenadeMolotovItem animatable) {
		return new ResourceLocation("mm", "geo/grenade_molotov.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GrenadeMolotovItem animatable) {
		return new ResourceLocation("mm", "textures/item/grenade_molotov.png");
	}
}
