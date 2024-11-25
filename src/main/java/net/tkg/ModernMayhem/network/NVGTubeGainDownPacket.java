package net.tkg.ModernMayhem.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.util.CuriosUtil;
import net.tkg.ModernMayhem.util.PacketBase;

public class NVGTubeGainDownPacket extends PacketBase {

    public NVGTubeGainDownPacket() {}

    public NVGTubeGainDownPacket(FriendlyByteBuf buffer) {}


    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        if (context.getDirection() != NetworkDirection.PLAY_TO_SERVER || context.getSender() == null) {
            // Just checking if the packet is being sent to the server and the sender is not null
            return false;
        }
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (CuriosUtil.hasNVGEquipped(player)) {
                ItemStack nvgItem = CuriosUtil.getFaceWearItem(player);
                if (nvgItem.getItem() instanceof GenericNVGGogglesItem) {
                    GenericNVGGogglesItem.switchConfigDown(nvgItem);
                }
            }
        });
        return true;
    }
}
