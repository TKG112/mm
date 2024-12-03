package net.tkg.ModernMayhem.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractContainerMenuUtil extends AbstractContainerMenu implements ContainerUtil {


    protected AbstractContainerMenuUtil(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    public void createPlayerInventory(Inventory playerInventory, int lockedSlotID, boolean applyLock) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                if (applyLock) addSlot(new Slot(playerInventory, 9 + column + row * 9, 8 + column * 18, 84 + row * 18) {
                    @Override
                    public boolean mayPickup(@NotNull Player pPlayer) {
                        if (this.getContainerSlot() == lockedSlotID) {
                            return false;
                        }
                        return super.mayPickup(pPlayer);
                    }

                    @Override
                    public boolean mayPlace(@NotNull ItemStack pStack) {
                        if (this.getContainerSlot() == lockedSlotID) {
                            return false;
                        }
                        return super.mayPlace(pStack);
                    }
                });
                else addSlot(new Slot(playerInventory, 9 + column + row * 9, 8 + column * 18, 84 + row * 18));
            }
        }
    }

    public void createPlayerHotbar(Inventory playerInventory, int lockedSlotID, boolean applyLock) {
        for (int column = 0; column < 9; ++column) {
            if (applyLock) addSlot(new Slot(playerInventory, column, 8 + column * 18, 142) {
                @Override
                public boolean mayPickup(@NotNull Player pPlayer) {
                    if (this.getContainerSlot() == lockedSlotID) {
                        return false;
                    }
                    return super.mayPickup(pPlayer);
                }

                @Override
                public boolean mayPlace(@NotNull ItemStack pStack) {
                    if (this.getContainerSlot() == lockedSlotID) {
                        return false;
                    }
                    return super.mayPlace(pStack);
                }
            });
            else addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }
}
