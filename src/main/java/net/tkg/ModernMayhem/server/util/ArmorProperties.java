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
            new double[]{3, 6, 8, 3},
            new double[]{3, 3, 3, 3},
            new double[]{0.15, 0.15, 0.15, 0.15}
    ),
    NOTHING(
            "nothing",
            new int[]{25, 25, 25, 25},
            SoundEvents.ARMOR_EQUIP_LEATHER,
            new double[]{0, 0, 0, 0},
            new double[]{0, 0, 0, 0},
            new double[]{0, 0, 0, 0}
    ),
    RONIN(
            "ronin",
            new int[]{25, 25, 25, 25},
            SoundEvents.ARMOR_EQUIP_LEATHER,
            new double[]{4, 8, 10, 4},
            new double[]{3, 3, 3, 3},
            new double[]{0.25, 0.25, 0.25, 0.25}
    ),
    HAZMAT(
            "hazmat",
            new int[]{25, 25, 25, 25},
            SoundEvents.ARMOR_EQUIP_LEATHER,
            new double[]{2, 5, 4, 1},
            new double[]{0, 0, 0, 0},
            new double[]{0, 0, 0, 0}
    )
    ;

    private final String name;
    private final int[] durabilityMultiplierArray;
    private final SoundEvent equipSound;
    private final double[] protectionAmountArray;
    private final double[] toughnessAmountArray;
    private final double[] knockbackResistanceArray;
    private final ArmorConfigFile armorConfigFile;

    ArmorProperties(String name, int[] durabilityMultiplierArray, SoundEvent equipSound, double[] protectionAmountArray, double[] toughnessAmountArray, double[] knockbackResistanceArray) {
        this.name = name;
        this.durabilityMultiplierArray = durabilityMultiplierArray;
        this.equipSound = equipSound;
        this.protectionAmountArray = protectionAmountArray;
        this.toughnessAmountArray = toughnessAmountArray;
        this.knockbackResistanceArray = knockbackResistanceArray;
        this.armorConfigFile = new ArmorConfigFile(this.name, this.durabilityMultiplierArray, this.protectionAmountArray, this.toughnessAmountArray, this.knockbackResistanceArray);
    }

    public String getName() { return name; }
    public SoundEvent getEquipSound() { return equipSound; }

    public int getDurabilityMultiplier(ArmorItem.Type type) { return this.armorConfigFile.getDurability(type); }
    public double getProtectionAmount(ArmorItem.Type type) { return this.armorConfigFile.getProtection(type); }
    public double getToughnessAmount(ArmorItem.Type type) { return this.armorConfigFile.getToughness(type); }
    public double getKnockbackResistance(ArmorItem.Type type) { return this.armorConfigFile.getKnockback(type); }

    public int getDefaultDurability(ArmorItem.Type type) { return durabilityMultiplierArray[type.getSlot().getIndex()]; }
    public double getDefaultProtection(ArmorItem.Type type) { return protectionAmountArray[type.getSlot().getIndex()]; }
    public double getDefaultToughness(ArmorItem.Type type) { return toughnessAmountArray[type.getSlot().getIndex()]; }
    public double getDefaultKnockback(ArmorItem.Type type) { return knockbackResistanceArray[type.getSlot().getIndex()]; }

    public ForgeConfigSpec getConfig() { return this.armorConfigFile.getConfig(); }

    public static class ArmorConfigFile {
        public final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public ForgeConfigSpec CONFIG;

        public ForgeConfigSpec.IntValue DURABILITY_HEAD;
        public ForgeConfigSpec.IntValue DURABILITY_CHEST;
        public ForgeConfigSpec.IntValue DURABILITY_LEGS;
        public ForgeConfigSpec.IntValue DURABILITY_BOOTS;
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

        public ArmorConfigFile(String name, int[] durability, double[] protection, double[] toughness, double[] knockback) {
            BUILDER.push("Armor type : " + name);

            DURABILITY_HEAD  = defineInt("durabilityMultiplierHead",  "Durability multiplier for helmet",     durability[3]);
            DURABILITY_CHEST = defineInt("durabilityMultiplierChest", "Durability multiplier for chestplate", durability[2]);
            DURABILITY_LEGS  = defineInt("durabilityMultiplierLegs",  "Durability multiplier for leggings",   durability[1]);
            DURABILITY_BOOTS = defineInt("durabilityMultiplierBoots", "Durability multiplier for boots",      durability[0]);

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
            return BUILDER.comment("\n" + description, "Default: " + defaultValue).defineInRange(path, defaultValue, 0.0, 100.0);
        }

        private ForgeConfigSpec.IntValue defineInt(String path, String description, int defaultValue) {
            return BUILDER.comment("\n" + description, "Default: " + defaultValue).defineInRange(path, defaultValue, 0, 10000);
        }

        public int getDurability(ArmorItem.Type type) {
            return switch (type.getSlot()) {
                case HEAD -> DURABILITY_HEAD.get();
                case CHEST -> DURABILITY_CHEST.get();
                case LEGS -> DURABILITY_LEGS.get();
                case FEET -> DURABILITY_BOOTS.get();
                default -> 1;
            };
        }
        public double getProtection(ArmorItem.Type type) {
            return switch (type.getSlot()) {
                case HEAD -> PROTECTION_AMOUNT_HEAD.get();
                case CHEST -> PROTECTION_AMOUNT_CHESTPLATE.get();
                case LEGS -> PROTECTION_AMOUNT_LEGGINGS.get();
                case FEET -> PROTECTION_AMOUNT_BOOTS.get();
                default -> 0.0;
            };
        }
        public double getToughness(ArmorItem.Type type) {
            return switch (type.getSlot()) {
                case HEAD -> TOUGHNESS_AMOUNT_HEAD.get();
                case CHEST -> TOUGHNESS_AMOUNT_CHESTPLATE.get();
                case LEGS -> TOUGHNESS_AMOUNT_LEGGINGS.get();
                case FEET -> TOUGHNESS_AMOUNT_BOOTS.get();
                default -> 0.0;
            };
        }
        public double getKnockback(ArmorItem.Type type) {
            return switch (type.getSlot()) {
                case HEAD -> KNOCKBACK_RESISTANCE_HEAD.get();
                case CHEST -> KNOCKBACK_RESISTANCE_CHESTPLATE.get();
                case LEGS -> KNOCKBACK_RESISTANCE_LEGGINGS.get();
                case FEET -> KNOCKBACK_RESISTANCE_BOOTS.get();
                default -> 0.0;
            };
        }
        public ForgeConfigSpec getConfig() { return CONFIG; }
    }
}