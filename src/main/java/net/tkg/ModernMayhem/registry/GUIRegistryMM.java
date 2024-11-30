package net.tkg.ModernMayhem.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.GUI.TestBackpackGUIMenu;
import net.tkg.ModernMayhem.ModernMayhemMod;

public class GUIRegistryMM {
    public static final DeferredRegister<MenuType<?>> GUIS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModernMayhemMod.ID);

    public static final RegistryObject<MenuType<TestBackpackGUIMenu>>  TESTBACKPACK_GUI = GUIS.register(
            "testbackpack_gui",
            () -> IForgeMenuType.create(TestBackpackGUIMenu::new)
    );

    public static void init(IEventBus eventBus) { GUIS.register(eventBus);
    }
}
