
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ArmorItem;

import net.mcreator.mm.item.SplintItem;
import net.mcreator.mm.item.SmokeItem;
import net.mcreator.mm.item.NVGItem;
import net.mcreator.mm.item.MolotovItem;
import net.mcreator.mm.item.MilitaryHeadsetItem;
import net.mcreator.mm.item.MilitaryGogglesItem;
import net.mcreator.mm.item.MilitaryGlassesItem;
import net.mcreator.mm.item.MilitaryBalaclavaItem;
import net.mcreator.mm.item.MenuItemItem;
import net.mcreator.mm.item.MedkitItem;
import net.mcreator.mm.item.ImpactItem;
import net.mcreator.mm.item.FragItem;
import net.mcreator.mm.item.FlashbangItem;
import net.mcreator.mm.item.DuffelbagItem;
import net.mcreator.mm.item.BlackPlateCarrierPouchesItem;
import net.mcreator.mm.item.BlackPlateCarrierItem;
import net.mcreator.mm.item.BlackPlateCarrierAmmoItem;
import net.mcreator.mm.item.BlackMilitaryHelmetItem;
import net.mcreator.mm.item.BlackKevlarPantsItem;
import net.mcreator.mm.item.BlackKevlarBootsItem;
import net.mcreator.mm.item.BlackKevlarBodyItem;
import net.mcreator.mm.item.BlackBackpack3Item;
import net.mcreator.mm.item.BlackBackpack2Item;
import net.mcreator.mm.item.BlackBackpack1Item;
import net.mcreator.mm.item.BandageItem;
import net.mcreator.mm.item.BalaclavaItem;
import net.mcreator.mm.MmMod;

public class MmModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MmMod.MODID);
	public static final RegistryObject<Item> NVG = REGISTRY.register("nvg", () -> new NVGItem());
	public static final RegistryObject<Item> MENU_ITEM = REGISTRY.register("menu_item", () -> new MenuItemItem());
	public static final RegistryObject<Item> BALACLAVA = REGISTRY.register("balaclava", () -> new BalaclavaItem());
	public static final RegistryObject<Item> MILITARY_BALACLAVA = REGISTRY.register("military_balaclava", () -> new MilitaryBalaclavaItem());
	public static final RegistryObject<Item> MILITARY_GLASSES = REGISTRY.register("military_glasses", () -> new MilitaryGlassesItem());
	public static final RegistryObject<Item> MILITARY_GOGGLES = REGISTRY.register("military_goggles", () -> new MilitaryGogglesItem());
	public static final RegistryObject<Item> MILITARY_HEADSET = REGISTRY.register("military_headset", () -> new MilitaryHeadsetItem());
	public static final RegistryObject<Item> FRAG = REGISTRY.register("frag", () -> new FragItem());
	public static final RegistryObject<Item> IMPACT = REGISTRY.register("impact", () -> new ImpactItem());
	public static final RegistryObject<Item> MOLOTOV = REGISTRY.register("molotov", () -> new MolotovItem());
	public static final RegistryObject<Item> FLASHBANG = REGISTRY.register("flashbang", () -> new FlashbangItem());
	public static final RegistryObject<Item> SMOKE = REGISTRY.register("smoke", () -> new SmokeItem());
	public static final RegistryObject<Item> DUFFELBAG = REGISTRY.register("duffelbag", () -> new DuffelbagItem());
	public static final RegistryObject<Item> DUFFELBAG_BLOCK = block(MmModBlocks.DUFFELBAG_BLOCK);
	public static final RegistryObject<Item> SPLINT = REGISTRY.register("splint", () -> new SplintItem());
	public static final RegistryObject<Item> BANDAGE = REGISTRY.register("bandage", () -> new BandageItem());
	public static final RegistryObject<Item> MEDKIT = REGISTRY.register("medkit", () -> new MedkitItem());
	public static final RegistryObject<BlackMilitaryHelmetItem> BLACK_MILITARY_HELMET_HELMET = REGISTRY.register("black_military_helmet_helmet", () -> new BlackMilitaryHelmetItem(ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<BlackKevlarBodyItem> BLACK_KEVLAR_BODY_CHESTPLATE = REGISTRY.register("black_kevlar_body_chestplate", () -> new BlackKevlarBodyItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<BlackKevlarPantsItem> BLACK_KEVLAR_PANTS_LEGGINGS = REGISTRY.register("black_kevlar_pants_leggings", () -> new BlackKevlarPantsItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<BlackKevlarBootsItem> BLACK_KEVLAR_BOOTS_BOOTS = REGISTRY.register("black_kevlar_boots_boots", () -> new BlackKevlarBootsItem(ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistryObject<Item> BLACK_BACKPACK_1 = REGISTRY.register("black_backpack_1", () -> new BlackBackpack1Item());
	public static final RegistryObject<Item> BLACK_BACKPACK_2 = REGISTRY.register("black_backpack_2", () -> new BlackBackpack2Item());
	public static final RegistryObject<Item> BLACK_BACKPACK_3 = REGISTRY.register("black_backpack_3", () -> new BlackBackpack3Item());
	public static final RegistryObject<Item> BLACK_PLATE_CARRIER = REGISTRY.register("black_plate_carrier", () -> new BlackPlateCarrierItem());
	public static final RegistryObject<Item> BLACK_PLATE_CARRIER_AMMO = REGISTRY.register("black_plate_carrier_ammo", () -> new BlackPlateCarrierAmmoItem());
	public static final RegistryObject<Item> BLACK_PLATE_CARRIER_POUCHES = REGISTRY.register("black_plate_carrier_pouches", () -> new BlackPlateCarrierPouchesItem());

	// Start of user code block custom items
	// End of user code block custom items
	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
