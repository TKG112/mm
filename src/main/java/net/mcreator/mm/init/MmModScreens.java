
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
import net.mcreator.mm.client.gui.Tier3BackpackGUI2Screen;
import net.mcreator.mm.client.gui.Tier2BackpackGUIScreen;
import net.mcreator.mm.client.gui.Tier2BackpackGUI2Screen;
import net.mcreator.mm.client.gui.Tier1BackpackGUIScreen;
import net.mcreator.mm.client.gui.Tier1BackpackGUI2Screen;
import net.mcreator.mm.client.gui.RigGUIScreen;
import net.mcreator.mm.client.gui.RigGUI2Screen;
import net.mcreator.mm.client.gui.ReconGUIScreen;
import net.mcreator.mm.client.gui.ReconGUI2Screen;
import net.mcreator.mm.client.gui.BandoleerGUIScreen;
import net.mcreator.mm.client.gui.BandoleerGUI2Screen;
import net.mcreator.mm.client.gui.AmmoGUIScreen;
import net.mcreator.mm.client.gui.AmmoGUI2Screen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MmModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(MmModMenus.TIER_1_BACKPACK_GUI.get(), Tier1BackpackGUIScreen::new);
			MenuScreens.register(MmModMenus.TIER_3_BACKPACK_GUI.get(), Tier3BackpackGUIScreen::new);
			MenuScreens.register(MmModMenus.RIG_GUI.get(), RigGUIScreen::new);
			MenuScreens.register(MmModMenus.AMMO_GUI.get(), AmmoGUIScreen::new);
			MenuScreens.register(MmModMenus.BANDOLEER_GUI.get(), BandoleerGUIScreen::new);
			MenuScreens.register(MmModMenus.RECON_GUI.get(), ReconGUIScreen::new);
			MenuScreens.register(MmModMenus.TIER_2_BACKPACK_GUI.get(), Tier2BackpackGUIScreen::new);
			MenuScreens.register(MmModMenus.TIER_1_BACKPACK_GUI_2.get(), Tier1BackpackGUI2Screen::new);
			MenuScreens.register(MmModMenus.TIER_2_BACKPACK_GUI_2.get(), Tier2BackpackGUI2Screen::new);
			MenuScreens.register(MmModMenus.TIER_3_BACKPACK_GUI_2.get(), Tier3BackpackGUI2Screen::new);
			MenuScreens.register(MmModMenus.AMMO_GUI_2.get(), AmmoGUI2Screen::new);
			MenuScreens.register(MmModMenus.BANDOLEER_GUI_2.get(), BandoleerGUI2Screen::new);
			MenuScreens.register(MmModMenus.RECON_GUI_2.get(), ReconGUI2Screen::new);
			MenuScreens.register(MmModMenus.RIG_GUI_2.get(), RigGUI2Screen::new);
		});
	}
}
