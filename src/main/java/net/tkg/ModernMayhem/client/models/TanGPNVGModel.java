package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.facewear.TanGPNVGItem;
import software.bernie.geckolib.model.GeoModel;

public class TanGPNVGModel extends GeoModel<TanGPNVGItem> {
    @Override
    public ResourceLocation getModelResource(TanGPNVGItem object) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/black_gpnvg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TanGPNVGItem object) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/tan_gpnvg.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TanGPNVGItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animations/item/black_gpnvg.animation.json");
    }
}
