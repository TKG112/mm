package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.facewear.GreenNVG21Item;
import software.bernie.geckolib.model.GeoModel;

public class GreenNVG21Model extends GeoModel<GreenNVG21Item> {
    @Override
    public ResourceLocation getModelResource(GreenNVG21Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/black_nvg21.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GreenNVG21Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/green_nvg21.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GreenNVG21Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animations/item/black_nvg21.animation.json");
    }
}
