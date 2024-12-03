package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.BlackBackpackT3Item;
import software.bernie.geckolib.model.GeoModel;

public class BlackBackpackT3Model extends GeoModel<BlackBackpackT3Item> {
    @Override
    public ResourceLocation getModelResource(BlackBackpackT3Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/backpack_t3.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlackBackpackT3Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/black_backpack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlackBackpackT3Item animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
