
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.mm.MmMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MmModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MmMod.MODID);
	public static final RegistryObject<CreativeModeTab> GMS = REGISTRY.register("gms",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.mm.gms")).icon(() -> new ItemStack(MmModItems.MENU_ITEM.get())).displayItems((parameters, tabData) -> {
				tabData.accept(MmModItems.BLACK_MILITARY_HELMET_HELMET.get());
				tabData.accept(MmModItems.BALACLAVA.get());
				tabData.accept(MmModItems.MILITARY_BALACLAVA.get());
				tabData.accept(MmModItems.MILITARY_GLASSES.get());
				tabData.accept(MmModItems.MILITARY_GOGGLES.get());
				tabData.accept(MmModItems.NVG.get());
				tabData.accept(MmModItems.MILITARY_HEADSET.get());
				tabData.accept(MmModItems.BLACK_KEVLAR_BODY_CHESTPLATE.get());
				tabData.accept(MmModItems.BLACK_BACKPACK_1.get());
				tabData.accept(MmModItems.BLACK_BACKPACK_2.get());
				tabData.accept(MmModItems.BLACK_BACKPACK_3.get());
				tabData.accept(MmModItems.BLACK_PLATE_CARRIER.get());
				tabData.accept(MmModItems.BLACK_PLATE_CARRIER_AMMO.get());
				tabData.accept(MmModItems.BLACK_PLATE_CARRIER_POUCHES.get());
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

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.COMBAT) {
			tabData.accept(MmModItems.BLACK_KEVLAR_PANTS_LEGGINGS.get());
			tabData.accept(MmModItems.BLACK_KEVLAR_BOOTS_BOOTS.get());
		}
	}
}
