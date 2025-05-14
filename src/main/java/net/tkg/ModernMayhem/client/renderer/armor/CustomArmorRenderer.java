package net.tkg.ModernMayhem.client.renderer.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.tkg.ModernMayhem.client.models.armor.CustomArmorModel;
import net.tkg.ModernMayhem.client.models.curios.back.BackpackModels;
import net.tkg.ModernMayhem.server.item.armor.CustomArmorItem;
import net.tkg.ModernMayhem.server.item.curios.back.BackpackItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;


public class CustomArmorRenderer extends GeoArmorRenderer<CustomArmorItem> {
    public CustomArmorRenderer(EquipmentSlot slot) {
        super(new CustomArmorModel()); // Using DefaultedItemGeoModel like this puts our 'location' as item/armor/example armor in the assets folders.
    }

    public static class CustomBackpackItemRenderer extends GeoItemRenderer<BackpackItem> {
        public CustomBackpackItemRenderer()  { super(new BackpackModels()); }
    }


}