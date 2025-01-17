package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.BlackBackpackT1Item;
import net.tkg.ModernMayhem.item.curios.back.GreenBackpackT1Item;
import software.bernie.geckolib.model.GeoModel;

public class GreenBackpackT1Model extends GeoModel<GreenBackpackT1Item> {
    @Override
    public ResourceLocation getModelResource(GreenBackpackT1Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/backpack_t1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GreenBackpackT1Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/green_backpack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GreenBackpackT1Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
