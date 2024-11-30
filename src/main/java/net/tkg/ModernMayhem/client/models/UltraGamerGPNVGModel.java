package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.facewear.UltraGamerGPNVGItem;
import software.bernie.geckolib.model.GeoModel;

public class UltraGamerGPNVGModel extends GeoModel<UltraGamerGPNVGItem> {
    @Override
    public ResourceLocation getModelResource(UltraGamerGPNVGItem object) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/black_gpnvg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(UltraGamerGPNVGItem object) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/ultra_gamer_gpnvg.png");
    }

    @Override
    public ResourceLocation getAnimationResource(UltraGamerGPNVGItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animations/item/black_gpnvg.animation.json");
    }
}
