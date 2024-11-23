package net.tkg.ModernMayhem.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.renderer.BlackGPNVGRenderer;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Consumer;

public class BlackGPNVGItem extends GeneralNVGGogglesItem {
    public BlackGPNVGItem() {
        super(new GeneralNVGGogglesItem.NVGConfig(1));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> lRenderer;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new BlackGPNVGRenderer();
                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }
        });
    }
}
