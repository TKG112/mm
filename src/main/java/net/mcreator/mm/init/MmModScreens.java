
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

import net.mcreator.mm.client.gui.Tier3BackpackGUIScreen;
import net.mcreator.mm.client.gui.Tier2BackpackScreen;
import net.mcreator.mm.client.gui.Tier1BackpackGUIScreen;
import net.mcreator.mm.client.gui.RigGUIScreen;
import net.mcreator.mm.client.gui.AmmoGUIScreen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MmModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(MmModMenus.TIER_1_BACKPACK_GUI.get(), Tier1BackpackGUIScreen::new);
			MenuScreens.register(MmModMenus.TIER_2_BACKPACK.get(), Tier2BackpackScreen::new);
			MenuScreens.register(MmModMenus.TIER_3_BACKPACK_GUI.get(), Tier3BackpackGUIScreen::new);
			MenuScreens.register(MmModMenus.RIG_GUI.get(), RigGUIScreen::new);
			MenuScreens.register(MmModMenus.AMMO_GUI.get(), AmmoGUIScreen::new);
		});
	}
}
