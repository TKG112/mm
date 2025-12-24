package net.tkg.ModernMayhem.server.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.PacketBase;

public class NVGSyncSwitchOnPacket extends PacketBase {

    public NVGSyncSwitchOnPacket() {}

    public NVGSyncSwitchOnPacket(FriendlyByteBuf buffer) {}

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        if (!isCtS(context)) return false;
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
                if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                    GenericNVGGogglesItem.switchOnNVGMode(facewearItem);
                }
            }
        });
        return true;
    }
}
