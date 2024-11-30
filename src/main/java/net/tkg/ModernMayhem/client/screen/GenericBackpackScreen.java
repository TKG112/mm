package net.tkg.ModernMayhem.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.tkg.ModernMayhem.GUI.GenericBackpackGUI;
import net.tkg.ModernMayhem.ModernMayhemMod;
import org.jetbrains.annotations.NotNull;

public interface GenericBackpackScreen {

    ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ModernMayhemMod.ID, "textures/gui/container/generic_backpack_inventory.png");

    default void renderBackpackInventory(GuiGraphics pGuiGraphics, int leftPos, int topPos, int nbLine, int nbSlotPerLine, int imageWidth, int imageHeight) {
        // Render the background texture
        pGuiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Render the backpack slots
        for (int line = 0; line < nbLine; line++) {
            for (int column = 0; column < nbSlotPerLine; column++) {
                int x = leftPos + 7 + column * 18;
                int y = topPos + 17 + line * 18;
                pGuiGraphics.blit(BACKGROUND_TEXTURE, x, y, 176, 0, 18, 18);
            }
        }
    }
}
