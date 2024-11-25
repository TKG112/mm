package net.tkg.ModernMayhem.util;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.item.generic.GenericNVGGogglesItem;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CuriosUtil {

    public static boolean hasNVGEquipped(Player player) {
        AtomicBoolean result = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent( curiosInventory -> {
            curiosInventory.getStacksHandler("facewear").ifPresent( facewearSlot -> {
                ItemStack facewearItem = facewearSlot.getStacks().getStackInSlot(0);
                if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                    result.set(true);
                }
            });
        });
        return result.get();
    }

    public static ItemStack getFaceWearItem(Player player) {
        AtomicReference<ItemStack> facewearItem = new AtomicReference<ItemStack>(null);
        CuriosApi.getCuriosInventory(player).ifPresent( curiosInventory -> {
            curiosInventory.getStacksHandler("facewear").ifPresent( facewearSlot -> {
                facewearItem.set(facewearSlot.getStacks().getStackInSlot(0));
            });
        });
        return facewearItem.get();
    }
}
