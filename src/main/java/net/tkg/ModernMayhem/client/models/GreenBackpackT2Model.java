package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.BlackBackpackT2Item;
import net.tkg.ModernMayhem.item.curios.back.GreenBackpackT2Item;
import software.bernie.geckolib.model.GeoModel;

public class GreenBackpackT2Model extends GeoModel<GreenBackpackT2Item> {
    @Override
    public ResourceLocation getModelResource(GreenBackpackT2Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/backpack_t2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GreenBackpackT2Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/green_backpack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GreenBackpackT2Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
