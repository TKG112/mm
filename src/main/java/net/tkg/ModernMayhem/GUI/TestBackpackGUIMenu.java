package net.tkg.ModernMayhem.GUI;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.tkg.ModernMayhem.registry.GUIRegistryMM;

public class TestBackpackGUIMenu extends GenericBackpackGUI{
    public TestBackpackGUIMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf data) {
        super(GUIRegistryMM.TESTBACKPACK_GUI.get(), pContainerId, 3, 3, pPlayerInventory);
    }

    public TestBackpackGUIMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, null);
    }

    @Override
    public int getSlotPerLine() {
        return 3;
    }

    @Override
    public int getNumberOfLine() {
        return 3;
    }
}
