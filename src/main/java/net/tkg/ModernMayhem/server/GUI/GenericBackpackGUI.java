package net.tkg.ModernMayhem.server.GUI;

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
import net.tkg.ModernMayhem.server.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.server.registry.GUIRegistryMM;
import net.tkg.ModernMayhem.server.util.AbstractContainerMenuUtil;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

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

        // Generating the backpack inventory slots FIRST
        int slotID = 0;
        for (int row = 0; row < this.numberOfLine; row++) {
            for (int collumn = 0; collumn < this.slotPerLine; collumn++) {
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

        // Then add player inventory (main inventory first, then hotbar)
        createPlayerInventory(pPlayerInventory, this.backpackSlotID, !this.isCuriosBackpack, this.numberOfLine, this.slotPerLine);
        createPlayerHotbar(pPlayerInventory, this.backpackSlotID, !this.isCuriosBackpack, this.numberOfLine, this.slotPerLine);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot originSlot = getSlot(pIndex);
        ItemStack originStack = originSlot.getItem();

        if (!originSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyOriginStack = originStack.copy();

        // Slot ranges:
        // 0 to (backpackSize-1): Backpack slots
        // backpackSize to (backpackSize+26): Player main inventory (3 rows)
        // (backpackSize+27) to (backpackSize+35): Player hotbar

        int playerInventoryStart = this.backpackSize;
        int playerInventoryEnd = this.backpackSize + 27;
        int hotbarStart = this.backpackSize + 27;
        int hotbarEnd = this.backpackSize + 36;

        if (pIndex < this.backpackSize) {
            // Moving FROM backpack TO player inventory
            // Try hotbar first, then main inventory
            if (!moveItemStackTo(originStack, hotbarStart, hotbarEnd, false)) {
                if (!moveItemStackTo(originStack, playerInventoryStart, playerInventoryEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else if (pIndex >= playerInventoryStart && pIndex < hotbarEnd) {
            // Moving FROM player inventory or hotbar TO backpack
            if (!moveItemStackTo(originStack, 0, this.backpackSize, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.err.println("Invalid slot index: " + pIndex);
            return ItemStack.EMPTY;
        }

        if (originStack.isEmpty()) {
            originSlot.set(ItemStack.EMPTY);
        } else {
            originSlot.setChanged();
        }

        if (originStack.getCount() == copyOriginStack.getCount()) {
            return ItemStack.EMPTY;
        }

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