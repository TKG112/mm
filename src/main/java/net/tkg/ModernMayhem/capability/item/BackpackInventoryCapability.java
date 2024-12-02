package net.tkg.ModernMayhem.capability.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.tkg.ModernMayhem.item.generic.GenericBackapackItem;
import org.jetbrains.annotations.NotNull;

public class BackpackInventoryCapability extends ItemStackHandler {
    public BackpackInventoryCapability(int size) {
        super(size);
    }

    public BackpackInventoryCapability() {
        super(1);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof GenericBackapackItem) {
            return false;
        }
        return super.isItemValid(slot, stack);
    }
}
