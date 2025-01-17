package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.BlackBackpackT3Item;
import net.tkg.ModernMayhem.item.curios.back.GreenBackpackT2Item;
import net.tkg.ModernMayhem.item.curios.back.GreenBackpackT3Item;
import software.bernie.geckolib.model.GeoModel;

public class GreenBackpackT3Model extends GeoModel<GreenBackpackT3Item> {
    @Override
    public ResourceLocation getModelResource(GreenBackpackT3Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/backpack_t3.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GreenBackpackT3Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/green_backpack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GreenBackpackT3Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
