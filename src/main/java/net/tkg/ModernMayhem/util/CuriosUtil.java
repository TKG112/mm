package net.tkg.ModernMayhem.util;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.item.generic.GenericBackpackItem;
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

    public static boolean hasBackpackEquipped(Player player) {
        AtomicBoolean result = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent( curiosInventory -> {
            curiosInventory.getStacksHandler("back").ifPresent( facewearSlot -> {
                ItemStack facewearItem = facewearSlot.getStacks().getStackInSlot(0);
                if (facewearItem.getItem() instanceof GenericBackpackItem) {
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

    public static ItemStack getBackpackItem(Player player) {
        AtomicReference<ItemStack> backpackItem = new AtomicReference<ItemStack>(null);
        CuriosApi.getCuriosInventory(player).ifPresent( curiosInventory -> {
            curiosInventory.getStacksHandler("back").ifPresent( backpackSlot -> {
                System.out.println("[[[ HERe ]]]");
                for (int i = 0; i < backpackSlot.getStacks().getSlots(); i++) {
                    ItemStack stack = backpackSlot.getStacks().getStackInSlot(i);
                    if (stack.getItem() instanceof GenericBackpackItem) {
                        backpackItem.set(backpackSlot.getStacks().getStackInSlot(i));
                        break;
                    }
                }
            });
        });
        return backpackItem.get();
    }

    public static int getBackpackSlotID(Player player) {
        AtomicReference<Integer> backpackSlotID = new AtomicReference<Integer>(-1);
        CuriosApi.getCuriosInventory(player).ifPresent( curiosInventory -> {
            curiosInventory.getStacksHandler("back").ifPresent( backpackSlot -> {
                for (int i = 0; i < backpackSlot.getStacks().getSlots(); i++) {
                    ItemStack stack = backpackSlot.getStacks().getStackInSlot(i);
                    if (stack.getItem() instanceof GenericBackpackItem) {
                        backpackSlotID.set(i);
                        break;
                    }
                }
            });
        });
        return backpackSlotID.get();
    }

}
