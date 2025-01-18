package net.tkg.ModernMayhem.GUI;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.tkg.ModernMayhem.client.screen.BackpackScreen;
import net.tkg.ModernMayhem.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.registry.GUIRegistryMM;
import net.tkg.ModernMayhem.util.AbstractContainerMenuUtil;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

// ⚠️ : Was not made for any inventory larger than a simple chest (3 line with 9 item per line)
public class GenericBackpackGUI extends AbstractContainerMenuUtil implements Supplier<Map<Integer, Slot>> {
    private int backpackSize;
    private int numberOfLine;
    private int slotPerLine;
    private ItemStackHandler itemHandler;
    private final Map<Integer, Slot> slots = new HashMap<>();
    private int backpackSlotID = -1;
    private boolean isCuriosBackpack = false;
    private Inventory playerInventory;
    private ICuriosItemHandler playerCuriosInventory;
    private String curiosSlotType = "";

    public GenericBackpackGUI(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf data) {
        this(pContainerId, pPlayerInventory, null, data);
        new SimpleContainerData(0);
    }

    public GenericBackpackGUI(
            int pContainerId,
            Inventory pPlayerInventory,
            FriendlyByteBuf data,
            ICuriosItemHandler pPlayerCuriosInventory
    ) {
        this(pContainerId, pPlayerInventory, pPlayerCuriosInventory, data);
    }

    public GenericBackpackGUI(
            int pContainerId,
            Inventory pPlayerInventory,
            ICuriosItemHandler pPlayerCuriosInventory,
            FriendlyByteBuf pExtraData
    ) {
        super(GUIRegistryMM.BACKPACK_GUI.get(), pContainerId);
        this.numberOfLine = 1;
        this.slotPerLine = 1;
        this.itemHandler = new ItemStackHandler(this.backpackSize) {
            @Override
            protected void onContentsChanged(int slot) {
                updateBackpack();
                super.onContentsChanged(slot);
            }
        };
        this.playerInventory = pPlayerInventory;
        this.playerCuriosInventory = pPlayerCuriosInventory;
        // Checking if the backpack is a Curios backpack

        // Extracting the data from pExtraData if it exists
        if (pExtraData != null) {
            this.numberOfLine = pExtraData.readByte();
            this.slotPerLine = pExtraData.readByte();
            // Extracting the itemHandler data
            CompoundTag tag = pExtraData.readNbt();
            if (tag != null) {
                this.itemHandler.deserializeNBT(tag);
            }
            this.isCuriosBackpack = pExtraData.readBoolean();
            if (this.isCuriosBackpack) {
                // if client side we need to get the player's Curios inventory from the client
                this.backpackSlotID = pExtraData.readByte();
                this.curiosSlotType = switch (pExtraData.readByte()) {
                    case 0 -> "back";
                    case 1 -> "body";
                    default -> "";
                };
                if (pPlayerCuriosInventory == null) {
                    this.playerCuriosInventory = CuriosApi.getCuriosInventory(playerInventory.player).resolve().get();
                }
            } else {
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
        }

        this.backpackSize = this.numberOfLine * this.slotPerLine;

        // Generating the inventory slots
        int slotID = 0;
        for (int row = 0; row < this.numberOfLine; row++) {
            for (int collumn = 0; collumn < this.slotPerLine; collumn++) {
                int finalSlotID = slotID;
                this.slots.put(slotID, this.addSlot(new SlotItemHandler(
                        itemHandler,
                        slotID,
                        getBackpackInventoryLeftPos(this.slotPerLine) + 1 + collumn * 18,
                        BackpackScreen.cornerNWHeight + 1 + row * 18) {

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
        createPlayerHotbar(pPlayerInventory, this.backpackSlotID, !this.isCuriosBackpack, this.numberOfLine, this.slotPerLine);
        createPlayerInventory(pPlayerInventory, this.backpackSlotID, !this.isCuriosBackpack, this.numberOfLine, this.slotPerLine);
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
        // Updating the backpack item NBT but only on the server side
        if (this.isCuriosBackpack) {
            Optional<ICurioStacksHandler> temp = this.playerCuriosInventory.getStacksHandler(this.curiosSlotType);
            if (temp.isEmpty()) {
                System.err.println("Curios inventory not found : " + this.curiosSlotType);
                return;
            }
            temp.ifPresent(iCurioStacksHandler -> {
                ItemStack backpack = iCurioStacksHandler.getStacks().getStackInSlot(this.backpackSlotID);
                backpack.getOrCreateTag().put("inventory", itemHandler.serializeNBT());
                iCurioStacksHandler.getStacks().setStackInSlot(this.backpackSlotID, backpack);
            });
        } else {
            if (this.backpackSlotID > -1) {
                ItemStack backpack = playerInventory.getItem(this.backpackSlotID);
                backpack.getOrCreateTag().put("inventory", itemHandler.serializeNBT());
                playerInventory.setItem(this.backpackSlotID, backpack);
            }
        }
    }

    @Override
    public int getSlotPerLine() {
        return this.slotPerLine;
    }

    @Override
    public int getNumberOfLine() {
        return this.numberOfLine;
    }
}
