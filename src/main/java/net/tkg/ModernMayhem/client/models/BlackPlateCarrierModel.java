package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.body.BlackPlateCarrierAmmoItem;
import net.tkg.ModernMayhem.item.curios.body.BlackPlateCarrierItem;
import software.bernie.geckolib.model.GeoModel;

public class BlackPlateCarrierModel extends GeoModel<BlackPlateCarrierItem> {
    @Override
    public ResourceLocation getModelResource(BlackPlateCarrierItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/plate_carrier.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlackPlateCarrierItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/black_plate_carrier.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlackPlateCarrierItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
