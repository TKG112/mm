package net.tkg.ModernMayhem.server.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.common.ForgeConfigSpec;

public enum ArmorProperties {
    KEVLAR(
            "kevlar",
            new int[]{25, 25, 25, 25},
            SoundEvents.ARMOR_EQUIP_LEATHER,
            new float[]{1, 2, 3, 1},
            new float[]{2, 4, 6, 2},
            new float[]{4, 8, 12, 1}
            ),
    NOTHING(
            "nothing",
            new int[]{25, 25, 25, 25},
            SoundEvents.ARMOR_EQUIP_LEATHER,
            new float[]{0, 0, 0, 0},
            new float[]{0, 0, 0, 0},
            new float[]{0, 0, 0, 0}
    ),
    RONIN(
            "ronin",
            new int[]{25, 25, 25, 25},
            SoundEvents.ARMOR_EQUIP_LEATHER,
            new float[]{2, 3, 4, 2},
            new float[]{3, 5, 7, 3},
            new float[]{5, 9, 13, 2}
    ),
    HAZMAT(
            "hazmat",
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
    private final ArmorConfigFile armorConfigFile;


    ArmorProperties(
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
        this.armorConfigFile = new ArmorConfigFile(
                this.name,
                this.protectionAmountArray,
                this.ToughnessAmoutArray,
                this.knockbackResistanceArray
        );
    }

    public String getName() {
        return name;
    }

    public int getDurabilityMultiplier(ArmorItem.Type type) { return durabilityMultiplierArray[type.getSlot().getIndex()]; }

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

    public float getToughnessAmount(ArmorItem.Type type) { return ToughnessAmoutArray[type.getSlot().getIndex()]; }

    public float[] getToughnessAmountArray() {
        return ToughnessAmoutArray;
    }

    public float getKnockbackResistance(ArmorItem.Type type) { return knockbackResistanceArray[type.getSlot().getIndex()]; }

    public float[] getKnockbackResistanceArray() {
        return knockbackResistanceArray;
    }

    public ForgeConfigSpec getConfig() { return this.armorConfigFile.getConfig(); }

    public ArmorConfigFile getArmorConfigFile() {
        return this.armorConfigFile;
    }

    public static class ArmorConfigFile {

        public final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public ForgeConfigSpec CONFIG = null;

        public ForgeConfigSpec.DoubleValue PROTECTION_AMOUNT_HEAD = null;
        public ForgeConfigSpec.DoubleValue PROTECTION_AMOUNT_CHESTPLATE = null;
        public ForgeConfigSpec.DoubleValue PROTECTION_AMOUNT_LEGGINGS = null;
        public ForgeConfigSpec.DoubleValue PROTECTION_AMOUNT_BOOTS = null;

        public ForgeConfigSpec.DoubleValue TOUGHNESS_AMOUNT_HEAD = null;
        public ForgeConfigSpec.DoubleValue TOUGHNESS_AMOUNT_CHESTPLATE = null;
        public ForgeConfigSpec.DoubleValue TOUGHNESS_AMOUNT_LEGGINGS = null;
        public ForgeConfigSpec.DoubleValue TOUGHNESS_AMOUNT_BOOTS = null;

        public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_HEAD = null;
        public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_CHESTPLATE = null;
        public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_LEGGINGS = null;
        public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_BOOTS = null;

        public ArmorConfigFile(
                String name,
                float[] protectionAmountArray,
                float[] toughnessAmoutArray,
                float[] knockbackResistanceArray
        ) {
            BUILDER.push("Armor type : " + name);

            //TODO Make these code actually work
            BUILDER.push("WIP, these values do nothing for now, will be re-implemented in future updates");

            PROTECTION_AMOUNT_HEAD = BUILDER.comment("Protection amount for head armor").defineInRange("protectionAmountHead", protectionAmountArray[0], 0, 100);
            PROTECTION_AMOUNT_CHESTPLATE = BUILDER.comment("Protection amount for chestplate armor").defineInRange("protectionAmountChestplate", protectionAmountArray[1], 0, 100);
            PROTECTION_AMOUNT_LEGGINGS = BUILDER.comment("Protection amount for leggings armor").defineInRange("protectionAmountLeggings", protectionAmountArray[2], 0, 100);
            PROTECTION_AMOUNT_BOOTS = BUILDER.comment("Protection amount for boots armor").defineInRange("protectionAmountBoots", protectionAmountArray[3], 0, 100);

            TOUGHNESS_AMOUNT_HEAD = BUILDER.comment("Toughness amount for head armor").defineInRange("toughnessAmountHead", toughnessAmoutArray[0], 0, 100);
            TOUGHNESS_AMOUNT_CHESTPLATE = BUILDER.comment("Toughness amount for chestplate armor").defineInRange("toughnessAmountChestplate", toughnessAmoutArray[1], 0, 100);
            TOUGHNESS_AMOUNT_LEGGINGS = BUILDER.comment("Toughness amount for leggings armor").defineInRange("toughnessAmountLeggings", toughnessAmoutArray[2], 0, 100);
            TOUGHNESS_AMOUNT_BOOTS = BUILDER.comment("Toughness amount for boots armor").defineInRange("toughnessAmountBoots", toughnessAmoutArray[3], 0, 100);

            KNOCKBACK_RESISTANCE_HEAD = BUILDER.comment("Knockback resistance for head armor").defineInRange("knockbackResistanceHead", knockbackResistanceArray[0], 0, 100);
            KNOCKBACK_RESISTANCE_CHESTPLATE = BUILDER.comment("Knockback resistance for chestplate armor").defineInRange("knockbackResistanceChestplate", knockbackResistanceArray[1], 0, 100);
            KNOCKBACK_RESISTANCE_LEGGINGS = BUILDER.comment("Knockback resistance for leggings armor").defineInRange("knockbackResistanceLeggings", knockbackResistanceArray[2], 0, 100);
            KNOCKBACK_RESISTANCE_BOOTS = BUILDER.comment("Knockback resistance for boots armor").defineInRange("knockbackResistanceBoots", knockbackResistanceArray[3], 0, 100);

            BUILDER.pop();

            CONFIG = BUILDER.build();
        }

        public ForgeConfigSpec getConfig() {
            return CONFIG;
        }

        public float[] getProtectionAmountArray() {
            return new float[]{
                PROTECTION_AMOUNT_HEAD.get().floatValue(),
                PROTECTION_AMOUNT_CHESTPLATE.get().floatValue(),
                PROTECTION_AMOUNT_LEGGINGS.get().floatValue(),
                PROTECTION_AMOUNT_BOOTS.get().floatValue()
            };
        }

        public float[] getToughnessAmountArray() {
            return new float[]{
                TOUGHNESS_AMOUNT_HEAD.get().floatValue(),
                TOUGHNESS_AMOUNT_CHESTPLATE.get().floatValue(),
                TOUGHNESS_AMOUNT_LEGGINGS.get().floatValue(),
                TOUGHNESS_AMOUNT_BOOTS.get().floatValue()
            };
        }

        public float[] getKnockbackResistanceArray() {
            return new float[]{
                KNOCKBACK_RESISTANCE_HEAD.get().floatValue(),
                KNOCKBACK_RESISTANCE_CHESTPLATE.get().floatValue(),
                KNOCKBACK_RESISTANCE_LEGGINGS.get().floatValue(),
                KNOCKBACK_RESISTANCE_BOOTS.get().floatValue()
            };
        }
    }

}
