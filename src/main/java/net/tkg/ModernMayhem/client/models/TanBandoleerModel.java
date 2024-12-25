package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.body.TanBandoleerItem;
import software.bernie.geckolib.model.GeoModel;

public class TanBandoleerModel extends GeoModel<TanBandoleerItem> {
    @Override
    public ResourceLocation getModelResource(TanBandoleerItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/bandoleer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TanBandoleerItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/tan_bandoleer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TanBandoleerItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
