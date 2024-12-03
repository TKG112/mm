package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.TanBackpackT3Item;
import software.bernie.geckolib.model.GeoModel;

public class TanBackpackT3Model extends GeoModel<TanBackpackT3Item> {
    @Override
    public ResourceLocation getModelResource(TanBackpackT3Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/backpack_t3.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TanBackpackT3Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/tan_backpack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TanBackpackT3Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
