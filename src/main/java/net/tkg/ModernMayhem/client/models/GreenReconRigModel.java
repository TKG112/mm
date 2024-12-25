package net.tkg.ModernMayhem.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.body.GreenReconRigItem;
import software.bernie.geckolib.model.GeoModel;

public class GreenReconRigModel extends GeoModel<GreenReconRigItem> {
    @Override
    public ResourceLocation getModelResource(GreenReconRigItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "geo/item/recon_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GreenReconRigItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "textures/item/green_recon_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GreenReconRigItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
