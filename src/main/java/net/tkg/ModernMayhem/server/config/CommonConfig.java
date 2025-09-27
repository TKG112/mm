package net.tkg.ModernMayhem.server.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    public static final ForgeConfigSpec.ConfigValue<Boolean> CAN_EAT_WITH_FACEWEAR_DOWN;

    static {
        BUILDER.push("Modern Mayhem Common Config Settings");
        BUILDER.push("Visor & NVG Goggles configs");
        CAN_EAT_WITH_FACEWEAR_DOWN = BUILDER.comment("Can eat with facewear down").define("canEatWithFacewearDown", true);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
}
