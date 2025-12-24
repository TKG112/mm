package net.tkg.ModernMayhem.server.util;

import net.minecraftforge.common.ForgeConfigSpec;

public enum RigStorageProperties {
    RECON_RIG(
            "recon_rig",
            2,
            6,
            true
    ),
    BANDOLEER(
            "bandoleer",
            2,
            3,
            true
    ),
    PLATE_CARRIER_DEFAULT(
            "plate_carrier_default",
            0,
            0,
            false
    ),
    PLATE_CARRIER_AMMO(
            "plate_carrier_ammo",
            1,
            4,
            true
    ),
    PLATE_CARRIER_POUCHES(
            "plate_carrier_pouches",
            2,
            4,
            false
    ),
    HEXAGON_RIG(
            "hexagon_rig",
            0,
            0,
            false
    );

    private final String name;
    private final int defaultLines;
    private final int defaultColumns;
    private final boolean defaultSuppliesAmmo;
    private final StorageConfigFile configFile;

    RigStorageProperties(String name, int defaultLines, int defaultColumns, boolean defaultSuppliesAmmo) {
        this.name = name;
        this.defaultLines = defaultLines;
        this.defaultColumns = defaultColumns;
        this.defaultSuppliesAmmo = defaultSuppliesAmmo;
        this.configFile = new StorageConfigFile(name, defaultLines, defaultColumns, defaultSuppliesAmmo);
    }

    public String getName() { return name; }

    public int getLines() { return this.configFile.LINES.get(); }
    public int getColumns() { return this.configFile.COLUMNS.get(); }
    // [NEW] Getter
    public boolean suppliesAmmo() { return this.configFile.SUPPLIES_AMMO.get(); }

    public ForgeConfigSpec getConfig() { return this.configFile.getConfig(); }

    public static class StorageConfigFile {
        public final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public ForgeConfigSpec CONFIG;
        public ForgeConfigSpec.IntValue LINES;
        public ForgeConfigSpec.IntValue COLUMNS;
        public ForgeConfigSpec.BooleanValue SUPPLIES_AMMO; // [NEW]

        public StorageConfigFile(String name, int defaultLines, int defaultCols, boolean defaultSuppliesAmmo) {
            BUILDER.push("Rig Configuration: " + name);

            LINES = BUILDER.comment("\nNumber of lines (rows) in the inventory.", "Default: " + defaultLines)
                    .defineInRange("lines", defaultLines, 0, 9);

            COLUMNS = BUILDER.comment("\nNumber of columns in the inventory.", "Default: " + defaultCols)
                    .defineInRange("columns", defaultCols, 0, 9);

            // [NEW] Config Option
            SUPPLIES_AMMO = BUILDER.comment("\nWhether this rig can automatically supply ammo to guns.", "Default: " + defaultSuppliesAmmo)
                    .define("suppliesAmmo", defaultSuppliesAmmo);

            BUILDER.pop();
            CONFIG = BUILDER.build();
        }

        public ForgeConfigSpec getConfig() { return CONFIG; }
    }
}