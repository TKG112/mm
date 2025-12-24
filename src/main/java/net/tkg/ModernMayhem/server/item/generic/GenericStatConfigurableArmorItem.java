package net.tkg.ModernMayhem.server.item.generic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.tkg.ModernMayhem.server.config.CommonConfig;
import net.tkg.ModernMayhem.server.util.ArmorProperties;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class GenericStatConfigurableArmorItem extends ArmorItem {

    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
    };

    public static final int[] BASE_DURABILITY = new int[]{ 13, 15, 16, 11 };
    public final ArmorProperties armorConfig;

    public GenericStatConfigurableArmorItem(ArmorProperties pConfigs, Type pType) {
        super(
                new ArmorMaterial() {
                    @Override
                    public int getDurabilityForType(@NotNull Type type) {
                        return BASE_DURABILITY[type.getSlot().getIndex()] * pConfigs.getDefaultDurability(type);
                    }

                    @Override
                    public int getDefenseForType(@NotNull Type type) {
                        return (int) pConfigs.getDefaultProtection(type);
                    }

                    @Override public int getEnchantmentValue() { return 0; }

                    @Override
                    public @NotNull SoundEvent getEquipSound() {
                        return Objects.requireNonNullElse(pConfigs.getEquipSound(), SoundEvents.ARMOR_EQUIP_GENERIC);
                    }

                    @Override public @NotNull Ingredient getRepairIngredient() { return Ingredient.of(); }
                    @Override public @NotNull String getName() { return pConfigs.getName(); }

                    @Override
                    public float getToughness() {
                        return (float) pConfigs.getDefaultToughness(pType);
                    }

                    @Override
                    public float getKnockbackResistance() {
                        return (float) pConfigs.getDefaultKnockback(pType);
                    }
                },
                pType,
                new Properties().stacksTo(1).rarity(Rarity.COMMON)
        );
        this.armorConfig = pConfigs;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return BASE_DURABILITY[this.getType().getSlot().getIndex()] * armorConfig.getDurabilityMultiplier(this.getType());
    }

    @Override
    public boolean canBeDepleted() {
        return this.getMaxDamage(ItemStack.EMPTY) > 0;
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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != this.getType().getSlot()) {
            return super.getAttributeModifiers(slot, stack);
        }

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        double protection = this.armorConfig.getProtectionAmount(this.getType());
        double toughness = this.armorConfig.getToughnessAmount(this.getType());
        double knockback = this.armorConfig.getKnockbackResistance(this.getType());

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

        if (protection > 0) modifiers.put(Attributes.ARMOR, new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], "Armor modifier", protection, AttributeModifier.Operation.ADDITION));
        if (toughness > 0) modifiers.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], "Armor toughness", toughness, AttributeModifier.Operation.ADDITION));
        if (knockback > 0) modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], "Armor knockback resistance", knockback, AttributeModifier.Operation.ADDITION));

        return modifiers;
    }
}