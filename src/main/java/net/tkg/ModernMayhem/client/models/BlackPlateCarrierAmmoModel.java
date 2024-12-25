package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.BlackBackpackT1Item;
import net.tkg.ModernMayhem.item.curios.body.BlackPlateCarrierAmmoItem;
import software.bernie.geckolib.model.GeoModel;

public class BlackPlateCarrierAmmoModel extends GeoModel<BlackPlateCarrierAmmoItem> {
    @Override
    public ResourceLocation getModelResource(BlackPlateCarrierAmmoItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/plate_carrier_ammo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlackPlateCarrierAmmoItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/black_plate_carrier.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlackPlateCarrierAmmoItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
