package net.tkg.ModernMayhem.client.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.config.ClientConfig;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FacewearStatusDisplay {

    private static Item lastItem;
    private static boolean lastDown;

    private FacewearStatusDisplay() {}

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!ClientConfig.SHOW_FACEWEAR_STATUS.get()) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            lastItem = null;
            return;
        }

        ItemStack facewear = CuriosUtil.getFaceWearItem(player);
        if (facewear == null || facewear.isEmpty()
                || !(facewear.getItem() instanceof GenericSpecialGogglesItem)) {
            lastItem = null;
            return;
        }

        boolean down = GenericSpecialGogglesItem.isNVGOnFace(facewear);

        if (facewear.getItem() != lastItem) {
            lastItem = facewear.getItem();
            lastDown = down;
            return;
        }

        if (down == lastDown) return;
        lastDown = down;

        boolean visor = GenericSpecialGogglesItem.isVisor(facewear);
        String key = visor
                ? (down ? "message.mm.visor_down" : "message.mm.visor_up")
                : (down ? "message.mm.facewear_on" : "message.mm.facewear_off");

        player.displayClientMessage(
                Component.translatable(key, facewear.getHoverName())
                        .withStyle(down ? ChatFormatting.GREEN : ChatFormatting.GRAY),
                true);
    }
}
