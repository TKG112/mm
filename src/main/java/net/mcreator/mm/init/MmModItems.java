
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

import net.mcreator.mm.item.TanVisorItem;
import net.mcreator.mm.item.TanNVG21Item;
import net.mcreator.mm.item.TanGPNVGItem;
import net.mcreator.mm.item.RoninItem;
import net.mcreator.mm.item.ReconItem;
import net.mcreator.mm.item.MilitaryHeadsetItem;
import net.mcreator.mm.item.MilitaryGogglesItem;
import net.mcreator.mm.item.MilitaryGlassesItem;
import net.mcreator.mm.item.MilitaryBalaclavaItem;
import net.mcreator.mm.item.MenuItemItem;
import net.mcreator.mm.item.HeadMountItem;
import net.mcreator.mm.item.GrenadeSmokeItem;
import net.mcreator.mm.item.GrenadeMolotovItem;
import net.mcreator.mm.item.GrenadeImpactItem;
import net.mcreator.mm.item.GrenadeFragItem;
import net.mcreator.mm.item.GrenadeFlashbangItem;
import net.mcreator.mm.item.GreenSSH68Item;
import net.mcreator.mm.item.GreenNVG21Item;
import net.mcreator.mm.item.GreenKevlarPantsItem;
import net.mcreator.mm.item.GreenKevlarBootsItem;
import net.mcreator.mm.item.GreenKevlarBodyItem;
import net.mcreator.mm.item.GreenIOLAItem;
import net.mcreator.mm.item.GreenBackpack3Item;
import net.mcreator.mm.item.GreenBackpack2Item;
import net.mcreator.mm.item.GreenBackpack1Item;
import net.mcreator.mm.item.DuffelbagItem;
import net.mcreator.mm.item.BlackVisorItem;
import net.mcreator.mm.item.BlackPlateCarrierPouchesItem;
import net.mcreator.mm.item.BlackPlateCarrierItem;
import net.mcreator.mm.item.BlackPlateCarrierAmmoItem;
import net.mcreator.mm.item.BlackNVG21Item;
import net.mcreator.mm.item.BlackKevlarPantsItem;
import net.mcreator.mm.item.BlackKevlarBootsItem;
import net.mcreator.mm.item.BlackKevlarBodyItem;
import net.mcreator.mm.item.BlackIOLAItem;
import net.mcreator.mm.item.BlackGPNVGItem;
import net.mcreator.mm.item.BlackCombatHelmetItem;
import net.mcreator.mm.item.BlackBackpack3Item;
import net.mcreator.mm.item.BlackBackpack2Item;
import net.mcreator.mm.item.BlackBackpack1Item;
import net.mcreator.mm.item.BandoleerItem;
import net.mcreator.mm.item.BalaclavaItem;
import net.mcreator.mm.MmMod;

