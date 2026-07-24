package net.tkg.ModernMayhem.server.util;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.server.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CuriosUtil {

    public static boolean hasNVGEquipped(Player player) {
        AtomicBoolean result = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent( curiosInventory -> {
            curiosInventory.getStacksHandler("facewear").ifPresent( facewearSlot -> {
                ItemStack facewearItem = facewearSlot.getStacks().getStackInSlot(0);
                if (facewearItem.getItem() instanceof GenericSpecialGogglesItem) {
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
                ItemStack backItem = facewearSlot.getStacks().getStackInSlot(0);
                if (backItem.getItem() instanceof GenericBackpackItem) {
                    result.set(true);
                }
            });
        });
        return result.get();
    }

    public static boolean hasRigEquipped(Player player) {
        AtomicBoolean result = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent( curiosInventory -> {
            curiosInventory.getStacksHandler("body").ifPresent( facewearSlot -> {
                ItemStack chestItem = facewearSlot.getStacks().getStackInSlot(0);
                if (chestItem.getItem() instanceof GenericBackpackItem) {
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

    public static ItemStack getRigItem(Player player) {
        AtomicReference<ItemStack> rigItem = new AtomicReference<ItemStack>(null);
        CuriosApi.getCuriosInventory(player).ifPresent( curiosInventory -> {
            curiosInventory.getStacksHandler("body").ifPresent( rigSlot -> {
                for (int i = 0; i < rigSlot.getStacks().getSlots(); i++) {
                    ItemStack stack = rigSlot.getStacks().getStackInSlot(i);
                    if (stack.getItem() instanceof GenericBackpackItem) {
                        rigItem.set(rigSlot.getStacks().getStackInSlot(i));
                        break;
                    }
                }
            });
        });
        return rigItem.get();
    }

    // --- generic, identifier-based lookups (used by data-driven curios, incl. pack-invented slots) ---

    /** First {@link GenericBackpackItem} held in the given Curios slot, or an empty stack. */
    public static ItemStack getCurioInSlot(Player player, String slot) {
        AtomicReference<ItemStack> found = new AtomicReference<>(ItemStack.EMPTY);
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory ->
                curiosInventory.getStacksHandler(slot).ifPresent(handler -> {
                    for (int i = 0; i < handler.getStacks().getSlots(); i++) {
                        ItemStack stack = handler.getStacks().getStackInSlot(i);
                        if (stack.getItem() instanceof GenericBackpackItem) {
                            found.set(stack);
                            break;
                        }
                    }
                }));
        return found.get();
    }

    /** Index of the first {@link GenericBackpackItem} in the given Curios slot, or -1. */
    public static int getCurioSlotID(Player player, String slot) {
        AtomicReference<Integer> index = new AtomicReference<>(-1);
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory ->
                curiosInventory.getStacksHandler(slot).ifPresent(handler -> {
                    for (int i = 0; i < handler.getStacks().getSlots(); i++) {
                        if (handler.getStacks().getStackInSlot(i).getItem() instanceof GenericBackpackItem) {
                            index.set(i);
                            break;
                        }
                    }
                }));
        return index.get();
    }

    public static boolean hasCurioInSlot(Player player, String slot) {
        return !getCurioInSlot(player, slot).isEmpty();
    }

    /**
     * Whether this player actually has the given Curios slot at all.
     * <p>
     * Curios slots are server-authoritative and synced to clients, so this is the natural parity check
     * between a client that has a content pack and a server that may not.
     */
    public static boolean hasSlot(Player player, String slot) {
        AtomicBoolean present = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory ->
                present.set(curiosInventory.getStacksHandler(slot).isPresent()));
        return present.get();
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

    public static int getRigSlotID(Player player) {
        AtomicReference<Integer> rigSlotID = new AtomicReference<Integer>(-1);
        CuriosApi.getCuriosInventory(player).ifPresent( curiosInventory -> {
            curiosInventory.getStacksHandler("body").ifPresent( rigSlot -> {
                for (int i = 0; i < rigSlot.getStacks().getSlots(); i++) {
                    ItemStack stack = rigSlot.getStacks().getStackInSlot(i);
                    if (stack.getItem() instanceof GenericBackpackItem) {
                        rigSlotID.set(i);
                        break;
                    }
                }
            });
        });
        return rigSlotID.get();
    }
}
