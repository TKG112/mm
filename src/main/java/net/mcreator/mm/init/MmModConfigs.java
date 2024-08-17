package net.mcreator.mm.init;

import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.mcreator.mm.configuration.ModernMayhemConfiguration;
import net.mcreator.mm.MmMod;

@Mod.EventBusSubscriber(modid = MmMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MmModConfigs {
	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModernMayhemConfiguration.SPEC, "modernmayhem.toml");
		});
	}
}
