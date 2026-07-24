package net.tkg.ModernMayhem.server.network;

import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.server.registry.SoundRegistryMM;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.PacketBase;

public class ThermalPaletteCyclePacket extends PacketBase {

    private final boolean forward;

    public ThermalPaletteCyclePacket(boolean forward) {
        this.forward = forward;
    }

    public ThermalPaletteCyclePacket(FriendlyByteBuf buffer) {
        this.forward = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(forward);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        if (!isCtS(context)) return false;
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            ItemStack face = CuriosUtil.getFaceWearItem(player);
            if (GenericSpecialGogglesItem.isThermal(face)) {
                GenericSpecialGogglesItem.cycleThermalPalette(face, forward);
                player.playNotifySound(SoundRegistryMM.SMALL_CLICK.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
        });
        return true;
    }
}