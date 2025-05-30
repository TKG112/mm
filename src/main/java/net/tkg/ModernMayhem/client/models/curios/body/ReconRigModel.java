package net.tkg.ModernMayhem.client.models.curios.body;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.body.ReconRigItem;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class ReconRigModel extends GeoModel<ReconRigItem> {
    @Override
    public ResourceLocation getModelResource(ReconRigItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/body/recon_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ReconRigItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/body/green_recon_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ReconRigItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
