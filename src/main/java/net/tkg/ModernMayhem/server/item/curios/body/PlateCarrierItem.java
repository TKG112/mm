package net.tkg.ModernMayhem.server.item.curios.body;

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
import net.tkg.ModernMayhem.server.config.CommonConfig;
import net.tkg.ModernMayhem.server.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.server.util.CuriosBodyProperties;
import net.tkg.ModernMayhem.server.util.RigStorageProperties;
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
        super((byte) 1);
        this.type = type;
        this.variant = variant;
    }

    @Override
    public int getInventoryLines() {
        return switch (type) {
            case "default" -> RigStorageProperties.PLATE_CARRIER_DEFAULT.getLines();
            case "ammo" -> RigStorageProperties.PLATE_CARRIER_AMMO.getLines();
            case "pouches" -> RigStorageProperties.PLATE_CARRIER_POUCHES.getLines();
            default -> 0;
        };
    }

    @Override
    public boolean canSupplyAmmo() {
        return switch (getType()) {
            case "default" -> RigStorageProperties.PLATE_CARRIER_DEFAULT.suppliesAmmo();
            case "ammo" -> RigStorageProperties.PLATE_CARRIER_AMMO.suppliesAmmo();
            case "pouches" -> RigStorageProperties.PLATE_CARRIER_POUCHES.suppliesAmmo();
            default -> false;
        };
    }

    @Override
    public int getInventoryColumns() {
        return switch (type) {
            case "default" -> RigStorageProperties.PLATE_CARRIER_DEFAULT.getColumns();
            case "ammo" -> RigStorageProperties.PLATE_CARRIER_AMMO.getColumns();
            case "pouches" -> RigStorageProperties.PLATE_CARRIER_POUCHES.getColumns();
            default -> 0;
        };
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
    public boolean canBeDepleted() {
        return this.getMaxDamage(ItemStack.EMPTY) > 0;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return CuriosBodyProperties.PLATE_CARRIER.getDurability();
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (CommonConfig.ENABLE_DYNAMIC_ARMOR_STATS.get()) {
            int newDamage = stack.getDamageValue() + amount;

            if (newDamage >= stack.getMaxDamage()) {
                stack.setDamageValue(stack.getMaxDamage());
                return 0;
            }
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();

        double protection = CuriosBodyProperties.PLATE_CARRIER.getProtection();
        double toughness = CuriosBodyProperties.PLATE_CARRIER.getToughness();
        double knockback = CuriosBodyProperties.PLATE_CARRIER.getKnockback();

        if (CommonConfig.ENABLE_DYNAMIC_ARMOR_STATS.get()) {
            int maxD = stack.getMaxDamage();

            if (maxD > 0) {
                int currentD = stack.getDamageValue();
                double durabilityPercent = 1.0 - ((double) currentD / maxD);
                if (durabilityPercent < 0) durabilityPercent = 0;
                double factor = 0.1 + (0.9 * durabilityPercent);

                protection *= factor;
                toughness *= factor;
                knockback *= factor;
            }
        }

        if (protection > 0) multimap.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor", protection, AttributeModifier.Operation.ADDITION));
        if (toughness > 0) multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", toughness, AttributeModifier.Operation.ADDITION));
        if (knockback > 0) multimap.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Knockback resistance", knockback, AttributeModifier.Operation.ADDITION));

        return multimap;
    }

    public String getType() {
        return this.type;
    }

    public int getVariant() {
        return this.variant;
    }
}