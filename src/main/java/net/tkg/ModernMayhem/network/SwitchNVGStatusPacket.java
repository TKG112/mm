package net.tkg.ModernMayhem.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.tkg.ModernMayhem.item.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.util.PacketBase;
import net.tkg.ModernMayhem.util.SoundUtil;
import top.theillusivec4.curios.api.CuriosApi;

public class SwitchNVGStatusPacket extends PacketBase {

    public SwitchNVGStatusPacket() {}

    public SwitchNVGStatusPacket(FriendlyByteBuf buffer) {}

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        if (context.getDirection() != NetworkDirection.PLAY_TO_SERVER) {
            return false;
        }
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            LevelAccessor world = player.level();
            CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
                inventory.getStacksHandler("facewear").ifPresent(facewearSlot -> {
                    ItemStack facewearItem = facewearSlot.getStacks().getStackInSlot(0);
                    if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                        GenericNVGGogglesItem.switchNVGMode(facewearItem);
                        if (world instanceof Level _world) {
                            if (GenericNVGGogglesItem.getNVGMode(facewearItem) == 1) {
                                if (((GenericNVGGogglesItem) facewearItem.getItem()).ACTIVATION_SOUND != null) {
                                    SoundUtil.playLocationAwareSound(_world, player.getX(), player.getY(), player.getZ(), ((GenericNVGGogglesItem) facewearItem.getItem()).ACTIVATION_SOUND, SoundSource.NEUTRAL, 1, 1);
                                }
                            } else {
                                if (((GenericNVGGogglesItem) facewearItem.getItem()).DEACTIVATION_SOUND != null) {
                                    SoundUtil.playLocationAwareSound(_world, player.getX(), player.getY(), player.getZ(), ((GenericNVGGogglesItem) facewearItem.getItem()).DEACTIVATION_SOUND, SoundSource.NEUTRAL, 1, 1);
                                }
                            }
                        }
                    }
                });
            });


        });
        return true;
    }
}
