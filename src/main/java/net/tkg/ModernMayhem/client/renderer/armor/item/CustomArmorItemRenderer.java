package net.tkg.ModernMayhem.client.renderer.armor.item;

import net.tkg.ModernMayhem.client.models.armor.item.CustomArmorItemModel;
import net.tkg.ModernMayhem.server.item.armor.CustomArmorItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CustomArmorItemRenderer extends GeoItemRenderer<CustomArmorItem> {
    public CustomArmorItemRenderer() {
        super(new CustomArmorItemModel());
    }
}
