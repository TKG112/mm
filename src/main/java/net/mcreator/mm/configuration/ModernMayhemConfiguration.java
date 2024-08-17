package net.mcreator.mm.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModernMayhemConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> GRENADES_BREAK_BLOCKS;
	static {
		BUILDER.push("Grenades");
		GRENADES_BREAK_BLOCKS = BUILDER.define("Grenades Break Blocks", true);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
