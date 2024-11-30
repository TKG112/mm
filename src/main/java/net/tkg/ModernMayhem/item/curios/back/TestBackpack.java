package net.tkg.ModernMayhem.item.curios.back;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.tkg.ModernMayhem.GUI.TestBackpackGUIMenu;
import net.tkg.ModernMayhem.item.generic.GenericBackapackItem;
import org.jetbrains.annotations.NotNull;

public class TestBackpack extends GenericBackapackItem {
    public TestBackpack() {
        super(3, 3);
    }

    @Override
    public void openGUI(ServerPlayer pSPlayer, Inventory pPlayerInventory) {
        pSPlayer.openMenu(new MenuProvider() {

            @Override
            public @NotNull Component getDisplayName() {
                return pSPlayer.getMainHandItem().getDisplayName();
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(
                    int pContainerId,
                    @NotNull Inventory pPlayerInventory,
                    @NotNull Player pPlayer) {
                return new TestBackpackGUIMenu(pContainerId, pPlayerInventory);
            }
        });
    }
}
