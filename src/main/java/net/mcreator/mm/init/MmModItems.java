
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorItem;

import net.mcreator.mm.item.NVGItem;
import net.mcreator.mm.item.MilitaryHeadsetItem;
import net.mcreator.mm.item.MilitaryGogglesItem;
import net.mcreator.mm.item.MilitaryGlassesItem;
import net.mcreator.mm.item.MilitaryBalaclavaItem;
import net.mcreator.mm.item.MenuItemItem;
import net.mcreator.mm.item.DuffelbagItem;
import net.mcreator.mm.item.BlackMilitaryHelmetItem;
import net.mcreator.mm.item.BalaclavaItem;
import net.mcreator.mm.MmMod;

public class MmModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MmMod.MODID);
	public static final RegistryObject<BlackMilitaryHelmetItem> BLACK_MILITARY_HELMET_HELMET = REGISTRY.register("black_military_helmet_helmet", () -> new BlackMilitaryHelmetItem(ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> NVG = REGISTRY.register("nvg", () -> new NVGItem());
	public static final RegistryObject<Item> DUFFELBAG = REGISTRY.register("duffelbag", () -> new DuffelbagItem());
	public static final RegistryObject<Item> MENU_ITEM = REGISTRY.register("menu_item", () -> new MenuItemItem());
	public static final RegistryObject<Item> BALACLAVA = REGISTRY.register("balaclava", () -> new BalaclavaItem());
	public static final RegistryObject<Item> MILITARY_BALACLAVA = REGISTRY.register("military_balaclava", () -> new MilitaryBalaclavaItem());
	public static final RegistryObject<Item> MILITARY_GLASSES = REGISTRY.register("military_glasses", () -> new MilitaryGlassesItem());
	public static final RegistryObject<Item> MILITARY_GOGGLES = REGISTRY.register("military_goggles", () -> new MilitaryGogglesItem());
	public static final RegistryObject<Item> MILITARY_HEADSET = REGISTRY.register("military_headset", () -> new MilitaryHeadsetItem());
	// Start of user code block custom items
	// End of user code block custom items
}
