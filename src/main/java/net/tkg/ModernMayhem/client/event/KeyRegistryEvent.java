package net.tkg.ModernMayhem.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.registry.KeyMappingRegistryMM;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyRegistryEvent {

    @SubscribeEvent
    public static void onKeyRegisterEvent(RegisterKeyMappingsEvent event) {
        event.register(KeyMappingRegistryMM.TOGGLE_NVG_KEY);
        event.register(KeyMappingRegistryMM.INCREASE_TUBE_GAIN_KEY);
        event.register(KeyMappingRegistryMM.DECREASE_TUBE_GAIN_KEY);
        event.register(KeyMappingRegistryMM.OPEN_BACKPACK_KEY);
        event.register(KeyMappingRegistryMM.OPEN_RIG_KEY);
    }
}
