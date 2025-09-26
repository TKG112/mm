package net.tkg.ModernMayhem.server.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.PacketBase;

public class SwitchNVGStatusPacket extends PacketBase {

    public SwitchNVGStatusPacket() {}

    public SwitchNVGStatusPacket(FriendlyByteBuf buffer) {}

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        if (!isCtS(context)) return false;
        // This section switch the NVG status on the player's facewear slot and play the sound accordingly
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (CuriosUtil.hasNVGEquipped(player)) {
                ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
                if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                    GenericNVGGogglesItem.switchEquipState(facewearItem);
                }
            }
        });
        return true;
    }
}
