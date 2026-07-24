package net.tkg.ModernMayhem.server.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.content.DataDrivenContent;

public class CreativeTabsRegistryMM {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModernMayhemMod.ID);

    public static final RegistryObject<CreativeModeTab> MM = TABS.register(
            "mm",
            () -> CreativeModeTab.builder().
                    title(Component.translatable("item_group."+ModernMayhemMod.ID))
                    .icon(() -> new ItemStack(ItemRegistryMM.MENU_ITEM.get())).displayItems( ((pParameters, pOutput) -> {
                        pOutput.accept(ItemRegistryMM.COTI.get());
                        pOutput.accept(ItemRegistryMM.DUFFEL_BAG.get());
                        pOutput.accept(ItemRegistryMM.WHITE_PHOSPHOR.get());
                        pOutput.accept(ItemRegistryMM.GREEN_PHOSPHOR.get());
                        pOutput.accept(ItemRegistryMM.RED_PHOSPHOR.get());

                        DataDrivenContent.registeredItems().forEach(pOutput::accept);

                    })).build()
            );

    public static void init(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
