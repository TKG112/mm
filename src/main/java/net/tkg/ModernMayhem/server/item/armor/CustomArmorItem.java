package net.tkg.ModernMayhem.server.item.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.renderer.armor.CustomArmorRenderer;
import net.tkg.ModernMayhem.server.item.generic.GenericStatConfigurableArmorItem;
import net.tkg.ModernMayhem.server.util.ArmorProperties;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class CustomArmorItem extends GenericStatConfigurableArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int variant;
    private final boolean hasFaceWearCapability;

    public CustomArmorItem(
            ArmorProperties pConfig,
            Type pType,
            int pVariant
    ) {
        this(pConfig, pType, pVariant, false);
    }


    public CustomArmorItem(
        ArmorProperties pConfig,
        Type pType,
        int pVariant,
        boolean pHasFaceWearCapability
    ) {
        super(
            pConfig,
            pType
        );
        this.variant = pVariant;
        this.hasFaceWearCapability = pHasFaceWearCapability;
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new CustomArmorRenderer();

                // This prepares our GeoArmorRenderer for the current render frame.
                // These parameters may be null however, so we don't do anything further with them
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
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

    public boolean hasFaceWearCapability() { return hasFaceWearCapability; }
}
