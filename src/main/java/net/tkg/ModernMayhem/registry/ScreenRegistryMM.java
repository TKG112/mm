package net.tkg.ModernMayhem.registry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tkg.ModernMayhem.GUI.GenericBackpackGUI;
import net.tkg.ModernMayhem.GUI.TestBackpackGUIMenu;
import net.tkg.ModernMayhem.client.screen.GenericBackpackScreen;
import net.tkg.ModernMayhem.client.screen.TestBackpackScreen;

public class ScreenRegistryMM {

    public static void register(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(GUIRegistryMM.TESTBACKPACK_GUI.get(), TestBackpackScreen::new);
        });
    }
}
