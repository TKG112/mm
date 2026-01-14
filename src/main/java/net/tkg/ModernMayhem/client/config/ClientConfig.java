package net.tkg.ModernMayhem.client.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.tkg.ModernMayhem.client.Darkness;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    public static final ForgeConfigSpec.EnumValue<Darkness.DarkMode> DARKNESS_MODE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DARKNESS_ON_OVERWORLD;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DARKNESS_ON_NETHER;
    public static final ForgeConfigSpec.DoubleValue DARKNESS_NETHER_FOG_BRIGHT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DARKNESS_ON_END;
    public static final ForgeConfigSpec.DoubleValue DARKNESS_END_FOG_BRIGHT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DARKNESS_BY_DEFAULT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DARKNESS_ON_NO_SKY_LIGHT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DARKNESS_BLOCK_LIGHT_ONLY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DARKNESS_ON_FULL_BRIGHT_BIOMES;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DARKNESS_AFFECTED_BY_MOON_PHASE;
    public static final ForgeConfigSpec.DoubleValue DARKNESS_NEW_MOON_BRIGHT;
    public static final ForgeConfigSpec.DoubleValue DARKNESS_FULL_MOON_BRIGHT;

    //public static final ForgeConfigSpec.ConfigValue<Boolean> REALISTIC_MASK_MODE;

    static {
        BUILDER.push("Modern Mayhem Client Config Settings");

        BUILDER.push("True Darkness Settings");
        DARKNESS_MODE = BUILDER
                .comment("Mode of darkness to apply. Options: VANILLA, DIM, DARK, DARKNESS, BLACK, BLACKNESS")
                .defineEnum("darknessMode", Darkness.DarkMode.VANILLA);

        BUILDER.push("Dimensions");
        DARKNESS_ON_OVERWORLD = BUILDER
                .comment("Apply darkness to the Overworld")
                .define("darknessOnOverworld", true);
        DARKNESS_ON_NETHER = BUILDER
                .comment("Apply darkness to the Nether")
                .define("darknessOnNether", false);
        DARKNESS_NETHER_FOG_BRIGHT = BUILDER
                .comment("Fog brightness in the Nether when darkness is enabled (0.0 to 1.0)")
                .defineInRange("darknessNetherFogBright", 0.5, 0.0, 1.0);
        DARKNESS_ON_END = BUILDER
                .comment("Apply darkness to the End")
                .define("darknessOnEnd", false);
        DARKNESS_END_FOG_BRIGHT = BUILDER
                .comment("Fog brightness in the End when darkness is enabled (0.0 to 1.0)")
                .defineInRange("darknessEndFogBright", 0.5, 0.0, 1.0);
        DARKNESS_BY_DEFAULT = BUILDER
                .comment("Apply darkness to dimensions with sky light by default")
                .define("darknessByDefault", false);
        DARKNESS_ON_NO_SKY_LIGHT = BUILDER
                .comment("Apply darkness to dimensions without sky light")
                .define("darknessOnNoSkyLight", false);
        BUILDER.pop();

        BUILDER.push("Sky & Rendering");
        DARKNESS_BLOCK_LIGHT_ONLY = BUILDER
                .comment("If true, only modifies block light, ignoring sky light calculation")
                .define("darknessBlockLightOnly", false);
        DARKNESS_ON_FULL_BRIGHT_BIOMES = BUILDER
                .comment("Apply darkness in biomes that are naturally full bright")
                .define("darknessOnFullBrightBiomes", false);
        DARKNESS_AFFECTED_BY_MOON_PHASE = BUILDER
                .comment("If true, darkness brightness is affected by the moon phase")
                .define("darknessAffectedByMoonPhase", true);
        DARKNESS_NEW_MOON_BRIGHT = BUILDER
                .comment("Brightness factor during a new moon (0.0 to 1.0)")
                .defineInRange("darknessNewMoonBright", 0.0, 0.0, 1.0);
        DARKNESS_FULL_MOON_BRIGHT = BUILDER
                .comment("Brightness factor during a full moon (0.0 to 1.0)")
                .defineInRange("darknessFullMoonBright", 0.25, 0.0, 1.0);
        BUILDER.pop();
        BUILDER.pop();


        /*
        BUILDER.push("Night Vision Settings");
        REALISTIC_MASK_MODE = BUILDER
                .comment("If true, NVGs will use a realistic viewport mask texture (blacking out the peripheral vision) instead of a fullscreen overlay.")
                .define("realisticMaskMode", false);
        BUILDER.pop();
        */

        CONFIG = BUILDER.build();
    }
}