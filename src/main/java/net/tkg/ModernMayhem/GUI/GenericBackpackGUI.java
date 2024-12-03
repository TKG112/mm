package net.tkg.ModernMayhem.GUI;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.tkg.ModernMayhem.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.util.AbstractContainerMenuUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

// ⚠️ : Was not made for any inventory larger than a simple chest (3 line with 9 item per line)
public abstract class GenericBackpackGUI extends AbstractContainerMenuUtil implements Supplier<Map<Integer, Slot>> {
    private int backpackSize;
    private ItemStackHandler itemHandler;
    private final Map<Integer, Slot> slots = new HashMap<>();
    private int backpackSlotID = -1;
    private boolean isCuriosBackpack = false;
    private Inventory playerInventory;
    private ICuriosItemHandler playerCuriosInventory;


    public GenericBackpackGUI(
            @Nullable MenuType<?> pMenuType,
            int pContainerId,
            int pNumberOfLine,
            int pSlotPerLine,
            Inventory pPlayerInventory,
            ICuriosItemHandler pPlayerCuriosInventory,
            FriendlyByteBuf pExtraData
    ) {
        super(pMenuType, pContainerId);
        this.backpackSize = pNumberOfLine * pSlotPerLine;
        this.itemHandler = new ItemStackHandler(this.backpackSize) {
            @Override
            protected void onContentsChanged(int slot) {
                updateBackpack();
                super.onContentsChanged(slot);
            }
        };
        this.playerInventory = pPlayerInventory;
        this.playerCuriosInventory = pPlayerCuriosInventory;

        // Extracting the data from pExtraData if it exists
        if (pExtraData != null) {
            // Extracting the itemHandler data
            CompoundTag tag = pExtraData.readNbt();
            if (tag != null) {
                this.itemHandler.deserializeNBT(tag);
            }
            // Checking if the backpack is a Curios backpack
            this.isCuriosBackpack = pExtraData.readBoolean();


            // Find the slot ID of the item in the player's hand to lock it in the backpack
            ItemStack itemInHand = pExtraData.readItem();
            if (this.isCuriosBackpack) {
                this.backpackSlotID = pExtraData.readInt();
            } else {
                if (!itemInHand.isEmpty()) {
                    for (int i = 0; i < pPlayerInventory.getContainerSize(); i++) {
                        if (ItemStack.isSameItemSameTags(pPlayerInventory.getItem(i), itemInHand)) {
                            this.backpackSlotID = i;
                            System.out.println("Found the backpack slot ID: " + i);
                            break;
                        }
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
                        slotID,
                        8 + collumn * 18,
                        18 + row * 18) {
                    private final int slotID = finalSlotID;

                    @Override
                    public void initialize(ItemStack stack) {}

                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        if (stack.getItem() instanceof GenericBackpackItem) {
                            return false;
                        }
                        return super.mayPlace(stack);
                    }
                }));
                slotID++;
            }
        }
        createPlayerHotbar(pPlayerInventory, this.backpackSlotID, this.isCuriosBackpack);
        createPlayerInventory(pPlayerInventory, this.backpackSlotID, this.isCuriosBackpack);
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
            // Moving from player inventory to backpack
            if (!moveItemStackTo(originStack, 36, 36 + this.backpackSize, false))
                return ItemStack.EMPTY;
        }else if (pIndex < (36 + this.backpackSize)) {
            // Moving from backpack to player inventory
            if (!moveItemStackTo(originStack, 0, 36, false))
                return ItemStack.EMPTY;
        } else {
                System.err.println("Invalid slot index: " + pIndex);
                return ItemStack.EMPTY;
        }

        originSlot.setChanged();
        originSlot.onTake(pPlayer, originStack);
        updateBackpack();

        return copyOriginStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public Map<Integer, Slot> get() {
        return this.slots;
    }

    private void updateBackpack() {
        if (this.isCuriosBackpack) {
            this.playerCuriosInventory.getStacksHandler("back").ifPresent(iCurioStacksHandler -> {
                ItemStack backpack = iCurioStacksHandler.getStacks().getStackInSlot(this.backpackSlotID);
                backpack.getOrCreateTag().put("inventory", itemHandler.serializeNBT());
                iCurioStacksHandler.getStacks().setStackInSlot(this.backpackSlotID, backpack);
            });
            return;
        } else {
            if (this.backpackSlotID > -1) {
                ItemStack backpack = playerInventory.getItem(this.backpackSlotID);
                backpack.getOrCreateTag().put("inventory", itemHandler.serializeNBT());
                playerInventory.setItem(this.backpackSlotID, backpack);
            }
        }
    }
}
