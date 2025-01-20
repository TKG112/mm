package net.tkg.ModernMayhem.server.util;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class InventoryCapableItemInventoryUtil {

    public static Dictionary<Integer, ItemStack> getInventoryContent(ItemStack pInventoryCapableItemStack) {
        Dictionary<Integer, ItemStack> result = new Hashtable<>();
        CompoundTag compoundtag = pInventoryCapableItemStack.getOrCreateTag();
        if (compoundtag.contains("Items")) {
            ListTag listtag = compoundtag.getList("Items", 10);
            for (int i = 0; i < listtag.size(); i++) {
                ItemStack stack = ItemStack.of(listtag.getCompound(i));
                result.put(i, stack);
            }
        }
        return result;
    }

    public static FriendlyByteBuf getInventoryContentAsFriendlyByteBuf(ItemStack pInventoryCapableItemStack) {
        FriendlyByteBuf result = new FriendlyByteBuf(Unpooled.buffer());
        Dictionary<Integer, ItemStack> inventoryContent = getInventoryContent(pInventoryCapableItemStack);
        for (Enumeration<Integer> keys = inventoryContent.keys(); keys.hasMoreElements();) {
            Integer key = keys.nextElement();
            ItemStack stack = inventoryContent.get(key);
            result.writeInt(key);
            result.writeItemStack(stack, false);
        }
        return result;
    }

    public static Dictionary<Integer, ItemStack> getInventoryContentFromFriendlyByteBuf(FriendlyByteBuf pFriendlyByteBuf) {
        Dictionary<Integer, ItemStack> result = new Hashtable<>();
        while (pFriendlyByteBuf.isReadable()) {
            Integer key = pFriendlyByteBuf.readInt();
            ItemStack stack = pFriendlyByteBuf.readItem();
            result.put(key, stack);
        }
        return result;
    }
}
