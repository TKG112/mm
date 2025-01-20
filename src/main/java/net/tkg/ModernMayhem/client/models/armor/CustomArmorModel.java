package net.tkg.ModernMayhem.client.models.armor;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.armor.CustomArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class CustomArmorModel extends GeoModel<CustomArmorItem> {
    @Override
    public ResourceLocation getModelResource(CustomArmorItem customArmorItem) {
        return switch (customArmorItem.getMaterial().getName()) {
            case "kevlar" -> new ResourceLocation(ModernMayhemMod.ID, "geo/armor/kevlar_clothing.geo.json");
            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
        };
    }

    @Override
    public ResourceLocation getTextureResource(CustomArmorItem customArmorItem) {
        switch (customArmorItem.getMaterial().getName()) {
            case "kevlar" -> {
                return switch (customArmorItem.getVariant()) {
                    case 0 -> new ResourceLocation(ModernMayhemMod.ID, "textures/armor/black_kevlar_clothing.png");
                    case 1 -> new ResourceLocation(ModernMayhemMod.ID, "textures/armor/green_kevlar_clothing.png");
                    case 2 -> new ResourceLocation(ModernMayhemMod.ID, "textures/armor/tan_kevlar_clothing.png");
                    default -> throw new IllegalStateException("Unexpected value: No such variant with id " + customArmorItem.getVariant());
                };
            }
            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
        }
    }

    @Override
    public ResourceLocation getAnimationResource(CustomArmorItem customArmorItem) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
