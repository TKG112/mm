package net.tkg.ModernMayhem.item.curios.body;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.renderer.curios.body.PlateCarrierRenderer;
import net.tkg.ModernMayhem.item.generic.GenericBackpackItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;
import java.util.function.Consumer;

public class PlateCarrierItem extends GenericBackpackItem implements GeoItem, ICurioItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final String type;
    private final int variant;

    public PlateCarrierItem(String type, int variant) {
        super((byte) getNumberofLinesPlateCarrier(type), (byte) getNumberofCollumnsPlateCarrier(type), (byte) 1);
        this.type = type;
        this.variant = variant;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> lRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new PlateCarrierRenderer();
                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }

        });
        consumer.accept(new IClientItemExtensions() {
            private PlateCarrierRenderer.PlateCarrierSlotRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new PlateCarrierRenderer.PlateCarrierSlotRenderer();

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

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        multimap.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor", 7.0D, AttributeModifier.Operation.ADDITION));
        multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", 2.0D, AttributeModifier.Operation.ADDITION));
        multimap.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Knockback resistance", 0.3D, AttributeModifier.Operation.ADDITION));
        return multimap;
    }

    public String getType() {
        return this.type;
    }

    public int getVariant() {
        return this.variant;
    }

    private static int getNumberofLinesPlateCarrier(String type) {
        switch (type) {
            case "default":
                return 0;
            case "ammo":
                return 1;
            case "pouches":
                return 2;
            default:
                return 0;
        }
    }

    private static int getNumberofCollumnsPlateCarrier(String type) {
        switch (type) {
            case "default":
                return 0;
            case "ammo":
                return 4;
            case "pouches":
                return 4;
            default:
                return 0;
        }
    }
}