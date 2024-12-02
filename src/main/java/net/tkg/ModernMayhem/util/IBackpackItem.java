package net.tkg.ModernMayhem.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IBackpackItem {

    void OpenGUI(Player pPlayer, ItemStack pStack, ItemStack itemInHand);
}
