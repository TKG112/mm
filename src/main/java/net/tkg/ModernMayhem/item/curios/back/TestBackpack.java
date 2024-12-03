package net.tkg.ModernMayhem.item.curios.back;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.GUI.TestBackpackGUIMenu;
import net.tkg.ModernMayhem.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.util.CuriosUtil;
import org.jetbrains.annotations.Nullable;

public class TestBackpack extends GenericBackpackItem {
    public TestBackpack() {
        super(3, 3);
    }

    @Override
    public void OpenGUIFromPlayerInventory(Player pPlayer, ItemStack pStack) {
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
                    data.writeBoolean(false);
                    data.writeItemStack(pStack, false);
                }
                return new TestBackpackGUIMenu(pContainerId, pPlayerInventory, data);
            }
        });
    }

    @Override
    public void OpenGUIFromCuriosInventory(Player pPlayer, ItemStack pStack) {
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
                    data.writeBoolean(true);
                    data.writeItemStack(pStack, false);
                    data.writeInt(CuriosUtil.getBackpackSlotID(pPlayer));
                }
                return new TestBackpackGUIMenu(pContainerId, pPlayerInventory, data);
            }
        });
    }
}
