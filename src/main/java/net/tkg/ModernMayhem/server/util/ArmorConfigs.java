package net.tkg.ModernMayhem.server.util;

import com.google.common.collect.HashMultimap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;

public enum ArmorConfigs {
    KEVLAR(
            "kevlar",
            new int[]{25, 25, 25, 25},
            SoundEvents.ARMOR_EQUIP_LEATHER,
            new float[]{1, 2, 3, 1},
            new float[]{2, 4, 6, 2},
            new float[]{4, 8, 12, 1}
            ),
    ;

    private final String name;
    private final int[] durabilityMultiplierArray;
    private final SoundEvent equipSound;
    private final float[] protectionAmountArray;
    private final float[] ToughnessAmoutArray;
    private final float[] knockbackResistanceArray;
    private final HashMultimap<Attribute, AttributeModifier> materialAttributes;

    ArmorConfigs(
            String name,
            int[] durabilityMultiplierArray,
            SoundEvent equipSound,
            float[] protectionAmountArray,
            float[] toughnessAmoutArray,
            float[] knockbackResistanceArray
    ) {
        this.name = name;
        this.durabilityMultiplierArray = durabilityMultiplierArray;
        this.equipSound = equipSound;
        this.protectionAmountArray = protectionAmountArray;
        this.ToughnessAmoutArray = toughnessAmoutArray;
        this.knockbackResistanceArray = knockbackResistanceArray;
        this.materialAttributes = HashMultimap.create();
        this.materialAttributes.put(Attributes.ARMOR, new AttributeModifier("Armor", 7.0D, AttributeModifier.Operation.ADDITION));
        this.materialAttributes.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Armor toughness", 2.0D, AttributeModifier.Operation.ADDITION));
        this.materialAttributes.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("Knockback resistance", 0.3D, AttributeModifier.Operation.ADDITION));
    }

    public String getName() {
        return name;
    }

    public int getDurabilityMultiplier(ArmorItem.Type type) {
        return durabilityMultiplierArray[type.getSlot().getIndex()];
    }

    public int[] getDurabilityMultiplierArray() {
        return durabilityMultiplierArray;
    }

    public SoundEvent getEquipSound() {
        return equipSound;
    }

    public float getProtectionAmount(ArmorItem.Type type) {
        return protectionAmountArray[type.getSlot().getIndex()];
    }

    public float[] getProtectionAmountArray() {
        return protectionAmountArray;
    }

    public float getToughnessAmount(ArmorItem.Type type) {
        return ToughnessAmoutArray[type.getSlot().getIndex()];
    }

    public float[] getToughnessAmountArray() {
        return ToughnessAmoutArray;
    }

    public float getKnockbackResistance(ArmorItem.Type type) {
        return knockbackResistanceArray[type.getSlot().getIndex()];
    }

    public float[] getKnockbackResistanceArray() {
        return knockbackResistanceArray;
    }

    public HashMultimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return materialAttributes;
    }

    public static class SerializedArmorConfig {
        private final int[] durabilityMultiplierArray;
        private final float[] protectionAmountArray;
        private final float[] toughnessAmoutArray;
        private final float[] knockbackResistanceArray;

        public SerializedArmorConfig(ArmorConfigs armorConfig) {
            this(
                    armorConfig.getDurabilityMultiplierArray(),
                    armorConfig.getProtectionAmountArray(),
                    armorConfig.getToughnessAmountArray(),
                    armorConfig.getKnockbackResistanceArray()
            );
        }

        public SerializedArmorConfig(
                int[] durabilityMultiplierArray,
                float[] protectionAmountArray,
                float[] toughnessAmoutArray,
                float[] knockbackResistanceArray
        ) {
            this.durabilityMultiplierArray = durabilityMultiplierArray;
            this.protectionAmountArray = protectionAmountArray;
            this.toughnessAmoutArray = toughnessAmoutArray;
            this.knockbackResistanceArray = knockbackResistanceArray;
        }

        public int[] getDurabilityMultiplierArray() {
            return durabilityMultiplierArray;
        }

        public float[] getProtectionAmountArray() {
            return protectionAmountArray;
        }

        public float[] getToughnessAmoutArray() {
            return toughnessAmoutArray;
        }

        public float[] getKnockbackResistanceArray() {
            return knockbackResistanceArray;
        }

        public HashMultimap<Attribute, AttributeModifier> getAttributeModifiers(ArmorItem.Type type) {
            HashMultimap<Attribute, AttributeModifier> materialAttributes = HashMultimap.create();
            materialAttributes.put(Attributes.ARMOR, new AttributeModifier("Armor", this.protectionAmountArray[type.getSlot().getIndex()], AttributeModifier.Operation.ADDITION));
            materialAttributes.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Armor toughness", this.toughnessAmoutArray[type.getSlot().getIndex()], AttributeModifier.Operation.ADDITION));
            materialAttributes.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("Knockback resistance", this.knockbackResistanceArray[type.getSlot().getIndex()], AttributeModifier.Operation.ADDITION));
            return materialAttributes;
        }
    }

}
