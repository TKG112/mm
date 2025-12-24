package net.tkg.ModernMayhem.server.util;

import net.minecraftforge.common.ForgeConfigSpec;

public enum BackpackStorageProperties {
    TIER_1("tier_1", 3, 3),
    TIER_2("tier_2", 3, 6),
    TIER_3("tier_3", 3, 9);

    private final String name;
    private final int defaultLines;
    private final int defaultColumns;
    private final StorageConfigFile configFile;

    BackpackStorageProperties(String name, int defaultLines, int defaultColumns) {
        this.name = name;
        this.defaultLines = defaultLines;
        this.defaultColumns = defaultColumns;
        this.configFile = new StorageConfigFile(name, defaultLines, defaultColumns);
    }

    public String getName() {
        return name;
    }

    public int getLines() {
        return this.configFile.LINES.get();
    }

    public int getColumns() {
        return this.configFile.COLUMNS.get();
    }

    public ForgeConfigSpec getConfig() {
        return this.configFile.getConfig();
    }

    // Helper to get properties by tier ID
    public static BackpackStorageProperties getByTier(int tier) {
        return switch (tier) {
            case 1 -> TIER_1;
            case 2 -> TIER_2;
            case 3 -> TIER_3;
            default -> TIER_1;
        };
    }

    public static class StorageConfigFile {
        public final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public ForgeConfigSpec CONFIG;
        public ForgeConfigSpec.IntValue LINES;
        public ForgeConfigSpec.IntValue COLUMNS;

        public StorageConfigFile(String name, int defaultLines, int defaultCols) {
            BUILDER.push("Backpack Configuration: " + name);

            LINES = BUILDER.comment("\nNumber of lines (rows) in the inventory.", "Default: " + defaultLines)
                    .defineInRange("lines", defaultLines, 0, 9);

            COLUMNS = BUILDER.comment("\nNumber of columns in the inventory.", "Default: " + defaultCols)
                    .defineInRange("columns", defaultCols, 0, 9);

            BUILDER.pop();
            CONFIG = BUILDER.build();
        }

        public ForgeConfigSpec getConfig() { return CONFIG; }
    }
}