package net.tkg.ModernMayhem.client.models.curios.knee;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.knee.KneepadItems;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class KneepadModel extends GeoModel<KneepadItems> {
    @Override
    public ResourceLocation getModelResource(KneepadItems animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/knee/knee_pads.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KneepadItems animatable) {
        switch (animatable.getVariant()) {
            case 0 -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/knee/black_knee_pads.png");
            }
            case 1 -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/knee/green_knee_pads.png");
            }
            case 2 -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/knee/tan_knee_pads.png");
            }
            default -> throw new IllegalStateException("Unexpected value: no such visor variant as " + animatable.getVariant());
        }
    }

    @Override
    public ResourceLocation getAnimationResource(KneepadItems animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "animations/empty.animation.json");
    }
}
