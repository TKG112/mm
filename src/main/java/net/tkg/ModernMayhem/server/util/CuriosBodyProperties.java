package net.tkg.ModernMayhem.server.util;

import net.minecraftforge.common.ForgeConfigSpec;

public enum CuriosBodyProperties {
    RECON_RIG(
            "recon_rig",
            250,
            2.0,
            0.0,
            0.0
    ),
    BANDOLEER("bandoleer",
            250,
            2.0,
            0.0,
            0.0
    ),
    PLATE_CARRIER(
            "plate_carrier",
            400,
            7.0,
            2.0,
            0.3
    ),
    HEXAGON_RIG(
            "hexagon_rig",
            600,
            10.0,
            3.0,
            0.3
    )
    ;

    private final String name;
    private final int defaultDurability;
    private final double defaultProtection;
    private final double defaultToughness;
    private final double defaultKnockback;
    private final CuriosConfigFile curiosConfigFile;

    CuriosBodyProperties(String name, int defaultDurability, double defaultProtection, double defaultToughness, double defaultKnockback) {
        this.name = name;
        this.defaultDurability = defaultDurability;
        this.defaultProtection = defaultProtection;
        this.defaultToughness = defaultToughness;
        this.defaultKnockback = defaultKnockback;
        this.curiosConfigFile = new CuriosConfigFile(name, defaultDurability, defaultProtection, defaultToughness, defaultKnockback);
    }

    public String getName() { return name; }

    public int getDurability() { return this.curiosConfigFile.DURABILITY.get(); }

    public double getProtection() { return this.curiosConfigFile.PROTECTION.get(); }
    public double getToughness() { return this.curiosConfigFile.TOUGHNESS.get(); }
    public double getKnockback() { return this.curiosConfigFile.KNOCKBACK.get(); }

    public ForgeConfigSpec getConfig() { return this.curiosConfigFile.getConfig(); }

    public static class CuriosConfigFile {
        public final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public ForgeConfigSpec CONFIG;
        public ForgeConfigSpec.IntValue DURABILITY;
        public ForgeConfigSpec.DoubleValue PROTECTION;
        public ForgeConfigSpec.DoubleValue TOUGHNESS;
        public ForgeConfigSpec.DoubleValue KNOCKBACK;

        public CuriosConfigFile(String name, int durability, double protection, double toughness, double knockback) {
            BUILDER.push("Curios Item: " + name);
            DURABILITY = defineInt("durability", "Max Durability of the item (Set to 0 for Unbreakable)", durability);
            PROTECTION = define("protectionAmount", "Protection amount provided by this item", protection);
            TOUGHNESS = define("toughnessAmount", "Armor Toughness provided by this item", toughness);
            KNOCKBACK = define("knockbackResistance", "Knockback Resistance provided by this item", knockback);
            BUILDER.pop();
            CONFIG = BUILDER.build();
        }

        private ForgeConfigSpec.DoubleValue define(String path, String description, double defaultValue) {
            return BUILDER.comment("\n" + description, "Default: " + defaultValue).defineInRange(path, defaultValue, 0.0, 100.0);
        }

        private ForgeConfigSpec.IntValue defineInt(String path, String description, int defaultValue) {
            return BUILDER.comment("\n" + description, "Default: " + defaultValue).defineInRange(path, defaultValue, 0, 10000);
        }
        public ForgeConfigSpec getConfig() { return CONFIG; }
    }
}