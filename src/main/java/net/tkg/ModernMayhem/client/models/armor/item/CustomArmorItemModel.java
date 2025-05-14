package net.tkg.ModernMayhem.client.models.armor.item;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.armor.CustomArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class CustomArmorItemModel extends GeoModel<CustomArmorItem> {

    @Override
    public ResourceLocation getModelResource(CustomArmorItem item) {
        return getDynamicPath(item, "geo/armor/item", "geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CustomArmorItem item) {
        return getDynamicPath(item, "textures/item/armor", "png");
    }

    @Override
    public ResourceLocation getAnimationResource(CustomArmorItem item) {
        // Optional – if you need one animation for all or none
        return new ResourceLocation(ModernMayhemMod.ID, "animations/empty.animation.json");
    }

    private ResourceLocation getDynamicPath(CustomArmorItem item, String folder, String extension) {
        ResourceLocation registryId = item.builtInRegistryHolder().key().location();
        if (registryId == null) {
            throw new IllegalStateException("Item does not have a registry name: " + item);
        }

        return new ResourceLocation(
                registryId.getNamespace(),
                String.format("%s/%s.%s", folder, registryId.getPath(), extension)
        );
    }
}
