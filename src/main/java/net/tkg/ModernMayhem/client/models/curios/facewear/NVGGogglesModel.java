package net.tkg.ModernMayhem.client.models.curios.facewear;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import software.bernie.geckolib.model.GeoModel;

public class NVGGogglesModel extends GeoModel<NVGGogglesItem> {
    @Override
    public ResourceLocation getModelResource(NVGGogglesItem animatable) {
        return switch (animatable.getConfig().getType()) {
            case 0 -> new ResourceLocation(ModernMayhemMod.ID, "geo/item/curios/facewear/gpnvg.geo.json");
            case 1 -> new ResourceLocation(ModernMayhemMod.ID, "geo/item/curios/facewear/nvg21.geo.json");
            default -> new ResourceLocation(ModernMayhemMod.ID, "NOT_FOUND");
        };
    }

    @Override
    public ResourceLocation getTextureResource(NVGGogglesItem animatable) {
         switch (animatable.getConfig().getType()) {
            case 0 -> {
                return switch (animatable.getConfig().getVariant()) {
                    case 0 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/facewear/black_gpnvg.png");
                    case 1 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_gpnvg.png");
                    case 2 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/facewear/green_gpnvg.png");
                    case 99 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/facewear/ultra_gamer_gpnvg.png");
                    default -> new ResourceLocation(ModernMayhemMod.ID, "NOT_FOUND");
                };
            }
            case 1 -> {
                return switch (animatable.getConfig().getVariant()) {
                    case 0 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/facewear/black_nvg21.png");
                    case 1 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_nvg21.png");
                    case 2 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/facewear/green_nvg21.png");
                    default -> new ResourceLocation(ModernMayhemMod.ID, "NOT_FOUND");
                };
            }
        };
        return new ResourceLocation(ModernMayhemMod.ID, "NOT_FOUND");
    }

    @Override
    public ResourceLocation getAnimationResource(NVGGogglesItem animatable) {
        return switch (animatable.getConfig().getType()) {
            case 0 -> new ResourceLocation(ModernMayhemMod.ID, "animations/item/curios/facewear/gpnvg.animation.json");
            case 1 -> new ResourceLocation(ModernMayhemMod.ID, "animations/item/curios/facewear/nvg21.animation.json");
            default -> new ResourceLocation(ModernMayhemMod.ID, "NOT_FOUND");
        };
    }
}
