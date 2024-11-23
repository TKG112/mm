package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.facewear.TanNVG21Item;
import software.bernie.geckolib.model.GeoModel;

public class TanNVG21Model extends GeoModel<TanNVG21Item> {
    @Override
    public ResourceLocation getModelResource(TanNVG21Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/black_nvg21.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TanNVG21Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/tan_nvg21.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TanNVG21Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animations/item/black_nvg21.animation.json");
    }
}
