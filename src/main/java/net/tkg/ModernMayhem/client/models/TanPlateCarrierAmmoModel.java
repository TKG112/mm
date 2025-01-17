package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.body.BlackPlateCarrierAmmoItem;
import net.tkg.ModernMayhem.item.curios.body.TanPlateCarrierAmmoItem;
import software.bernie.geckolib.model.GeoModel;

public class TanPlateCarrierAmmoModel extends GeoModel<TanPlateCarrierAmmoItem> {
    @Override
    public ResourceLocation getModelResource(TanPlateCarrierAmmoItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/plate_carrier_ammo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TanPlateCarrierAmmoItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/tan_plate_carrier.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TanPlateCarrierAmmoItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
