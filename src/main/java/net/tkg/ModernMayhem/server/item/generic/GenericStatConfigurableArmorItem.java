package net.tkg.ModernMayhem.server.item.generic;

import com.google.common.collect.Multimap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.tkg.ModernMayhem.server.util.ArmorConfigs;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GenericStatConfigurableArmorItem extends ArmorItem {

    // Needed to calculate the durability of the armor
    public static final int[] BASE_DURABILITY = new int[]{ 13, 15, 16, 11 };

    public static ArmorConfigs armorConfig;
    public static ArmorConfigs.SerializedArmorConfig serializedArmorConfig;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;


    public GenericStatConfigurableArmorItem(
            ArmorConfigs pConfigs,
            ArmorConfigs.SerializedArmorConfig pSerializedArmorConfig,
            Type pType
    ) {
        super(
                new ArmorMaterial() {

                    @Override
                    public int getDurabilityForType(@NotNull Type type) {
                        return BASE_DURABILITY[type.getSlot().getIndex()] * pSerializedArmorConfig.getDurabilityMultiplierArray()[type.getSlot().getIndex()];
                    }

                    @Override
                    public int getDefenseForType(@NotNull Type type) {
                        return 0;
                    }

                    @Override
                    public int getEnchantmentValue() {
                        return 0;
                    }

                    @Override
                    public @NotNull SoundEvent getEquipSound() {
                        // If the equip sound is null, return the generic armor equip sound
                        return Objects.requireNonNullElse(pConfigs.getEquipSound(), SoundEvents.ARMOR_EQUIP_GENERIC);
                    }

                    @Override
                    public @NotNull Ingredient getRepairIngredient() {
                        // If the repair ingredient is null, return an empty ingredient
                        return Ingredient.of();
                    }

                    @Override
                    public @NotNull String getName() {
                        return pConfigs.getName();
                    }

                    @Override
                    public float getToughness() {
                        return 0;
                    }

                    @Override
                    public float getKnockbackResistance() {
                        return 0;
                    }
                },
                pType,
                new Properties().stacksTo(1).rarity(Rarity.COMMON)
        );
        armorConfig = pConfigs;
        serializedArmorConfig = pSerializedArmorConfig;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != this.getType().getSlot()) {
            return super.getAttributeModifiers(slot, stack); // If the slot is not the same as the slot of the armor, return the default attribute modifiers
            // We are doing this in order not to have 4 times the same attribute modifiers when equipped in each slot.
        }
        if (this.attributeModifiers == null) {
            this.attributeModifiers = serializedArmorConfig.getAttributeModifiers(this.getType()); // Get the attribute modifiers from the armor material
        }
        return this.attributeModifiers; // Return the attribute modifiers from the armor material
    }

}
