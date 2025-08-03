package net.tkg.ModernMayhem.server.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.server.item.curios.facewear.VisorItems;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.registry.SoundRegistryMM;
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
            Level world = context.getSender().level();
            Player clientPlayer = Minecraft.getInstance().player;
            if (CuriosUtil.hasNVGEquipped(player) || CuriosUtil.hasVisorEquipped(player)) {
                ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
                if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                    GenericNVGGogglesItem.switchEquipState(facewearItem);
                } else if (facewearItem.getItem() instanceof VisorItems) {
                    VisorItems.switchEquipState(facewearItem);
                    if (VisorItems.isVisorDown(facewearItem)) {
                        world.playSeededSound(clientPlayer, clientPlayer.getX(), clientPlayer.getY(), clientPlayer.getZ(), SoundRegistryMM.SOUND_VISOR_OPEN.get(), SoundSource.NEUTRAL,1.0F,1.0F,0);
                    } else {
                        world.playSeededSound(clientPlayer, clientPlayer.getX(), clientPlayer.getY(), clientPlayer.getZ(), SoundRegistryMM.SOUND_VISOR_CLOSE.get(), SoundSource.NEUTRAL,1.0F,1.0F,0);
                    }
                }
            }
        });
        return true;
    }
}
