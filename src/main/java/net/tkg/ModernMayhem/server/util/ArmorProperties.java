package net.tkg.ModernMayhem.server.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.common.ForgeConfigSpec;

public enum ArmorProperties {
    KEVLAR(
            "kevlar",
            new int[]{25, 25, 25, 25},
            SoundEvents.ARMOR_EQUIP_LEATHER,
            new float[]{3, 6, 8, 3},
            new float[]{3, 3, 3, 3},
            new float[]{0.15f, 0.15f, 0.15f, 0.15f}
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
            new float[]{4, 8, 10, 4},
            new float[]{3, 3, 3, 3},
            new float[]{0.25f, 0.25f, 0.25f, 0.25f}
    ),
    HAZMAT(
            "hazmat",
            new int[]{25, 25, 25, 25},
            SoundEvents.ARMOR_EQUIP_LEATHER,
            new float[]{2, 5, 4, 1},
            new float[]{0, 0, 0, 0},
            new float[]{0, 0, 0, 0}
    ),
    ;

    private final String name;
    private final int[] durabilityMultiplierArray;
    private final SoundEvent equipSound;
    private final float[] protectionAmountArray;
    private final float[] toughnessAmountArray;
    private final float[] knockbackResistanceArray;
    private final ArmorConfigFile armorConfigFile;

    ArmorProperties(
            String name,
            int[] durabilityMultiplierArray,
            SoundEvent equipSound,
            float[] protectionAmountArray,
            float[] toughnessAmountArray,
            float[] knockbackResistanceArray
    ) {
        this.name = name;
        this.durabilityMultiplierArray = durabilityMultiplierArray;
        this.equipSound = equipSound;
        this.protectionAmountArray = protectionAmountArray;
        this.toughnessAmountArray = toughnessAmountArray;
        this.knockbackResistanceArray = knockbackResistanceArray;
        this.armorConfigFile = new ArmorConfigFile(
                this.name,
                this.protectionAmountArray,
                this.toughnessAmountArray,
                this.knockbackResistanceArray
        );
    }

    public String getName() { return name; }

    public int getDurabilityMultiplier(ArmorItem.Type type) { return durabilityMultiplierArray[type.getSlot().getIndex()]; }

    public SoundEvent getEquipSound() { return equipSound; }

    public float getProtectionAmount(ArmorItem.Type type) { return this.armorConfigFile.getProtection(type); }
    public float getToughnessAmount(ArmorItem.Type type) { return this.armorConfigFile.getToughness(type); }
    public float getKnockbackResistance(ArmorItem.Type type) { return this.armorConfigFile.getKnockback(type); }

    public float getDefaultProtection(ArmorItem.Type type) { return protectionAmountArray[type.getSlot().getIndex()]; }
    public float getDefaultToughness(ArmorItem.Type type) { return toughnessAmountArray[type.getSlot().getIndex()]; }
    public float getDefaultKnockback(ArmorItem.Type type) { return knockbackResistanceArray[type.getSlot().getIndex()]; }

    public ForgeConfigSpec getConfig() { return this.armorConfigFile.getConfig(); }

    public static class ArmorConfigFile {
        public final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public ForgeConfigSpec CONFIG;

        public ForgeConfigSpec.DoubleValue PROTECTION_AMOUNT_HEAD;
        public ForgeConfigSpec.DoubleValue PROTECTION_AMOUNT_CHESTPLATE;
        public ForgeConfigSpec.DoubleValue PROTECTION_AMOUNT_LEGGINGS;
        public ForgeConfigSpec.DoubleValue PROTECTION_AMOUNT_BOOTS;

        public ForgeConfigSpec.DoubleValue TOUGHNESS_AMOUNT_HEAD;
        public ForgeConfigSpec.DoubleValue TOUGHNESS_AMOUNT_CHESTPLATE;
        public ForgeConfigSpec.DoubleValue TOUGHNESS_AMOUNT_LEGGINGS;
        public ForgeConfigSpec.DoubleValue TOUGHNESS_AMOUNT_BOOTS;

