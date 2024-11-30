package net.tkg.ModernMayhem.GUI;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.tkg.ModernMayhem.util.AbstractContainerMenuUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

// ⚠️ : Was not made for any inventory larger than a simple chest (3 line with 9 item per line)
public abstract class GenericBackpackGUI extends AbstractContainerMenuUtil implements Supplier<Map<Integer, Slot>> {
    private int backpackSize;
    private int slotPerLine;
    private int numberOfLine;
    private IItemHandler itemHandler;
    private final Map<Integer, Slot> slots = new HashMap<>();


    public GenericBackpackGUI(@Nullable MenuType<?> pMenuType, int pContainerId, int pNumberOfLine, int pSlotPerLine, Inventory pPlayerInventory) {
        super(pMenuType, pContainerId);
        this.backpackSize = pNumberOfLine * pSlotPerLine;
        this.itemHandler = new ItemStackHandler(this.backpackSize);

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
        createPlayerInventory(pPlayerInventory);
        createPlayerHotbar(pPlayerInventory);

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
}
