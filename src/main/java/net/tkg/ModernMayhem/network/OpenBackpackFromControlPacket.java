package net.tkg.ModernMayhem.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.item.curios.back.*;
import net.tkg.ModernMayhem.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.util.CuriosUtil;
import net.tkg.ModernMayhem.util.PacketBase;

public class OpenBackpackFromControlPacket extends PacketBase {


    public OpenBackpackFromControlPacket() {}

    public OpenBackpackFromControlPacket(FriendlyByteBuf buffer) {}

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        Player player = context.getSender();
        ItemStack backpack = CuriosUtil.getBackpackItem(player);
        if (backpack.getItem() instanceof GenericBackpackItem genericBackpackItem) {
            System.out.println("Opening backpack GUI");
            if (backpack.getItem() instanceof BlackBackpackT1Item exactBackpack1) {
                exactBackpack1.OpenGUIFromCuriosInventory(player, backpack);
                System.out.println("Opening Black Backpack T1 GUI");
            } else if (backpack.getItem() instanceof BlackBackpackT2Item exactBackpack2) {
                exactBackpack2.OpenGUIFromCuriosInventory(player, backpack);
                System.out.println("Opening Black Backpack T2 GUI");
            } else if (backpack.getItem() instanceof BlackBackpackT3Item exactBackpack3) {
                exactBackpack3.OpenGUIFromCuriosInventory(player, backpack);
                System.out.println("Opening Black Backpack T3 GUI");
            } else if (backpack.getItem() instanceof TanBackpackT1Item exactBackpack4) {
                exactBackpack4.OpenGUIFromCuriosInventory(player, backpack);
                System.out.println("Opening Tan Backpack T1 GUI");
            } else if (backpack.getItem() instanceof TanBackpackT2Item exactBackpack5) {
                exactBackpack5.OpenGUIFromCuriosInventory(player, backpack);
                System.out.println("Opening Tan Backpack T2 GUI");
            } else if (backpack.getItem() instanceof TanBackpackT3Item exactBackpack6) {
                exactBackpack6.OpenGUIFromCuriosInventory(player, backpack);
                System.out.println("Opening Tan Backpack T3 GUI");
            }
        }
        return true;
    }
}
