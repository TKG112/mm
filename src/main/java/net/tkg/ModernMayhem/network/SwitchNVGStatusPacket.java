package net.tkg.ModernMayhem.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.registry.SoundRegistryMM;
import net.tkg.ModernMayhem.util.PacketBase;
import top.theillusivec4.curios.api.CuriosApi;

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
            CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
                inventory.getStacksHandler("facewear").ifPresent(facewearSlot -> {
                    ItemStack facewearItem = facewearSlot.getStacks().getStackInSlot(0);
                    if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                        GenericNVGGogglesItem.switchNVGMode(facewearItem);
                        if (GenericNVGGogglesItem.getNVGCheck(facewearItem)) {
                            System.out.println("Playing sound on");
                            world.playSeededSound(player, player.getX(), player.getY(), player.getZ(), SoundRegistryMM.SOUND_NVG_ON.get(), SoundSource.NEUTRAL, 1, 1, 0 );
                        } else {
                            System.out.println("Playing sound off");
                            world.playSeededSound(player, player.getX(), player.getY(), player.getZ(), SoundRegistryMM.SOUND_NVG_OFF.get(), SoundSource.NEUTRAL, 1, 1, 0 );
                        }
                    }
                });
            });
        });
        return true;
    }
}
