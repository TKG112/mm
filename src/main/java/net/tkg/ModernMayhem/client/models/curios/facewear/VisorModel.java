package net.tkg.ModernMayhem.client.models.curios.facewear;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.facewear.VisorItem;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class VisorModel extends GeoModel<VisorItem> {
    @Override
    public ResourceLocation getModelResource(VisorItem animatable) {
        return switch (animatable.getConfig().getType()) {
            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/gpnvg.geo.json");
            case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/pvs14.geo.json");
            case 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/pvs7.geo.json");
            case 3 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/visor.geo.json");
            default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
        };
    }

    @Override
    public ResourceLocation getTextureResource(VisorItem animatable) {
        return getTextureResource(animatable.getConfig().getType(), animatable.getConfig().getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(VisorItem animatable) {
        return switch (animatable.getConfig().getType()) {
            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/gpnvg.animation.json");
            case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/pvs14.animation.json");
            case 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/pvs7.animation.json");
            case 3 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/visor.animation.json");
            default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
        };
    }

    public static ResourceLocation getTextureResource(int type, int variant) {
        switch (type) {
            case 0 -> {
                return switch (variant) {
                    case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_gpnvg.png");
                    case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_gpnvg.png");
                    case 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/green_gpnvg.png");
                    case 99 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/ultra_gamer_gpnvg.png");
                    default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                };
            }
            case 1 -> {
                return switch (variant) {
                    case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_pvs14.png");
                    case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_pvs14.png");
                    case 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/green_pvs14.png");
                    default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                };
            }
            case 2 -> {
                return switch (variant) {
                    case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_pvs7.png");
                    default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                };
            }
            case 3 -> {
                return switch (variant) {
                    case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_visor.png");
                    case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_visor.png");
                    default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                };
            }
        };
        return fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
    }
}
