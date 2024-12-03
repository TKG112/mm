package net.tkg.ModernMayhem.GUI;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.tkg.ModernMayhem.registry.GUIRegistryMM;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class BackpackT2GUIMenu extends GenericBackpackGUI{

    public BackpackT2GUIMenu(int pContainerId, Inventory pPlayerInventory, ICuriosItemHandler pPlayerCurioInventory, FriendlyByteBuf data) {
        super(GUIRegistryMM.BACKPACKT2_GUI.get(), pContainerId, 3, 6, pPlayerInventory, pPlayerCurioInventory, data);
    }

    public BackpackT2GUIMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf data) {
        super(GUIRegistryMM.BACKPACKT2_GUI.get(), pContainerId, 3, 6, pPlayerInventory, null, data);
    }

    @Override
    public int getSlotPerLine() {
        return 6;
    }

    @Override
    public int getNumberOfLine() {
        return 3;
    }
}
