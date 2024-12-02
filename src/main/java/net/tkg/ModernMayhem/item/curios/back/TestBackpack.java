package net.tkg.ModernMayhem.item.curios.back;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.GUI.TestBackpackGUIMenu;
import net.tkg.ModernMayhem.item.generic.GenericBackapackItem;
import org.jetbrains.annotations.Nullable;

public class TestBackpack extends GenericBackapackItem {
    public TestBackpack() {
        super(3, 3);
    }

    @Override
    public void OpenGUI(Player pPlayer, ItemStack pStack, ItemStack itemInHand) {
        pPlayer.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Test Backpack");
            }

            @Override
            public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
                CompoundTag tag = pStack.getOrCreateTag();
                if (tag.contains("inventory")) {
                    System.out.println("Found inventory when opening the backpack gui");
                    data.writeNbt(tag.getCompound("inventory"));
                    data.writeItemStack(itemInHand, false);
                }
                return new TestBackpackGUIMenu(pContainerId, pPlayerInventory, data);
            }
        });
    }
}
