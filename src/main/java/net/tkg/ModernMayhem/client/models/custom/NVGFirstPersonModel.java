package net.tkg.ModernMayhem.client.models.custom;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class NVGFirstPersonModel extends GeoModel<NVGFirstPersonFakeItem> {

    @Override
    public ResourceLocation getModelResource(NVGFirstPersonFakeItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/black_gpnvgs_fpm.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NVGFirstPersonFakeItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_gpnvg.png");
    }

    @Override
    public ResourceLocation getAnimationResource(NVGFirstPersonFakeItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/gpnvg_fpa.animation.json");
    }
}
