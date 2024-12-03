package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.TanBackpackT1Item;
import software.bernie.geckolib.model.GeoModel;

public class TanBackpackT1Model extends GeoModel<TanBackpackT1Item> {
    @Override
    public ResourceLocation getModelResource(TanBackpackT1Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/backpack_t1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TanBackpackT1Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/tan_backpack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TanBackpackT1Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
