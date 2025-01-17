package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.body.BlackPlateCarrierPouchesItem;
import net.tkg.ModernMayhem.item.curios.body.TanPlateCarrierPouchesItem;
import software.bernie.geckolib.model.GeoModel;

public class TanPlateCarrierPouchesModel extends GeoModel<TanPlateCarrierPouchesItem> {
    @Override
    public ResourceLocation getModelResource(TanPlateCarrierPouchesItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/plate_carrier_pouches.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TanPlateCarrierPouchesItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/tan_plate_carrier.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TanPlateCarrierPouchesItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}