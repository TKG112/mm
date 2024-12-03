package net.tkg.ModernMayhem.item.generic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.tkg.ModernMayhem.util.IBackpackItem;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public abstract class GenericBackpackItem extends Item implements ICurioItem, IBackpackItem {
    private final int inventorySize;
    private final int inventoryLines;
    private final int inventoryColumns;

    public GenericBackpackItem(int pInventoryLines, int pInventoryColumns) {
        super(new Properties().stacksTo(1));
        this.inventorySize = pInventoryLines * pInventoryColumns;
        this.inventoryLines = pInventoryLines;
        this.inventoryColumns = pInventoryColumns;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        InitInventory(stack, this.inventorySize);
        if (pPlayer instanceof ServerPlayer && pUsedHand == InteractionHand.MAIN_HAND) {
            OpenGUIFromPlayerInventory(pPlayer, stack);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {
        ItemStack slotStack = pSlot.getItem();
        // Check if the item has an inventory tag if not create one
        CompoundTag tag = InitInventory(pStack, this.inventorySize);

        if (pAction == ClickAction.SECONDARY) {
            ItemStackHandler inventory = new ItemStackHandler(inventorySize);
            inventory.deserializeNBT(tag.getCompound("inventory"));
            if (slotStack.isEmpty()) {
                // If the slot is empty, we can insert the stack
                for (int i = inventory.getSlots() - 1; i >= 0; i--) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        pSlot.set(inventory.extractItem(i, stack.getCount(), false));
                        tag.put("inventory", inventory.serializeNBT());
                        return true;
                    }
                }
            } else {
                // If the slot is not empty, we add the stack to the inventory
                for (int i = 0; i < inventory.getSlots(); i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    // If the stack is the same as the slotStack, we can merge the stacks
                    if (ItemStack.isSameItemSameTags(slotStack, inventory.getStackInSlot(i))) {
                        ItemStack remaining = inventory.insertItem(i, slotStack, false);
                        pSlot.set(remaining);
                        tag.put("inventory", inventory.serializeNBT());
                        if (remaining.getCount() <= 0) return true;
                    }
                    // If the stack is empty, we can insert the slotStack
                    if (stack.isEmpty()) {
                        inventory.insertItem(i, slotStack, false);
                        pSlot.set(ItemStack.EMPTY);
                        tag.put("inventory", inventory.serializeNBT());
                        return true;
                    }
                }
            }
        }
        return super.overrideStackedOnOther(pStack, pSlot, pAction, pPlayer);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        return super.overrideOtherStackedOnMe(pStack, pOther, pSlot, pAction, pPlayer, pAccess);
    }

    protected static CompoundTag InitInventory(ItemStack pStack, int pInventorySize) {
        CompoundTag tag = pStack.getOrCreateTag();
        if (!tag.contains("inventory")) {
            tag.put("inventory", new ItemStackHandler(pInventorySize).serializeNBT());
        }
        return tag;
    }
}