        public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_HEAD;
        public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_CHESTPLATE;
        public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_LEGGINGS;
        public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_BOOTS;

        public ArmorConfigFile(String name, float[] protection, float[] toughness, float[] knockback) {
            BUILDER.push("Armor type : " + name);

            PROTECTION_AMOUNT_HEAD       = define("protectionAmountHead",       "Protection amount for head armor",       protection[3]);
            PROTECTION_AMOUNT_CHESTPLATE = define("protectionAmountChestplate", "Protection amount for chestplate armor", protection[2]);
            PROTECTION_AMOUNT_LEGGINGS   = define("protectionAmountLeggings",   "Protection amount for leggings armor",   protection[1]);
            PROTECTION_AMOUNT_BOOTS      = define("protectionAmountBoots",      "Protection amount for boots armor",      protection[0]);

            TOUGHNESS_AMOUNT_HEAD       = define("toughnessAmountHead",       "Toughness amount for head armor",       toughness[3]);
            TOUGHNESS_AMOUNT_CHESTPLATE = define("toughnessAmountChestplate", "Toughness amount for chestplate armor", toughness[2]);
            TOUGHNESS_AMOUNT_LEGGINGS   = define("toughnessAmountLeggings",   "Toughness amount for leggings armor",   toughness[1]);
            TOUGHNESS_AMOUNT_BOOTS      = define("toughnessAmountBoots",      "Toughness amount for boots armor",      toughness[0]);

            KNOCKBACK_RESISTANCE_HEAD       = define("knockbackResistanceHead",       "Knockback resistance for head armor",       knockback[3]);
            KNOCKBACK_RESISTANCE_CHESTPLATE = define("knockbackResistanceChestplate", "Knockback resistance for chestplate armor", knockback[2]);
            KNOCKBACK_RESISTANCE_LEGGINGS   = define("knockbackResistanceLeggings",   "Knockback resistance for leggings armor",   knockback[1]);
            KNOCKBACK_RESISTANCE_BOOTS      = define("knockbackResistanceBoots",      "Knockback resistance for boots armor",      knockback[0]);

            BUILDER.pop();
            CONFIG = BUILDER.build();
        }

        private ForgeConfigSpec.DoubleValue define(String path, String description, double defaultValue) {
            return BUILDER
                    .comment("\n" + description, "Default: " + defaultValue)
                    .defineInRange(path, defaultValue, 0, 100);
        }

        public float getProtection(ArmorItem.Type type) {
            return switch (type.getSlot()) {
                case HEAD -> PROTECTION_AMOUNT_HEAD.get().floatValue();
                case CHEST -> PROTECTION_AMOUNT_CHESTPLATE.get().floatValue();
                case LEGS -> PROTECTION_AMOUNT_LEGGINGS.get().floatValue();
                case FEET -> PROTECTION_AMOUNT_BOOTS.get().floatValue();
                default -> 0f;
            };
        }

        public float getToughness(ArmorItem.Type type) {
            return switch (type.getSlot()) {
                case HEAD -> TOUGHNESS_AMOUNT_HEAD.get().floatValue();
                case CHEST -> TOUGHNESS_AMOUNT_CHESTPLATE.get().floatValue();
                case LEGS -> TOUGHNESS_AMOUNT_LEGGINGS.get().floatValue();
                case FEET -> TOUGHNESS_AMOUNT_BOOTS.get().floatValue();
                default -> 0f;
            };
        }

        public float getKnockback(ArmorItem.Type type) {
            return switch (type.getSlot()) {
                case HEAD -> KNOCKBACK_RESISTANCE_HEAD.get().floatValue();
                case CHEST -> KNOCKBACK_RESISTANCE_CHESTPLATE.get().floatValue();
                case LEGS -> KNOCKBACK_RESISTANCE_LEGGINGS.get().floatValue();
                case FEET -> KNOCKBACK_RESISTANCE_BOOTS.get().floatValue();
                default -> 0f;
            };
        }

        public ForgeConfigSpec getConfig() { return CONFIG; }
    }
}