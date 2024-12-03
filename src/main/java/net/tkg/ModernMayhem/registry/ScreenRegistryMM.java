package net.tkg.ModernMayhem.registry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tkg.ModernMayhem.client.screen.BackpackT1Screen;
import net.tkg.ModernMayhem.client.screen.BackpackT2Screen;
import net.tkg.ModernMayhem.client.screen.BackpackT3Screen;
import net.tkg.ModernMayhem.client.screen.TestBackpackScreen;

public class ScreenRegistryMM {

    public static void register(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(GUIRegistryMM.TESTBACKPACK_GUI.get(), TestBackpackScreen::new);
            MenuScreens.register(GUIRegistryMM.BACKPACKT1_GUI.get(), BackpackT1Screen::new);
            MenuScreens.register(GUIRegistryMM.BACKPACKT2_GUI.get(), BackpackT2Screen::new);
            MenuScreens.register(GUIRegistryMM.BACKPACKT3_GUI.get(), BackpackT3Screen::new);
        });
    }
}
