package net.tkg.ModernMayhem.server.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.registry.SoundRegistryMM;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.PacketBase;

public class NVGTubeGainUpPacket extends PacketBase {

    public NVGTubeGainUpPacket() {}

    public NVGTubeGainUpPacket(FriendlyByteBuf buffer) {}

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        if (!isCtS(context)) return false;
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level world = context.getSender().level();
            Player clientPlayer = Minecraft.getInstance().player;
            if (CuriosUtil.hasNVGEquipped(player)) {
                ItemStack nvgItem = CuriosUtil.getFaceWearItem(player);
                if (nvgItem.getItem() instanceof NVGGogglesItem) {
                    GenericNVGGogglesItem.switchConfigUp(nvgItem);
                    if (GenericNVGGogglesItem.hasConfigIndexChanged(clientPlayer, nvgItem)) {
                        world.playSeededSound(clientPlayer, clientPlayer.getX(), clientPlayer.getY(), clientPlayer.getZ(), SoundRegistryMM.SMALL_CLICK.get(), SoundSource.NEUTRAL, 1.0F, 1.0F, 0);
                    }
                }
            }
        });
        return true;
    }
}
