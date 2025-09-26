package net.tkg.ModernMayhem.server.item.curios.facewear;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.renderer.curios.facewear.VisorRenderer;
import net.tkg.ModernMayhem.server.item.NVGGoggleList;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class VisorItem extends GenericNVGGogglesItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final NVGGoggleList config;

    public VisorItem(NVGGoggleList nvgGoggleList) {
        super(
                nvgGoggleList.getConfigs(),
                nvgGoggleList.getConfigIndex(),
                nvgGoggleList.getActivationSound(),
                nvgGoggleList.getDeactivationSound()
        );
        this.config = nvgGoggleList;
    }

    @Override
    public boolean shouldRenderShader() {
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> lRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new VisorRenderer();
                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }

        });
        consumer.accept(new IClientItemExtensions() {
            private VisorRenderer.VisorSlotRenderer renderer = null;

            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new VisorRenderer.VisorSlotRenderer();

                return renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public NVGGoggleList getConfig() {
        return config;
    }
}
