package net.tkg.ModernMayhem.registry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tkg.ModernMayhem.client.screen.TestBackpackScreen;

public class ScreenRegistryMM {

    public static void register(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(GUIRegistryMM.TESTBACKPACK_GUI.get(), TestBackpackScreen::new);
        });
    }
}
