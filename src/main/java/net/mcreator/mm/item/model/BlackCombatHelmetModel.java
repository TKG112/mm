package net.mcreator.mm.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.item.BlackCombatHelmetItem;

public class BlackCombatHelmetModel extends GeoModel<BlackCombatHelmetItem> {
	@Override
	public ResourceLocation getAnimationResource(BlackCombatHelmetItem object) {
		return new ResourceLocation("mm", "animations/black_combat_helmet.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackCombatHelmetItem object) {
		return new ResourceLocation("mm", "geo/black_combat_helmet.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackCombatHelmetItem object) {
		return new ResourceLocation("mm", "textures/item/black_combat_helmet.png");
	}
}
