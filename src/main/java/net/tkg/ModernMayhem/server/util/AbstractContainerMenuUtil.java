package net.tkg.ModernMayhem.server.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.client.screen.BackpackScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractContainerMenuUtil extends AbstractContainerMenu implements ContainerUtil {

    public int getPlayerInventoryTopPos(int numberOfLineInBackpack) {
        return numberOfLineInBackpack * BackpackScreen.slotSize + 4 + BackpackScreen.topBorderHeight * 2;
    }

    public int getPlayerInventoryLeftPos(int numberOfColumnInBackpack) {
        if (numberOfColumnInBackpack > 9) {
            int backpackInventoryWidth = BackpackScreen.cornerNWWidth+ BackpackScreen.slotSize * numberOfColumnInBackpack + BackpackScreen.cornerNEWidth;
            return (backpackInventoryWidth - BackpackScreen.playerInventoryWidth) / 2;
        }
        return BackpackScreen.leftBorderWidth + 1;
    }

    public int getPlayerHotbarTopPos(int numberOfLineInBackpack) {
        return getPlayerInventoryTopPos(numberOfLineInBackpack) + 3 * BackpackScreen.slotSize + 4;
    }

    public int getBackpackInventoryLeftPos(int numberOfColumnInBackpack) {
        if (numberOfColumnInBackpack < 9) {
            int backpackInventoryWidth = BackpackScreen.cornerNWWidth + BackpackScreen.slotSize * numberOfColumnInBackpack + BackpackScreen.cornerNEWidth;
            return (BackpackScreen.playerInventoryWidth - backpackInventoryWidth) / 2 + BackpackScreen.cornerNWWidth;
        }
        return BackpackScreen.leftBorderWidth;
    }

    protected AbstractContainerMenuUtil(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    public void createPlayerInventory(Inventory playerInventory, int lockedSlotID, boolean applyLock, int numberOfLineInBackpack, int numberOfColumnInBackpack) {
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                if (applyLock) addSlot(new Slot(
                        playerInventory,
                        9 + column + row * 9,
                        getPlayerInventoryLeftPos(numberOfColumnInBackpack) + column * 18,
                        getPlayerInventoryTopPos(numberOfLineInBackpack) + row * 18
                ) {
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
                else addSlot(new Slot(playerInventory,
                        9 + column + row * 9,
                        getPlayerInventoryLeftPos(numberOfColumnInBackpack) + column * 18,
                        getPlayerInventoryTopPos(numberOfLineInBackpack) + row * 18
                ));
            }
        }
    }

    public void createPlayerHotbar(Inventory playerInventory, int lockedSlotID, boolean applyLock, int numberOfLineInBackpack, int numberOfColumnInBackpack) {
        for (int column = 0; column < 9; ++column) {
            if (applyLock) addSlot(new Slot(
                    playerInventory,
                    column,
                    getPlayerInventoryLeftPos(numberOfColumnInBackpack) + column * 18,
                    getPlayerHotbarTopPos(numberOfLineInBackpack)
            ) {
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
            else addSlot(new Slot(
                    playerInventory,
                    column,
                    getPlayerInventoryLeftPos(numberOfColumnInBackpack) + column * 18,
                    getPlayerHotbarTopPos(numberOfLineInBackpack)
            ));
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
