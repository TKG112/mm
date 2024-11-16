package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.RoninItem;

public class RoninModel extends GeoModel<RoninItem> {
	@Override
	public ResourceLocation getAnimationResource(RoninItem object) {
		return new ResourceLocation("mm", "animations/black_ronin.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(RoninItem object) {
		return new ResourceLocation("mm", "geo/black_ronin.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(RoninItem object) {
		return new ResourceLocation("mm", "textures/item/black_ronin.png");
	}
}
