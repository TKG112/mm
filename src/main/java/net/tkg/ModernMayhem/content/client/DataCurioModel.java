package net.tkg.ModernMayhem.content.client;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.content.item.DataCurioItem;
import software.bernie.geckolib.model.GeoModel;

/**
 * One GeoModel for every data-driven curio: model, texture and animation come straight from the
 * item's definition, so a single class serves every backpack, rig and cosmetic from every pack.
 */
public class DataCurioModel extends GeoModel<DataCurioItem> {

    @Override
    public ResourceLocation getModelResource(DataCurioItem item) {
        return item.definition().model();
    }

    @Override
    public ResourceLocation getTextureResource(DataCurioItem item) {
        return item.definition().texture();
    }

    @Override
    public ResourceLocation getAnimationResource(DataCurioItem item) {
        return item.definition().animation();
    }
}
