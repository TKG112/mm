package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.body.BlackPlateCarrierPouchesItem;
import software.bernie.geckolib.model.GeoModel;

public class BlackPlateCarrierPouchesModel extends GeoModel<BlackPlateCarrierPouchesItem> {
    @Override
    public ResourceLocation getModelResource(BlackPlateCarrierPouchesItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/plate_carrier_pouches.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlackPlateCarrierPouchesItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/black_plate_carrier.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlackPlateCarrierPouchesItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}