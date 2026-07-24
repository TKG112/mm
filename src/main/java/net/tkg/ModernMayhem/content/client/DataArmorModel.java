package net.tkg.ModernMayhem.content.client;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.content.def.ArmorDefinition;
import net.tkg.ModernMayhem.content.item.DataArmorItem;
import software.bernie.geckolib.model.GeoModel;

/**
 * One GeoModel for every data-driven armor piece: model/texture/animation come straight from the
 * item's {@link ArmorDefinition}, replacing the old hardcoded {@code switch} per variant.
 * <p>
 * When rendered on a slim-armed player ({@link DataArmorRenderer#SLIM_CONTEXT}) the slim model/texture
 * are used if the definition provides them.
 */
public class DataArmorModel extends GeoModel<DataArmorItem> {

    @Override
    public ResourceLocation getModelResource(DataArmorItem item) {
        ArmorDefinition def = item.definition();
        if (DataArmorRenderer.SLIM_CONTEXT.get() && def.slimModel() != null) {
            return def.slimModel();
        }
        return def.model();
    }

    @Override
    public ResourceLocation getTextureResource(DataArmorItem item) {
        ArmorDefinition def = item.definition();
        if (DataArmorRenderer.SLIM_CONTEXT.get() && def.slimTexture() != null) {
            return def.slimTexture();
        }
        return def.texture();
    }

    @Override
    public ResourceLocation getAnimationResource(DataArmorItem item) {
        return item.definition().animation();
    }
}
