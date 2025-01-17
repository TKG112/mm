package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.body.BlackPlateCarrierItem;
import net.tkg.ModernMayhem.item.curios.body.TanPlateCarrierItem;
import software.bernie.geckolib.model.GeoModel;

public class TanPlateCarrierModel extends GeoModel<TanPlateCarrierItem> {
    @Override
    public ResourceLocation getModelResource(TanPlateCarrierItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/plate_carrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TanPlateCarrierItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/tan_plate_carrier.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TanPlateCarrierItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
