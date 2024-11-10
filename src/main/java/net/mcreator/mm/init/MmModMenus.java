
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
import net.mcreator.mm.world.inventory.Tier2BackpackMenu;
import net.mcreator.mm.world.inventory.Tier1BackpackGUIMenu;
import net.mcreator.mm.world.inventory.RigGUIMenu;
import net.mcreator.mm.world.inventory.AmmoGUIMenu;
import net.mcreator.mm.MmMod;

public class MmModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MmMod.MODID);
	public static final RegistryObject<MenuType<Tier1BackpackGUIMenu>> TIER_1_BACKPACK_GUI = REGISTRY.register("tier_1_backpack_gui", () -> IForgeMenuType.create(Tier1BackpackGUIMenu::new));
	public static final RegistryObject<MenuType<Tier2BackpackMenu>> TIER_2_BACKPACK = REGISTRY.register("tier_2_backpack", () -> IForgeMenuType.create(Tier2BackpackMenu::new));
	public static final RegistryObject<MenuType<Tier3BackpackGUIMenu>> TIER_3_BACKPACK_GUI = REGISTRY.register("tier_3_backpack_gui", () -> IForgeMenuType.create(Tier3BackpackGUIMenu::new));
	public static final RegistryObject<MenuType<RigGUIMenu>> RIG_GUI = REGISTRY.register("rig_gui", () -> IForgeMenuType.create(RigGUIMenu::new));
	public static final RegistryObject<MenuType<AmmoGUIMenu>> AMMO_GUI = REGISTRY.register("ammo_gui", () -> IForgeMenuType.create(AmmoGUIMenu::new));
}
