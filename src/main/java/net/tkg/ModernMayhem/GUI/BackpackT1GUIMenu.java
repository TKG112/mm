package net.tkg.ModernMayhem.GUI;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.tkg.ModernMayhem.registry.GUIRegistryMM;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class BackpackT1GUIMenu extends GenericBackpackGUI{

    public BackpackT1GUIMenu(int pContainerId, Inventory pPlayerInventory, ICuriosItemHandler pPlayerCuriosInventory, FriendlyByteBuf data) {
        super(GUIRegistryMM.BACKPACKT1_GUI.get(), pContainerId, 3, 3, pPlayerInventory, pPlayerCuriosInventory, data);
    }

    public BackpackT1GUIMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf data) {
        super(GUIRegistryMM.BACKPACKT1_GUI.get(), pContainerId, 3, 3, pPlayerInventory, null, data);
    }

    @Override
    public int getSlotPerLine() { return 3; }

    @Override
    public int getNumberOfLine() { return 3; }
}
