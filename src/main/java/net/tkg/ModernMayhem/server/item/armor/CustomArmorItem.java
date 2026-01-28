package net.tkg.ModernMayhem.server.item.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.renderer.armor.CustomArmorRenderer;
import net.tkg.ModernMayhem.client.renderer.armor.item.CustomArmorItemRenderer;
import net.tkg.ModernMayhem.server.item.generic.GenericStatConfigurableArmorItem;
import net.tkg.ModernMayhem.server.util.ArmorProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class CustomArmorItem extends GenericStatConfigurableArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int variant;
    private final ArmorProperties config;

    public CustomArmorItem(ArmorProperties pConfig, Type pType, int pVariant) {
        super(pConfig, pType);
        this.variant = pVariant;
        this.config = pConfig;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> armorRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                if (armorRenderer == null) armorRenderer = new CustomArmorRenderer(slot);
                armorRenderer.prepForRender(entity, stack, slot, original);
                return armorRenderer;
            }

            @Override
            public @Nullable BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new CustomArmorItemRenderer(); // Use the item-specific renderer
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        // left empty on purpose, this method is used to register animation controllers, but we don't have any for the armors
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public int getVariant() {
        return this.variant;
    }

    public ArmorProperties getConfig() {
        return config;
    }

}
