
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
				tabData.accept(MmModItems.BLACK_MILITARY_HELMET_HELMET.get());
				tabData.accept(MmModItems.NVG.get());
				tabData.accept(MmModItems.BALACLAVA.get());
				tabData.accept(MmModItems.MILITARY_BALACLAVA.get());
				tabData.accept(MmModItems.MILITARY_GLASSES.get());
				tabData.accept(MmModItems.MILITARY_GOGGLES.get());
				tabData.accept(MmModItems.MILITARY_HEADSET.get());
				tabData.accept(MmModItems.BACKPACK_TIER_1.get());
			})

					.build());
	public static final RegistryObject<CreativeModeTab> GMS_ITEMS = REGISTRY.register("gms_items",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.mm.gms_items")).icon(() -> new ItemStack(MmModItems.FRAG.get())).displayItems((parameters, tabData) -> {
				tabData.accept(MmModItems.FRAG.get());
				tabData.accept(MmModItems.IMPACT.get());
				tabData.accept(MmModItems.MOLOTOV.get());
				tabData.accept(MmModItems.FLASHBANG.get());
				tabData.accept(MmModItems.SMOKE.get());
				tabData.accept(MmModItems.DUFFELBAG.get());
				tabData.accept(MmModItems.SPLINT.get());
				tabData.accept(MmModItems.BANDAGE.get());
				tabData.accept(MmModItems.MEDKIT.get());
			})

					.build());
}
