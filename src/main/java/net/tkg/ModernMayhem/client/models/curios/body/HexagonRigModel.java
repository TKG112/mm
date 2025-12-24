package net.tkg.ModernMayhem.client.models.curios.body;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.body.HexagonRigItem;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class HexagonRigModel extends GeoModel<HexagonRigItem> {
    @Override
    public ResourceLocation getModelResource(HexagonRigItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/body/hexagon_rig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HexagonRigItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/body/hexagon_rig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HexagonRigItem animatable) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
