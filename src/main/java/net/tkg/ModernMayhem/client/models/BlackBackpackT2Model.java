package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.BlackBackpackT2Item;
import software.bernie.geckolib.model.GeoModel;

public class BlackBackpackT2Model extends GeoModel<BlackBackpackT2Item> {
    @Override
    public ResourceLocation getModelResource(BlackBackpackT2Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/backpack_t2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlackBackpackT2Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/black_backpack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlackBackpackT2Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
