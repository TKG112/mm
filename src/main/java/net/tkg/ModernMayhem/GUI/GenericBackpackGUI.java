package net.tkg.ModernMayhem.GUI;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.tkg.ModernMayhem.util.AbstractContainerMenuUtil;
import net.tkg.ModernMayhem.util.InventoryCapableItemInventoryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.Supplier;

// ⚠️ : Was not made for any inventory larger than a simple chest (3 line with 9 item per line)
public abstract class GenericBackpackGUI extends AbstractContainerMenuUtil implements Supplier<Map<Integer, Slot>> {
    private int backpackSize;
    private ItemStackHandler itemHandler;
    private final Map<Integer, Slot> slots = new HashMap<>();
    private int backpackSlotID = -1;
    private Inventory playerInventory;


    public GenericBackpackGUI(
            @Nullable MenuType<?> pMenuType,
            int pContainerId,
            int pNumberOfLine,
            int pSlotPerLine,
            Inventory pPlayerInventory,
            FriendlyByteBuf pExtraData
    ) {
        super(pMenuType, pContainerId);
        this.backpackSize = pNumberOfLine * pSlotPerLine;
        this.itemHandler = new ItemStackHandler(this.backpackSize);
        this.playerInventory = pPlayerInventory;

        // Extracting the data from pExtraData if it exists
        if (pExtraData != null) {
            // Extracting the itemHandler data
            CompoundTag tag = pExtraData.readNbt();
            if (tag != null) {
                this.itemHandler.deserializeNBT(tag);
            }
            // Find the slot ID of the item in the player's hand to lock it in the backpack
            ItemStack itemInHand = pExtraData.readItem();
            if (!itemInHand.isEmpty()) {
                for (int i = 0; i < pPlayerInventory.getContainerSize(); i++) {
                    if (ItemStack.isSameItemSameTags(pPlayerInventory.getItem(i), itemInHand)) {
                        this.backpackSlotID = i;
                        break;
                    }
                }
            }
        }

        // Generating the inventory slots
        int slotID = 0;
        for (int row = 0; row < pNumberOfLine; row++) {
            for (int collumn = 0; collumn < pSlotPerLine; collumn++) {
                int finalSlotID = slotID;
                this.slots.put(slotID, this.addSlot(new SlotItemHandler(
                        itemHandler,
                        finalSlotID,
                        8 + collumn * 18,
                        18 + row * 18) {
                    private final int slot = finalSlotID;
                }));
                slotID++;
            }
        }
        createPlayerInventory(pPlayerInventory, backpackSlotID);
        createPlayerHotbar(pPlayerInventory, backpackSlotID);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot originSlot = getSlot(pIndex);
        ItemStack originStack = originSlot.getItem();

        if(originStack.getCount() <= 0)
            originSlot.set(ItemStack.EMPTY);

        if(!originSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyOriginStack = originStack.copy();

        if(pIndex < 36) {
            if(!moveItemStackTo(originStack, 36, 36 + this.backpackSize, false))
                return ItemStack.EMPTY;
            else if (pIndex < 36 + this.backpackSize) {
                if(!moveItemStackTo(originStack, 0, 36, false))
                    return ItemStack.EMPTY;
            } else {
                System.err.println("Invalid slot index: " + pIndex);
                return ItemStack.EMPTY;
            }
        }

        originSlot.setChanged();
        originSlot.onTake(pPlayer, originStack);

        return copyOriginStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public Map<Integer, Slot> get() {
        return null;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
        // If the item is moved to the backpack
        if (pStartIndex < 36 && pEndIndex >= 36 && pEndIndex < 36 + this.backpackSize) {
            return super.moveItemStackTo(pStack, pStartIndex, pEndIndex, pReverseDirection);
        }
        // If the item is moved from the backpack
        if (pStartIndex >= 36 && pStartIndex < 36 + this.backpackSize && pEndIndex < 36) {

            return super.moveItemStackTo(pStack, pStartIndex, pEndIndex, pReverseDirection);
        }
        return super.moveItemStackTo(pStack, pStartIndex, pEndIndex, pReverseDirection);
    }
}
