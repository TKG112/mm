package net.tkg.ModernMayhem.server.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
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
        if (context.getDirection() != NetworkDirection.PLAY_TO_SERVER || context.getSender() == null) {
            // Just checking if the packet is being sent to the server and the sender is not null
            return false;
        }
        // This section switch the NVG status on the player's facewear slot and play the sound accordingly
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level world = context.getSender().level();
            Player realPlayer = Minecraft.getInstance().player;
            if (CuriosUtil.hasNVGEquipped(player)) {
                ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
                if (facewearItem.getItem() instanceof GenericNVGGogglesItem genericNVGGogglesItem) {
                    GenericNVGGogglesItem.switchNVGMode(facewearItem);
                    if (GenericNVGGogglesItem.getNVGCheck(facewearItem)) {
                        System.out.println("Playing sound on");
                        world.playSeededSound(realPlayer, player.getX(), player.getY(), player.getZ(), genericNVGGogglesItem.ACTIVATION_SOUND.get(), SoundSource.NEUTRAL, 1, 1, 0 );
                    } else {
                        System.out.println("Playing sound off");
                        world.playSeededSound(realPlayer, player.getX(), player.getY(), player.getZ(), genericNVGGogglesItem.DEACTIVATION_SOUND.get(), SoundSource.NEUTRAL, 1, 1, 0 );
                    }
                }
            }
        });
        return true;
    }
}
