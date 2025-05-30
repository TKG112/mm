package net.tkg.ModernMayhem.server.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class TestConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    public static final ForgeConfigSpec.IntValue TEST_INT;

    static {
        BUILDER.comment("Test Config").push("test");
        TEST_INT = BUILDER.comment("Test Int").defineInRange("testInt", 69, 0, 100);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
}
