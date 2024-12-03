package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.TanBackpackT2Item;
import software.bernie.geckolib.model.GeoModel;

public class TanBackpackT2Model extends GeoModel<TanBackpackT2Item> {
    @Override
    public ResourceLocation getModelResource(TanBackpackT2Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/backpack_t2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TanBackpackT2Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/tan_backpack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TanBackpackT2Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
