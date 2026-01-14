package net.tkg.ModernMayhem.server.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    public static final ForgeConfigSpec.ConfigValue<Boolean> CAN_EAT_WITH_FACEWEAR_DOWN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_DYNAMIC_ARMOR_STATS;

    static {
        BUILDER.push("Modern Mayhem Common Config Settings");

        BUILDER.push("Visor & NVG Goggles configs");
        CAN_EAT_WITH_FACEWEAR_DOWN = BUILDER
                .comment("Can eat with facewear down")
                .define("canEatWithFacewearDown", true);
        BUILDER.pop();



        BUILDER.push("Armor Mechanics");
        ENABLE_DYNAMIC_ARMOR_STATS = BUILDER
                .comment("If true, armor stats scale with durability (100% stats at full repair, 10% stats at broken).",
                        "Also prevents armor from breaking fully (it stays at 0 durability).")
                .define("enableDynamicArmorStats", false);
        BUILDER.pop();

        CONFIG = BUILDER.build();
    }
}