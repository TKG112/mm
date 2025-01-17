package net.tkg.ModernMayhem.registry;

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

    public static final RegistryObject<CreativeModeTab> GMS = TABS.register(
            "gms",
            () -> CreativeModeTab.builder().
                    title(Component.translatable("item_group."+ModernMayhemMod.ID+".gms"))
                    .icon(() -> new ItemStack(ItemRegistryMM.MENU_ITEM.get())).displayItems( ((pParameters, pOutput) -> {
                        pOutput.accept(ItemRegistryMM.BLACK_GPNVG.get());
                        pOutput.accept(ItemRegistryMM.TAN_GPNVG.get());
                        pOutput.accept(ItemRegistryMM.ULTRA_GAMER_GPNVG.get());
                        pOutput.accept(ItemRegistryMM.BLACK_NVG21.get());
                        pOutput.accept(ItemRegistryMM.TAN_NVG21.get());
                        pOutput.accept(ItemRegistryMM.GREEN_NVG21.get());
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

                    })).build()
            );

    public static void init(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
