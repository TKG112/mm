package net.tkg.ModernMayhem.client.models.curios.back;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.back.BackpackItem;
import software.bernie.geckolib.model.GeoModel;

public class BackpackModels extends GeoModel<BackpackItem> {
    @Override
    public ResourceLocation getModelResource(BackpackItem animatable) {
        return switch (animatable.getTier()-1) {
            case 0 -> new ResourceLocation(ModernMayhemMod.ID, "geo/item/curios/back/backpack_t1.geo.json");
            case 1 -> new ResourceLocation(ModernMayhemMod.ID, "geo/item/curios/back/backpack_t2.geo.json");
            case 2 -> new ResourceLocation(ModernMayhemMod.ID, "geo/item/curios/back/backpack_t3.geo.json");
            default -> throw new IllegalStateException("Unexpected value: no such tier as " + animatable.getTier());
        };
    }

    @Override
    public ResourceLocation getTextureResource(BackpackItem animatable) {
        return switch (animatable.getVariant()) {
            case 0 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/back/black_backpack.png");
            case 1 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/back/green_backpack.png");
            case 2 -> new ResourceLocation(ModernMayhemMod.ID, "textures/item/curios/back/tan_backpack.png");
            default -> throw new IllegalStateException("Unexpected value: no such variant as " + animatable.getVariant());
        };
    }

    @Override
    public ResourceLocation getAnimationResource(BackpackItem animatable) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
