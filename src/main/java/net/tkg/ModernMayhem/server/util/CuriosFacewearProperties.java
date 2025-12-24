package net.tkg.ModernMayhem.server.util;

import net.minecraftforge.common.ForgeConfigSpec;

public enum CuriosFacewearProperties {
    VISOR(
            "visor",
            4.0,
            0.0,
            0.0
    )
    ;

    private final String name;
    private final double defaultProtection;
    private final double defaultToughness;
    private final double defaultKnockback;
    private final FacewearConfigFile facewearConfigFile;

    CuriosFacewearProperties(String name, double defaultProtection, double defaultToughness, double defaultKnockback) {
        this.name = name;
        this.defaultProtection = defaultProtection;
        this.defaultToughness = defaultToughness;
        this.defaultKnockback = defaultKnockback;
        this.facewearConfigFile = new FacewearConfigFile(name, defaultProtection, defaultToughness, defaultKnockback);
    }

    public String getName() {
        return name;
    }

    public double getProtection() { return this.facewearConfigFile.PROTECTION.get(); }
    public double getToughness() { return this.facewearConfigFile.TOUGHNESS.get(); }
    public double getKnockback() { return this.facewearConfigFile.KNOCKBACK.get(); }

    public ForgeConfigSpec getConfig() {
        return this.facewearConfigFile.getConfig();
    }

    public static class FacewearConfigFile {
        public final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public ForgeConfigSpec CONFIG;

        public ForgeConfigSpec.DoubleValue PROTECTION;
        public ForgeConfigSpec.DoubleValue TOUGHNESS;
        public ForgeConfigSpec.DoubleValue KNOCKBACK;

        public FacewearConfigFile(String name, double protection, double toughness, double knockback) {
            BUILDER.push("Facewear Item: " + name);

            PROTECTION = define("protectionAmount", "Protection amount provided by this item", protection);
            TOUGHNESS = define("toughnessAmount", "Armor Toughness provided by this item", toughness);
            KNOCKBACK = define("knockbackResistance", "Knockback Resistance provided by this item", knockback);

            BUILDER.pop();
            CONFIG = BUILDER.build();
        }

        private ForgeConfigSpec.DoubleValue define(String path, String description, double defaultValue) {
            return BUILDER.comment("\n" + description, "Default: " + defaultValue).defineInRange(path, defaultValue, 0.0, 100.0);
        }

        public ForgeConfigSpec getConfig() {
            return CONFIG;
        }
    }
}
