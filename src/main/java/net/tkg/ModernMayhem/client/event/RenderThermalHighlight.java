package net.tkg.ModernMayhem.client.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.shaderRenderer.ThermalHighlightRenderer;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class RenderThermalHighlight {

    private RenderThermalHighlight() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ThermalHighlightRenderer.init();
            MinecraftForge.EVENT_BUS.register(ThermalHighlightRenderer.class);
        });
    }

    @Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
    public static final class ClientForgeEvents {

        private ClientForgeEvents() {}

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;

            Minecraft mc = Minecraft.getInstance();
            if (mc.getWindow() != null) {
                ThermalHighlightRenderer.resize(
                        mc.getWindow().getWidth(),
                        mc.getWindow().getHeight()
                );
            }
        }
    }
}