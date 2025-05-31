package net.tkg.ModernMayhem.client.renderer.custom;

import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;
import net.tkg.ModernMayhem.client.models.custom.NVGFirstPersonModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class NVGFirstPersonRenderer extends GeoItemRenderer<NVGFirstPersonFakeItem> {
    public NVGFirstPersonRenderer() {
        super(new NVGFirstPersonModel());
    }
}
