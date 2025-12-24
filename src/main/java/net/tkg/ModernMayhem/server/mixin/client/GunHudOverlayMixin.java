package net.tkg.ModernMayhem.server.mixin.client;

import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.IAmmoBox;
import com.tacz.guns.client.gui.overlay.GunHudOverlay;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.tkg.ModernMayhem.server.item.generic.GenericBackpackItem; // [NEW] Import this
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(GunHudOverlay.class)
public class GunHudOverlayMixin {

    @Inject(method = "handleInventoryAmmo", at = @At("RETURN"), remap = false)
    private static void addRigAmmo(ItemStack gunStack, Inventory inventory, CallbackInfo ci) {
        Player player = inventory.player;

        ItemStack rigItem = CuriosUtil.getRigItem(player);
        if (rigItem == null || rigItem.isEmpty()) return;

        if (!modernMayhem$canRigSupplyAmmo(rigItem)) return;

        int size = modernMayhem$getInventorySizeFromRig(rigItem);
        if (size <= 0) return;

        CompoundTag tag = rigItem.getTag();
        if (tag == null || !tag.contains("inventory")) return;

        ItemStackHandler inventoryHandler = new ItemStackHandler(size);
        inventoryHandler.deserializeNBT(tag.getCompound("inventory"));

        for (int i = 0; i < inventoryHandler.getSlots(); i++) {
            ItemStack slotStack = inventoryHandler.getStackInSlot(i);
            if (slotStack.isEmpty()) continue;

            Item item = slotStack.getItem();

            if (item instanceof IAmmo iAmmo && iAmmo.isAmmoOfGun(gunStack, slotStack)) {
                GunHudOverlayAccessor.setCacheInventoryAmmoCount(
                        GunHudOverlayAccessor.getCacheInventoryAmmoCount() + slotStack.getCount()
                );
            }

            if (item instanceof IAmmoBox iBox && iBox.isAmmoBoxOfGun(gunStack, slotStack)) {
                if (iBox.isAllTypeCreative(slotStack) || iBox.isCreative(slotStack)) {
                    GunHudOverlayAccessor.setCacheInventoryAmmoCount(9999);
                    return;
                }

                GunHudOverlayAccessor.setCacheInventoryAmmoCount(
                        GunHudOverlayAccessor.getCacheInventoryAmmoCount() + iBox.getAmmoCount(slotStack)
                );
            }
        }
    }

    @Unique
    private static boolean modernMayhem$canRigSupplyAmmo(ItemStack rigItem) {
        if (rigItem.getItem() instanceof GenericBackpackItem backpackItem) {
            return backpackItem.canSupplyAmmo();
        }
        return false;
    }

    @Unique
    private static int modernMayhem$getInventorySizeFromRig(ItemStack rigItem) {
        if (rigItem.getItem() instanceof GenericBackpackItem backpackItem) {
            return backpackItem.getInventorySize();
        }
        return -1;
    }
}