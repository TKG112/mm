package net.tkg.ModernMayhem.client.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.thermal.Thermal;
import net.tkg.ModernMayhem.client.thermal.render.ThermalRenderer;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ThermalSetupEvent {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ThermalRenderer.init();

            MinecraftForge.EVENT_BUS.register(ThermalRenderer.class);

            Thermal.setupThermal();
        });
    }

    @Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Minecraft mc = Minecraft.getInstance();

                if (mc.getWindow() != null) {
                    int width = mc.getWindow().getWidth();
                    int height = mc.getWindow().getHeight();
                    ThermalRenderer.resize(width, height);
                }
            }
        }
    }
}
