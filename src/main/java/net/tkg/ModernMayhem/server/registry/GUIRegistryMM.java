package net.tkg.ModernMayhem.server.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.GUI.GenericBackpackGUI;

public class GUIRegistryMM {
    public static final DeferredRegister<MenuType<?>> GUIS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModernMayhemMod.ID);

    public static final RegistryObject<MenuType<GenericBackpackGUI>> BACKPACK_GUI = GUIS.register(
            "backpack_gui",
            () -> IForgeMenuType.create(GenericBackpackGUI::new)
    );

    public static void init(IEventBus eventBus) { GUIS.register(eventBus);
    }
}
