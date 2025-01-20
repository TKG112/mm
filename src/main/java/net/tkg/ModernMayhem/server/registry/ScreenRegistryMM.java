package net.tkg.ModernMayhem.server.registry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tkg.ModernMayhem.client.screen.BackpackScreen;

public class ScreenRegistryMM {

    public static void register(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(GUIRegistryMM.BACKPACK_GUI.get(), BackpackScreen::new);
        });
    }
}