public class MmModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MmMod.MODID);
	public static final RegistryObject<Item> MENU_ITEM = REGISTRY.register("menu_item", () -> new MenuItemItem());
	public static final RegistryObject<Item> BALACLAVA = REGISTRY.register("balaclava", () -> new BalaclavaItem());
	public static final RegistryObject<Item> MILITARY_BALACLAVA = REGISTRY.register("military_balaclava", () -> new MilitaryBalaclavaItem());
	public static final RegistryObject<Item> MILITARY_GLASSES = REGISTRY.register("military_glasses", () -> new MilitaryGlassesItem());
	public static final RegistryObject<Item> MILITARY_GOGGLES = REGISTRY.register("military_goggles", () -> new MilitaryGogglesItem());
	public static final RegistryObject<Item> MILITARY_HEADSET = REGISTRY.register("military_headset", () -> new MilitaryHeadsetItem());
	public static final RegistryObject<Item> DUFFELBAG = REGISTRY.register("duffelbag", () -> new DuffelbagItem());
	public static final RegistryObject<Item> DUFFELBAG_BLOCK = block(MmModBlocks.DUFFELBAG_BLOCK);
	public static final RegistryObject<Item> BLACK_BACKPACK_1 = REGISTRY.register("black_backpack_1", () -> new BlackBackpack1Item());
	public static final RegistryObject<Item> BLACK_BACKPACK_2 = REGISTRY.register("black_backpack_2", () -> new BlackBackpack2Item());
	public static final RegistryObject<Item> BLACK_BACKPACK_3 = REGISTRY.register("black_backpack_3", () -> new BlackBackpack3Item());
	public static final RegistryObject<Item> BLACK_PLATE_CARRIER = REGISTRY.register("black_plate_carrier", () -> new BlackPlateCarrierItem());
	public static final RegistryObject<Item> BLACK_PLATE_CARRIER_AMMO = REGISTRY.register("black_plate_carrier_ammo", () -> new BlackPlateCarrierAmmoItem());
	public static final RegistryObject<Item> BLACK_PLATE_CARRIER_POUCHES = REGISTRY.register("black_plate_carrier_pouches", () -> new BlackPlateCarrierPouchesItem());
	public static final RegistryObject<BlackKevlarBootsItem> BLACK_KEVLAR_BOOTS_BOOTS = REGISTRY.register("black_kevlar_boots_boots", () -> new BlackKevlarBootsItem(ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistryObject<Item> GRENADE_FRAG = REGISTRY.register("grenade_frag", () -> new GrenadeFragItem());
	public static final RegistryObject<Item> GRENADE_IMPACT = REGISTRY.register("grenade_impact", () -> new GrenadeImpactItem());
	public static final RegistryObject<Item> GRENADE_MOLOTOV = REGISTRY.register("grenade_molotov", () -> new GrenadeMolotovItem());
	public static final RegistryObject<Item> GRENADE_FLASHBANG = REGISTRY.register("grenade_flashbang", () -> new GrenadeFlashbangItem());
	public static final RegistryObject<Item> GRENADE_SMOKE = REGISTRY.register("grenade_smoke", () -> new GrenadeSmokeItem());
	public static final RegistryObject<RoninItem> RONIN_HELMET = REGISTRY.register("ronin_helmet", () -> new RoninItem(ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<HeadMountItem> HEAD_MOUNT_HELMET = REGISTRY.register("head_mount_helmet", () -> new HeadMountItem(ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> BANDOLEER = REGISTRY.register("bandoleer", () -> new BandoleerItem());
	public static final RegistryObject<Item> RECON = REGISTRY.register("recon", () -> new ReconItem());
	public static final RegistryObject<Item> BLACK_VISOR = REGISTRY.register("black_visor", () -> new BlackVisorItem());
	public static final RegistryObject<Item> BLACK_NVG_21 = REGISTRY.register("black_nvg_21", () -> new BlackNVG21Item());
	public static final RegistryObject<Item> BLACK_GPNVG = REGISTRY.register("black_gpnvg", () -> new BlackGPNVGItem());
	public static final RegistryObject<BlackIOLAItem> BLACK_IOLA_LEGGINGS = REGISTRY.register("black_iola_leggings", () -> new BlackIOLAItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<BlackCombatHelmetItem> BLACK_COMBAT_HELMET_HELMET = REGISTRY.register("black_combat_helmet_helmet", () -> new BlackCombatHelmetItem(ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<BlackKevlarBodyItem> BLACK_KEVLAR_BODY_CHESTPLATE = REGISTRY.register("black_kevlar_body_chestplate", () -> new BlackKevlarBodyItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<BlackKevlarPantsItem> BLACK_KEVLAR_PANTS_LEGGINGS = REGISTRY.register("black_kevlar_pants_leggings", () -> new BlackKevlarPantsItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> GREEN_NVG_21 = REGISTRY.register("green_nvg_21", () -> new GreenNVG21Item());
	public static final RegistryObject<Item> TAN_GPNVG = REGISTRY.register("tan_gpnvg", () -> new TanGPNVGItem());
	public static final RegistryObject<Item> TAN_NVG_21 = REGISTRY.register("tan_nvg_21", () -> new TanNVG21Item());
	public static final RegistryObject<Item> TAN_VISOR = REGISTRY.register("tan_visor", () -> new TanVisorItem());
	public static final RegistryObject<GreenKevlarBodyItem> GREEN_KEVLAR_BODY_CHESTPLATE = REGISTRY.register("green_kevlar_body_chestplate", () -> new GreenKevlarBodyItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<GreenKevlarBootsItem> GREEN_KEVLAR_BOOTS_BOOTS = REGISTRY.register("green_kevlar_boots_boots", () -> new GreenKevlarBootsItem(ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistryObject<GreenKevlarPantsItem> GREEN_KEVLAR_PANTS_LEGGINGS = REGISTRY.register("green_kevlar_pants_leggings", () -> new GreenKevlarPantsItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<GreenIOLAItem> GREEN_IOLA_LEGGINGS = REGISTRY.register("green_iola_leggings", () -> new GreenIOLAItem(ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<GreenSSH68Item> GREEN_SSH_68_HELMET = REGISTRY.register("green_ssh_68_helmet", () -> new GreenSSH68Item(ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> GREEN_BACKPACK_1 = REGISTRY.register("green_backpack_1", () -> new GreenBackpack1Item());
	public static final RegistryObject<Item> GREEN_BACKPACK_2 = REGISTRY.register("green_backpack_2", () -> new GreenBackpack2Item());
	public static final RegistryObject<Item> GREEN_BACKPACK_3 = REGISTRY.register("green_backpack_3", () -> new GreenBackpack3Item());

	// Start of user code block custom items
	// End of user code block custom items
	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
