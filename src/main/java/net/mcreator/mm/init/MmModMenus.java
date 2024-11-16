
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import net.mcreator.mm.world.inventory.Tier3BackpackGUIMenu;
import net.mcreator.mm.world.inventory.Tier3BackpackGUI2Menu;
import net.mcreator.mm.world.inventory.Tier2BackpackGUIMenu;
import net.mcreator.mm.world.inventory.Tier2BackpackGUI2Menu;
import net.mcreator.mm.world.inventory.Tier1BackpackGUIMenu;
import net.mcreator.mm.world.inventory.Tier1BackpackGUI2Menu;
import net.mcreator.mm.world.inventory.RigGUIMenu;
import net.mcreator.mm.world.inventory.RigGUI2Menu;
import net.mcreator.mm.world.inventory.ReconGUIMenu;
import net.mcreator.mm.world.inventory.ReconGUI2Menu;
import net.mcreator.mm.world.inventory.BandoleerGUIMenu;
import net.mcreator.mm.world.inventory.BandoleerGUI2Menu;
import net.mcreator.mm.world.inventory.AmmoGUIMenu;
import net.mcreator.mm.world.inventory.AmmoGUI2Menu;
import net.mcreator.mm.MmMod;

public class MmModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MmMod.MODID);
	public static final RegistryObject<MenuType<Tier1BackpackGUIMenu>> TIER_1_BACKPACK_GUI = REGISTRY.register("tier_1_backpack_gui", () -> IForgeMenuType.create(Tier1BackpackGUIMenu::new));
	public static final RegistryObject<MenuType<Tier3BackpackGUIMenu>> TIER_3_BACKPACK_GUI = REGISTRY.register("tier_3_backpack_gui", () -> IForgeMenuType.create(Tier3BackpackGUIMenu::new));
	public static final RegistryObject<MenuType<RigGUIMenu>> RIG_GUI = REGISTRY.register("rig_gui", () -> IForgeMenuType.create(RigGUIMenu::new));
	public static final RegistryObject<MenuType<AmmoGUIMenu>> AMMO_GUI = REGISTRY.register("ammo_gui", () -> IForgeMenuType.create(AmmoGUIMenu::new));
	public static final RegistryObject<MenuType<BandoleerGUIMenu>> BANDOLEER_GUI = REGISTRY.register("bandoleer_gui", () -> IForgeMenuType.create(BandoleerGUIMenu::new));
	public static final RegistryObject<MenuType<ReconGUIMenu>> RECON_GUI = REGISTRY.register("recon_gui", () -> IForgeMenuType.create(ReconGUIMenu::new));
	public static final RegistryObject<MenuType<Tier2BackpackGUIMenu>> TIER_2_BACKPACK_GUI = REGISTRY.register("tier_2_backpack_gui", () -> IForgeMenuType.create(Tier2BackpackGUIMenu::new));
	public static final RegistryObject<MenuType<Tier1BackpackGUI2Menu>> TIER_1_BACKPACK_GUI_2 = REGISTRY.register("tier_1_backpack_gui_2", () -> IForgeMenuType.create(Tier1BackpackGUI2Menu::new));
	public static final RegistryObject<MenuType<Tier2BackpackGUI2Menu>> TIER_2_BACKPACK_GUI_2 = REGISTRY.register("tier_2_backpack_gui_2", () -> IForgeMenuType.create(Tier2BackpackGUI2Menu::new));
	public static final RegistryObject<MenuType<Tier3BackpackGUI2Menu>> TIER_3_BACKPACK_GUI_2 = REGISTRY.register("tier_3_backpack_gui_2", () -> IForgeMenuType.create(Tier3BackpackGUI2Menu::new));
	public static final RegistryObject<MenuType<AmmoGUI2Menu>> AMMO_GUI_2 = REGISTRY.register("ammo_gui_2", () -> IForgeMenuType.create(AmmoGUI2Menu::new));
	public static final RegistryObject<MenuType<BandoleerGUI2Menu>> BANDOLEER_GUI_2 = REGISTRY.register("bandoleer_gui_2", () -> IForgeMenuType.create(BandoleerGUI2Menu::new));
	public static final RegistryObject<MenuType<ReconGUI2Menu>> RECON_GUI_2 = REGISTRY.register("recon_gui_2", () -> IForgeMenuType.create(ReconGUI2Menu::new));
	public static final RegistryObject<MenuType<RigGUI2Menu>> RIG_GUI_2 = REGISTRY.register("rig_gui_2", () -> IForgeMenuType.create(RigGUI2Menu::new));
}
