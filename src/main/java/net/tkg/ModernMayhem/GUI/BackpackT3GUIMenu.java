package net.tkg.ModernMayhem.GUI;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.tkg.ModernMayhem.registry.GUIRegistryMM;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class BackpackT3GUIMenu extends GenericBackpackGUI{

    public BackpackT3GUIMenu(int pContainerId, Inventory pPlayerInventory, ICuriosItemHandler pPlayerCurioInventory, FriendlyByteBuf data) {
        super(GUIRegistryMM.BACKPACKT3_GUI.get(), pContainerId, 3, 9, pPlayerInventory, pPlayerCurioInventory, data);
    }

    public BackpackT3GUIMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf data) {
        super(GUIRegistryMM.BACKPACKT3_GUI.get(), pContainerId, 3, 9, pPlayerInventory, null, data);
    }

    @Override
    public int getSlotPerLine() {
        return 9;
    }

    @Override
    public int getNumberOfLine() {
        return 3;
    }
}
