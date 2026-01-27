package net.tkg.ModernMayhem.server.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.registry.SoundRegistryMM;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.PacketBase;

public class NVGCotiTogglePacket extends PacketBase {

    public NVGCotiTogglePacket() {}

    public NVGCotiTogglePacket(FriendlyByteBuf buffer) {}

    @Override
    public void write(FriendlyByteBuf buffer) {}

    @Override
    public boolean handle(NetworkEvent.Context context) {
        if (!isCtS(context)) return false;
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && CuriosUtil.hasNVGEquipped(player)) {
                ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
                if (facewearItem.getItem() instanceof NVGGogglesItem) {
                    if (GenericSpecialGogglesItem.hasCoti(facewearItem)) {
                        GenericSpecialGogglesItem.switchCotiMode(facewearItem);
                        player.playNotifySound(SoundRegistryMM.SMALL_CLICK.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                    }
                }
            }
        });
        return true;
    }
}