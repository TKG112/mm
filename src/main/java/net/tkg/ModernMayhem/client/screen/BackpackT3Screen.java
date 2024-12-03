package net.tkg.ModernMayhem.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.tkg.ModernMayhem.GUI.BackpackT3GUIMenu;
import org.jetbrains.annotations.NotNull;

public class BackpackT3Screen extends AbstractContainerScreen<BackpackT3GUIMenu> implements GenericBackpackScreen{
    public BackpackT3Screen(BackpackT3GUIMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);


        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pGuiGraphics);
        renderBackpackInventory(
                pGuiGraphics,
                this.leftPos,
                this.topPos,
                this.menu.getNumberOfLine(),
                this.menu.getSlotPerLine(),
                this.imageWidth,
                this.imageHeight
        );
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTicks) {

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTicks);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
