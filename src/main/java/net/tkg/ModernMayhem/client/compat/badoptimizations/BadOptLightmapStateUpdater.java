package net.tkg.ModernMayhem.client.compat.badoptimizations;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;


@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BadOptLightmapStateUpdater {

    private BadOptLightmapStateUpdater() {}

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        BadOptLightmapHook.LIGHTMAP_DRIVEN = isGoggleLightmapActive();
    }

    private static boolean isGoggleLightmapActive() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        ItemStack face = CuriosUtil.getFaceWearItem(mc.player);
        if (face == null || face.isEmpty()) return false;
        if (!GenericSpecialGogglesItem.isThermal(face) && !GenericSpecialGogglesItem.isNightVision(face)) return false;
        return GenericSpecialGogglesItem.getNVGCheck(face); // powered / switched on
    }
}
