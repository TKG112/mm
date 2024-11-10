
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.mm.MmMod;

public class MmModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MmMod.MODID);
	public static final RegistryObject<CreativeModeTab> GMS = REGISTRY.register("gms",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.mm.gms")).icon(() -> new ItemStack(MmModItems.MENU_ITEM.get())).displayItems((parameters, tabData) -> {
				tabData.accept(MmModItems.BLACK_COMBAT_HELMET_HELMET.get());
				tabData.accept(MmModItems.BLACK_KEVLAR_BODY_CHESTPLATE.get());
				tabData.accept(MmModItems.BLACK_KEVLAR_PANTS_LEGGINGS.get());
				tabData.accept(MmModItems.BLACK_KEVLAR_BOOTS_BOOTS.get());
				tabData.accept(MmModItems.RONIN_HELMET.get());
				tabData.accept(MmModItems.HEAD_MOUNT_HELMET.get());
				tabData.accept(MmModItems.BALACLAVA.get());
				tabData.accept(MmModItems.MILITARY_BALACLAVA.get());
				tabData.accept(MmModItems.NVG.get());
				tabData.accept(MmModItems.BLACK_VISOR.get());
				tabData.accept(MmModItems.MILITARY_GLASSES.get());
				tabData.accept(MmModItems.MILITARY_GOGGLES.get());
				tabData.accept(MmModItems.MILITARY_HEADSET.get());
				tabData.accept(MmModItems.BANDOLEER.get());
				tabData.accept(MmModItems.RECON.get());
				tabData.accept(MmModItems.BLACK_PLATE_CARRIER.get());
				tabData.accept(MmModItems.BLACK_PLATE_CARRIER_AMMO.get());
				tabData.accept(MmModItems.BLACK_PLATE_CARRIER_POUCHES.get());
				tabData.accept(MmModItems.BLACK_BACKPACK_1.get());
				tabData.accept(MmModItems.BLACK_BACKPACK_2.get());
				tabData.accept(MmModItems.BLACK_BACKPACK_3.get());
			})

					.build());
	public static final RegistryObject<CreativeModeTab> GMS_ITEMS = REGISTRY.register("gms_items",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.mm.gms_items")).icon(() -> new ItemStack(MmModItems.GRENADE_FRAG.get())).displayItems((parameters, tabData) -> {
				tabData.accept(MmModItems.DUFFELBAG.get());
				tabData.accept(MmModItems.GRENADE_FRAG.get());
				tabData.accept(MmModItems.GRENADE_IMPACT.get());
				tabData.accept(MmModItems.GRENADE_MOLOTOV.get());
				tabData.accept(MmModItems.GRENADE_FLASHBANG.get());
				tabData.accept(MmModItems.GRENADE_SMOKE.get());
			})

					.build());
}
