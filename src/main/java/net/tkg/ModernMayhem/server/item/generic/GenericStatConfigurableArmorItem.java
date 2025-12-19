package net.tkg.ModernMayhem.server.item.generic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.tkg.ModernMayhem.server.util.ArmorProperties;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class GenericStatConfigurableArmorItem extends ArmorItem {

    // UUIDs to ensure attributes stack correctly
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
    };

    public static final int[] BASE_DURABILITY = new int[]{ 13, 15, 16, 11 };

    // [FIX] REMOVED 'static'. Each armor item needs its OWN config reference.
    public final ArmorProperties armorConfig;

    public GenericStatConfigurableArmorItem(ArmorProperties pConfigs, Type pType) {
        super(
                new ArmorMaterial() {
                    @Override
                    public int getDurabilityForType(@NotNull Type type) {
                        return BASE_DURABILITY[type.getSlot().getIndex()] * pConfigs.getDurabilityMultiplier(type);
                    }

                    @Override
                    public int getDefenseForType(@NotNull Type type) {
                        // [FIX] Use getDefaultProtection (Safe for Registry/Startup)
                        return (int) pConfigs.getDefaultProtection(type);
                    }

                    @Override
                    public int getEnchantmentValue() { return 0; }

                    @Override
                    public @NotNull SoundEvent getEquipSound() {
                        return Objects.requireNonNullElse(pConfigs.getEquipSound(), SoundEvents.ARMOR_EQUIP_GENERIC);
                    }

                    @Override
                    public @NotNull Ingredient getRepairIngredient() { return Ingredient.of(); }

                    @Override
                    public @NotNull String getName() { return pConfigs.getName(); }

                    @Override
                    public float getToughness() {
                        // [FIX] Use getDefaultToughness (Safe for Registry/Startup)
                        return pConfigs.getDefaultToughness(pType);
                    }

                    @Override
                    public float getKnockbackResistance() {
                        // [FIX] Use getDefaultKnockback (Safe for Registry/Startup)
                        return pConfigs.getDefaultKnockback(pType);
                    }
                },
                pType,
                new Properties().stacksTo(1).rarity(Rarity.COMMON)
        );
        this.armorConfig = pConfigs;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != this.getType().getSlot()) {
            return super.getAttributeModifiers(slot, stack);
        }

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        // [FIX] Here we use the Config Getters (Safe for Runtime/Gameplay)
        float protection = this.armorConfig.getProtectionAmount(this.getType());
        float toughness = this.armorConfig.getToughnessAmount(this.getType());
        float knockback = this.armorConfig.getKnockbackResistance(this.getType());

        if (protection > 0) {
            modifiers.put(Attributes.ARMOR, new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], "Armor modifier", protection, AttributeModifier.Operation.ADDITION));
        }
        if (toughness > 0) {
            modifiers.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], "Armor toughness", toughness, AttributeModifier.Operation.ADDITION));
        }
        if (knockback > 0) {
            modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()], "Armor knockback resistance", knockback, AttributeModifier.Operation.ADDITION));
        }

        return modifiers;
    }
}