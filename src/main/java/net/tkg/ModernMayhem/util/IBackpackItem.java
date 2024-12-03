package net.tkg.ModernMayhem.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IBackpackItem {

    void OpenGUIFromPlayerInventory(Player pPlayer, ItemStack pStack);

    void OpenGUIFromCuriosInventory(Player pPlayer, ItemStack pStack);
}
