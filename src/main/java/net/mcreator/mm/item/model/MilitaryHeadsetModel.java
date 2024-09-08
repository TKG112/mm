package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.MilitaryHeadsetItem;

public class MilitaryHeadsetModel extends GeoModel<MilitaryHeadsetItem> {
	@Override
	public ResourceLocation getAnimationResource(MilitaryHeadsetItem object) {
		return new ResourceLocation("mm", "animations/black_headset.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MilitaryHeadsetItem object) {
		return new ResourceLocation("mm", "geo/black_headset.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MilitaryHeadsetItem object) {
		return new ResourceLocation("mm", "textures/item/black_headset.png");
	}
}
