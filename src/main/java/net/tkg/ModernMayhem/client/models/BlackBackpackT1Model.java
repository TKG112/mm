package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.BlackBackpackT1Item;
import software.bernie.geckolib.model.GeoModel;

public class BlackBackpackT1Model extends GeoModel<BlackBackpackT1Item> {
    @Override
    public ResourceLocation getModelResource(BlackBackpackT1Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/backpack_t1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlackBackpackT1Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/black_backpack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlackBackpackT1Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
