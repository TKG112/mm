package net.tkg.ModernMayhem.server.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;

public class CreativeTabsRegistryMM {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModernMayhemMod.ID);

    public static final RegistryObject<CreativeModeTab> MM = TABS.register(
            "mm",
            () -> CreativeModeTab.builder().
                    title(Component.translatable("item_group."+ModernMayhemMod.ID))
                    .icon(() -> new ItemStack(ItemRegistryMM.MENU_ITEM.get())).displayItems( ((pParameters, pOutput) -> {
                        pOutput.accept(ItemRegistryMM.BLACK_GPNVG.get());
                        pOutput.accept(ItemRegistryMM.TAN_GPNVG.get());
                        pOutput.accept(ItemRegistryMM.ULTRA_GAMER_GPNVG.get());
                        pOutput.accept(ItemRegistryMM.BLACK_PVS14.get());
                        pOutput.accept(ItemRegistryMM.TAN_PVS14.get());
                        pOutput.accept(ItemRegistryMM.GREEN_PVS14.get());
                        pOutput.accept(ItemRegistryMM.BLACK_PVS7.get());
                        pOutput.accept(ItemRegistryMM.BLACK_BACKPACK_T1.get());
                        pOutput.accept(ItemRegistryMM.BLACK_BACKPACK_T2.get());
                        pOutput.accept(ItemRegistryMM.BLACK_BACKPACK_T3.get());
                        pOutput.accept(ItemRegistryMM.GREEN_BACKPACK_T1.get());
                        pOutput.accept(ItemRegistryMM.GREEN_BACKPACK_T2.get());
                        pOutput.accept(ItemRegistryMM.GREEN_BACKPACK_T3.get());
                        pOutput.accept(ItemRegistryMM.TAN_BACKPACK_T1.get());
                        pOutput.accept(ItemRegistryMM.TAN_BACKPACK_T2.get());
                        pOutput.accept(ItemRegistryMM.TAN_BACKPACK_T3.get());
                        pOutput.accept(ItemRegistryMM.BLACK_PLATE_CARRIER.get());
                        pOutput.accept(ItemRegistryMM.BLACK_PLATE_CARRIER_AMMO.get());
                        pOutput.accept(ItemRegistryMM.BLACK_PLATE_CARRIER_POUCHES.get());
                        pOutput.accept(ItemRegistryMM.TAN_PLATE_CARRIER.get());
                        pOutput.accept(ItemRegistryMM.TAN_PLATE_CARRIER_AMMO.get());
                        pOutput.accept(ItemRegistryMM.TAN_PLATE_CARRIER_POUCHES.get());
                        pOutput.accept(ItemRegistryMM.TAN_BANDOLEER.get());
                        pOutput.accept(ItemRegistryMM.GREEN_RECON.get());
                        pOutput.accept(ItemRegistryMM.BLACK_COMBAT_HELMET.get());
                        pOutput.accept(ItemRegistryMM.BLACK_KEVLAR_CHESTPLATE.get());
                        pOutput.accept(ItemRegistryMM.BLACK_KEVLAR_LEGGINGS.get());
                        pOutput.accept(ItemRegistryMM.BLACK_KEVLAR_BOOTS.get());
                        pOutput.accept(ItemRegistryMM.GREEN_SSH68_HELMET.get());
                        pOutput.accept(ItemRegistryMM.GREEN_KEVLAR_CHESTPLATE.get());
                        pOutput.accept(ItemRegistryMM.GREEN_KEVLAR_LEGGINGS.get());
                        pOutput.accept(ItemRegistryMM.GREEN_KEVLAR_BOOTS.get());
                        pOutput.accept(ItemRegistryMM.TAN_COMBAT_HELMET.get());
                        pOutput.accept(ItemRegistryMM.TAN_KEVLAR_CHESTPLATE.get());
                        pOutput.accept(ItemRegistryMM.TAN_KEVLAR_LEGGINGS.get());
                        pOutput.accept(ItemRegistryMM.TAN_KEVLAR_BOOTS.get());
                        pOutput.accept(ItemRegistryMM.BLACK_HEAD_MOUNT.get());
                        pOutput.accept(ItemRegistryMM.BLACK_RONIN.get());
                        pOutput.accept(ItemRegistryMM.BALACLAVA.get());
                        pOutput.accept(ItemRegistryMM.BLACK_GLASSES.get());
                        pOutput.accept(ItemRegistryMM.BLACK_GOGGLES.get());
                        pOutput.accept(ItemRegistryMM.BLACK_HEADSET.get());
                        pOutput.accept(ItemRegistryMM.BLACK_MILITARY_BALACLAVA.get());
                        pOutput.accept(ItemRegistryMM.DUFFEL_BAG.get());
                        pOutput.accept(ItemRegistryMM.WHITE_PHOSPHOR.get());
                        pOutput.accept(ItemRegistryMM.GREEN_PHOSPHOR.get());
                        pOutput.accept(ItemRegistryMM.RED_PHOSPHOR.get());


                    })).build()
            );

    public static void init(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
