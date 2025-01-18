package net.tkg.ModernMayhem.item.curios.back;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.renderer.curios.back.BackpackRenderer;
import net.tkg.ModernMayhem.item.generic.GenericBackpackItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.function.Consumer;

public class BackpackItem extends GenericBackpackItem implements GeoItem, ICurioItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int variant;
    private final int tier;


    public BackpackItem(int variant, int tier) {
        super((byte) getNumberofLinesBackpack(tier), (byte) getNumberofColumnsBackpack(tier), (byte) 0);
        this.variant = variant;
        this.tier = tier;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> lRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new BackpackRenderer();
                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }

        });
        consumer.accept(new IClientItemExtensions() {
            private BackpackRenderer.BackpackSlotRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new BackpackRenderer.BackpackSlotRenderer();

                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, (state) -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public int getVariant() {
        return this.variant;
    }

    public int getTier() {
        return this.tier;
    }

    public static int getNumberofLinesBackpack(int tier) {
        return switch (tier) {
            case 1 -> 3;
            case 2 -> 3;
            case 3 -> 3;
            default -> 1;
        };
    }

    public static int getNumberofColumnsBackpack(int tier) {
        return switch (tier) {
            case 1 -> 3;
            case 2 -> 6;
            case 3 -> 9;
            default -> 1;
        };
    }

}
