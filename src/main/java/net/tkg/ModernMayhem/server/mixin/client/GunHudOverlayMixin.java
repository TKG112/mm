package net.tkg.ModernMayhem.server.mixin.client;

import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.IAmmoBox;
import com.tacz.guns.client.gui.overlay.GunHudOverlay;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.tkg.ModernMayhem.server.item.curios.body.PlateCarrierItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(GunHudOverlay.class)
public class GunHudOverlayMixin {

    @Inject(method = "handleInventoryAmmo", at = @At("RETURN"), remap = false)
    private static void addChestplateBundleAmmo(ItemStack gunStack, Inventory inventory, CallbackInfo ci) {
        Player player = inventory.player;

        ItemStack rigItem = CuriosUtil.getRigItem(player);
        if (rigItem == null || rigItem.isEmpty() || !(rigItem.getItem() instanceof PlateCarrierItem carrier)) {
            return;
        }

        if (!"ammo".equals(carrier.getType())) {
            return;
        }

        CompoundTag tag = rigItem.getTag();
        if (tag == null || !tag.contains("inventory")) return;

        int size = PlateCarrierItem.getNumberofLinesPlateCarrier(carrier.getType()) *
                PlateCarrierItem.getNumberofCollumnsPlateCarrier(carrier.getType());

        ItemStackHandler inventoryHandler = new ItemStackHandler(size);
        inventoryHandler.deserializeNBT(tag.getCompound("inventory"));

        for (int i = 0; i < inventoryHandler.getSlots(); i++) {
            ItemStack slotStack = inventoryHandler.getStackInSlot(i);

            if (slotStack.isEmpty()) continue;

            if (slotStack.getItem() instanceof IAmmo iAmmo && iAmmo.isAmmoOfGun(gunStack, slotStack)) {
                GunHudOverlayAccessor.setCacheInventoryAmmoCount(
                        GunHudOverlayAccessor.getCacheInventoryAmmoCount() + slotStack.getCount()
                );
            }

            if (slotStack.getItem() instanceof IAmmoBox iBox && iBox.isAmmoBoxOfGun(gunStack, slotStack)) {
                GunHudOverlayAccessor.setCacheInventoryAmmoCount(
                        GunHudOverlayAccessor.getCacheInventoryAmmoCount() + iBox.getAmmoCount(slotStack)
                );
            }
        }
    }
}
