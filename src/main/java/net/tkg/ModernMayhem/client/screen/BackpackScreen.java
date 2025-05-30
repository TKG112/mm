package net.tkg.ModernMayhem.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.GUI.GenericBackpackGUI;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.max;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class BackpackScreen extends AbstractContainerScreen<GenericBackpackGUI> {
    ResourceLocation BACKGROUND_TEXTURE = fromNamespaceAndPath(ModernMayhemMod.ID, "textures/gui/container/generic_backpack_inventory.png");

    // Making variables to store some values
    public static final int playerInventoryLeftPos = 0;
    public static final int playerInventoryTopPos = 0;
    public static final int playerInventoryWidth = 176;
    public static final int playerInventoryHeight = 90;

    public static final int slotLeftPos = 7;
    public static final int slotTopPos = 7;
    public static final int slotSize = 18;

    public static final int cornerNWLeftPos = 178;
    public static final int cornerNWTopPos = 0;
    public static final int cornerNWWidth = 7;
    public static final int cornerNWHeight = 7;

    public static final int cornerNELeftPos = 207;
    public static final int cornerNETopPos = 0;
    public static final int cornerNEWidth = 7;
    public static final int cornerNEHeight = 7;

    public static final int cornerSWLeftPos = 178;
    public static final int cornerSWTopPos = 29;
    public static final int cornerSWWidth = 7;
    public static final int cornerSWHeight = 7;

    public static final int cornerSELeftPos = 207;
    public static final int cornerSETopPos = 29;
    public static final int cornerSEWidth = 7;
    public static final int cornerSEHeight = 7;

    public static final int topBorderLeftPos = 187;
    public static final int topBorderTopPos = 0;
    public static final int topBorderWidth = slotSize;
    public static final int topBorderHeight = 7;

    public static final int bottomBorderLeftPos = 187;
    public static final int bottomBorderTopPos = 29;
    public static final int bottomBorderWidth = slotSize;
    public static final int bottomBorderHeight = 7;

    public static final int leftBorderLeftPos = 178;
    public static final int leftBorderTopPos = 9;
    public static final int leftBorderWidth = 7;
    public static final int leftBorderHeight = slotSize;

    public static final int rightBorderLeftPos = 207;
    public static final int rightBorderTopPos = 9;
    public static final int rightBorderWidth = 7;
    public static final int rightBorderHeight = slotSize;

    public int slotPerLine = 0;
    public int numberOfLine = 0;
    public int slotsVerticalSize = 0;
    public int slotsHorizontalSize = 0;
    public int backpackPartWidth = 0;
    public int backpackPartHeight = 0;

    public BackpackScreen(GenericBackpackGUI pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.slotPerLine = menu.getSlotPerLine();
        this.numberOfLine = menu.getNumberOfLine();
        this.slotsVerticalSize = slotSize * numberOfLine;
        this.slotsHorizontalSize = slotSize * slotPerLine;

        this.backpackPartWidth = max(cornerNWWidth, cornerSWWidth) + slotsHorizontalSize + max(cornerNEWidth, cornerSEWidth);
        this.backpackPartHeight = max(cornerNWHeight, cornerNEHeight) + slotsVerticalSize;

        this.imageWidth = max(backpackPartWidth, playerInventoryWidth);
        this.imageHeight = backpackPartHeight + playerInventoryHeight + 3;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {}

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pGuiGraphics);
        renderBackpackInventory(
                pGuiGraphics
        );
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTicks) {

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTicks);

        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }


    private void renderBackpackInventory(GuiGraphics pGuiGraphics) {
        int backpackLeftPos = this.leftPos + (imageWidth - backpackPartWidth) / 2;

        // Render the corners
        pGuiGraphics.blit( // NW
                BACKGROUND_TEXTURE,
                backpackLeftPos,
                topPos,
                cornerNWLeftPos,
                cornerNWTopPos,
                cornerNWWidth,
                cornerNWHeight
        );
        pGuiGraphics.blit( // NE
                BACKGROUND_TEXTURE,
                backpackLeftPos + cornerNWWidth + slotsHorizontalSize,
                topPos,
                cornerNELeftPos,
                cornerNETopPos,
                cornerNEWidth,
                cornerNEHeight
        );
        pGuiGraphics.blit( // SW
                BACKGROUND_TEXTURE,
                backpackLeftPos,
                topPos + cornerNWHeight + slotsVerticalSize,
                cornerSWLeftPos,
                cornerSWTopPos,
                cornerSWWidth,
                cornerSWHeight
        );
        pGuiGraphics.blit( // SE
                BACKGROUND_TEXTURE,
                backpackLeftPos + cornerNWWidth + slotsHorizontalSize,
                topPos + cornerNWHeight + slotsVerticalSize,
                cornerSELeftPos,
                cornerSETopPos,
                cornerSEWidth,
                cornerSEHeight
        );

        // Render the borders
        for (int i = 0; i < slotPerLine; i++) {
            pGuiGraphics.blit(BACKGROUND_TEXTURE,
                    backpackLeftPos + cornerNWWidth + i * slotSize,
                    topPos,
                    topBorderLeftPos,
                    topBorderTopPos,
                    topBorderWidth,
                    topBorderHeight
            );
            pGuiGraphics.blit(BACKGROUND_TEXTURE,
                    backpackLeftPos + cornerNWWidth + i * slotSize,
                    topPos + cornerNWHeight + slotsVerticalSize,
                    bottomBorderLeftPos,
                    bottomBorderTopPos,
                    bottomBorderWidth,
                    bottomBorderHeight
            );
        }
        for (int i = 0; i < numberOfLine; i++) {
            pGuiGraphics.blit(
                    BACKGROUND_TEXTURE,
                    backpackLeftPos,
                    topPos + cornerNWHeight + i * slotSize,
                    leftBorderLeftPos,
                    leftBorderTopPos,
                    leftBorderWidth,
                    leftBorderHeight
            );
            pGuiGraphics.blit(
                    BACKGROUND_TEXTURE,
                    backpackLeftPos + cornerNWWidth + slotsHorizontalSize,
                    topPos + cornerNEHeight + i * slotSize,
                    rightBorderLeftPos,
                    rightBorderTopPos,
                    rightBorderWidth,
                    rightBorderHeight
            );
        }

        // Render the backpack slots
        for (int line = 0; line < numberOfLine; line++) {
            for (int column = 0; column < slotPerLine; column++) {
                int x = backpackLeftPos + cornerNWWidth + column * slotSize;
                int y = topPos + cornerNWHeight + line * slotSize;
                pGuiGraphics.blit(
                        BACKGROUND_TEXTURE,
                        x,
                        y,
                        slotLeftPos,
                        slotTopPos,
                        slotSize,
                        slotSize
                );
            }
        }

        // Render the player inventory background texture
        pGuiGraphics.blit(
                BACKGROUND_TEXTURE,
                leftPos + (imageWidth - playerInventoryWidth) / 2,
                topPos + imageHeight - playerInventoryHeight,
                playerInventoryTopPos,
                playerInventoryLeftPos,
                playerInventoryWidth,
                playerInventoryHeight
        );
    }
}
