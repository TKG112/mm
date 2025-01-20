package net.tkg.ModernMayhem.client.models.curios.body;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.body.PlateCarrierItem;
import software.bernie.geckolib.model.GeoModel;

public class PlateCarrierModel extends GeoModel<PlateCarrierItem> {
    @Override
    public ResourceLocation getModelResource(PlateCarrierItem animatable) {
        return switch (animatable.getType()) {
            case "default" -> new ResourceLocation(ModernMayhemMod.ID, "geo/item/curios/body/plate_carrier.geo.json");
            case "ammo" -> new ResourceLocation(ModernMayhemMod.ID, "geo/item/curios/body/plate_carrier_ammo.geo.json");
            case "pouches" -> new ResourceLocation(ModernMayhemMod.ID, "geo/item/curios/body/plate_carrier_pouches.geo.json");
            default -> throw new IllegalStateException("Unexpected value: no such type as " + animatable.getType());
        };
    }

    @Override
    public ResourceLocation getTextureResource(PlateCarrierItem animatable) {
        return switch (animatable.getVariant()) {
            case 0 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/body/black_plate_carrier.png");
            case 1 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/body/tan_plate_carrier.png");
            default -> throw new IllegalStateException("Unexpected value: no such variant as " + animatable.getVariant());
        };
    }

    @Override
    public ResourceLocation getAnimationResource(PlateCarrierItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
