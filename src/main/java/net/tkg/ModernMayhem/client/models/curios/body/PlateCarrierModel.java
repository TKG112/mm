package net.tkg.ModernMayhem.client.models.curios.body;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.body.PlateCarrierItem;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class PlateCarrierModel extends GeoModel<PlateCarrierItem> {
    @Override
    public ResourceLocation getModelResource(PlateCarrierItem animatable) {
        return switch (animatable.getType()) {
            case "default" -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/body/plate_carrier.geo.json");
            case "ammo" -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/body/plate_carrier_ammo.geo.json");
            case "pouches" -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/body/plate_carrier_pouches.geo.json");
            default -> throw new IllegalStateException("Unexpected value: no such type as " + animatable.getType());
        };
    }

    @Override
    public ResourceLocation getTextureResource(PlateCarrierItem animatable) {
        return switch (animatable.getVariant()) {
            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/body/black_plate_carrier.png");
            case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/body/tan_plate_carrier.png");
            default -> throw new IllegalStateException("Unexpected value: no such variant as " + animatable.getVariant());
        };
    }

    @Override
    public ResourceLocation getAnimationResource(PlateCarrierItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
