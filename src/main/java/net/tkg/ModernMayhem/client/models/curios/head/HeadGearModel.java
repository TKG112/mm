package net.tkg.ModernMayhem.client.models.curios.head;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.head.HeadGearItems;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class HeadGearModel extends GeoModel<HeadGearItems> {
    @Override
    public ResourceLocation getModelResource(HeadGearItems animatable) {
        return switch (animatable.getType()) {
            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/head/balaclava.geo.json");
            case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/glasses.geo.json");
            case 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/goggles.geo.json");
            case 3 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/earwear/headset.geo.json");
            case 4 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/head/military_balaclava.geo.json");
            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + animatable.getType());
        };
    }

    @Override
    public ResourceLocation getTextureResource(HeadGearItems animatable) {
        switch (animatable.getType()) {
            case 0 -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/head/balaclava.png");
            }
            case 1 -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_glasses.png");
            }
            case 2 -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_goggles.png");
            }
            case 3 -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/earwear/black_headset.png");
            }
            case 4 -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/head/black_military_balaclava.png");
            }
            default -> {
                throw new IllegalStateException("Unexpected value: no such armor type as " + animatable.getType());
            }
        }
    }

    @Override
    public ResourceLocation getAnimationResource(HeadGearItems animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "animations/empty.animation.json");
    }
}
