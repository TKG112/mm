package net.tkg.ModernMayhem.server.compat;

import net.irisshaders.iris.apiimpl.IrisApiV0Impl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tkg.ModernMayhem.client.event.RenderNVGShader;

public class OculusCompat {

    public static void init(IEventBus modEventBus) {
        // Register the client tick event to check if the Oculus shader is enabled
        MinecraftForge.EVENT_BUS.register(new OculusCompat());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.PlayerTickEvent event) {
        RenderNVGShader.oculusShaderEnabled = IrisApiV0Impl.INSTANCE.isShaderPackInUse();
    }
}
