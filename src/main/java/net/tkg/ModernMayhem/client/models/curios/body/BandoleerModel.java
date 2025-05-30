package net.tkg.ModernMayhem.client.models.curios.body;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.body.BandoleerItem;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class BandoleerModel extends GeoModel<BandoleerItem> {
    @Override
    public ResourceLocation getModelResource(BandoleerItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/body/bandoleer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BandoleerItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/body/tan_bandoleer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BandoleerItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
