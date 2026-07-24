package net.tkg.ModernMayhem.client.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.thermal.render.ThermalRenderer;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderThermalArm {

    @SubscribeEvent
    public static void onRenderArm(RenderArmEvent event) {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            if (!mc.player.getMainHandItem().isEmpty() || !mc.player.getOffhandItem().isEmpty()) return;
            ThermalRenderer.stageArmForMask(event.getPlayer(), event.getArm(), event.getPoseStack());
        } catch (Throwable t) {
            try { ModernMayhemMod.LOGGER.error("[RenderThermalArm] arm stage skipped a frame"); } catch (Throwable ignored) { }
        }
    }
}