package net.tkg.ModernMayhem.client.models.curios.body;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.body.BandoleerItem;
import software.bernie.geckolib.model.GeoModel;

public class BandoleerModel extends GeoModel<BandoleerItem> {
    @Override
    public ResourceLocation getModelResource(BandoleerItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/curios/body/bandoleer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BandoleerItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/body/tan_bandoleer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BandoleerItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
