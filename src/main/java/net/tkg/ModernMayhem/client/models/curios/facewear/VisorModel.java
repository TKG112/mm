package net.tkg.ModernMayhem.client.models.curios.facewear;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.facewear.VisorItems;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class VisorModel extends GeoModel<VisorItems> {
    @Override
    public ResourceLocation getModelResource(VisorItems animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/visor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VisorItems animatable) {
        switch (animatable.getVariant()) {
            case 0 -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_visor.png");
            }
            case 1 -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_visor.png");
            }
            default -> throw new IllegalStateException("Unexpected value: no such visor variant as " + animatable.getVariant());
        }
    }

    @Override
    public ResourceLocation getAnimationResource(VisorItems animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/visor.animation.json");
    }
}
