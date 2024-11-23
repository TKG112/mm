package net.tkg.ModernMayhem.item.curios.facewear;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.renderer.BlackNVG21Renderer;
import net.tkg.ModernMayhem.item.generic.GenericNVGGogglesItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Consumer;

public class BlackNVG21Item extends GenericNVGGogglesItem {
    public BlackNVG21Item() {
        super(
                new NVGConfig(
                        0.5f,
                        0.7f,
                        1,
                        1
                ),
                new ResourceLocation(ModernMayhemMod.ID, "sounds/item/nvg_on"),
                new ResourceLocation(ModernMayhemMod.ID, "sounds/item/nvg_off")
        );
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> lRenderer;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new BlackNVG21Renderer();
                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }
        });
    }
}
