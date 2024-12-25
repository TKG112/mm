package net.tkg.ModernMayhem.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.util.CuriosUtil;
import net.tkg.ModernMayhem.util.PacketBase;

public class OpenRigKeyPacket extends PacketBase {


    public OpenRigKeyPacket() {}

    public OpenRigKeyPacket(FriendlyByteBuf buffer) {}

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        Player player = context.getSender();
        ItemStack backpack = CuriosUtil.getRigItem(player);
        if (backpack.getItem() instanceof GenericBackpackItem genericBackpackItem) {
            genericBackpackItem.OpenGUIFromCuriosInventory(player, backpack);
        }
        return true;
    }
}
