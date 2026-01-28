package net.tkg.ModernMayhem.client.outline;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.outline.render.OutlineRenderer;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OutlineSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            OutlineRenderer.init();

            MinecraftForge.EVENT_BUS.register(OutlineRenderer.class);

            OutlineThermal.setupOutlines();

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
                    OutlineRenderer.resize(width, height);
                }
            }
        }
    }
}
