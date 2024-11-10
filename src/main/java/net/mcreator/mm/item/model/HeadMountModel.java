package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.HeadMountItem;

public class HeadMountModel extends GeoModel<HeadMountItem> {
	@Override
	public ResourceLocation getAnimationResource(HeadMountItem object) {
		return new ResourceLocation("mm", "animations/head_mount.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(HeadMountItem object) {
		return new ResourceLocation("mm", "geo/head_mount.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(HeadMountItem object) {
		return new ResourceLocation("mm", "textures/item/head_mount.png");
	}
}