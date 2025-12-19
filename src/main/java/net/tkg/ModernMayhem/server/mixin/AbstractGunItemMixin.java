package net.tkg.ModernMayhem.server.mixin;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.IAmmoBox;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.util.AttachmentDataUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.tkg.ModernMayhem.server.item.curios.body.BandoleerItem;
import net.tkg.ModernMayhem.server.item.curios.body.PlateCarrierItem;
import net.tkg.ModernMayhem.server.item.curios.body.ReconRigItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AbstractGunItem.class)
public class AbstractGunItemMixin {

    @Unique
    private static final ThreadLocal<Boolean> EXTRACTING_FROM_RIG = ThreadLocal.withInitial(() -> false);

    @Unique
    private static final ThreadLocal<Player> CURRENT_RELOADING_PLAYER = ThreadLocal.withInitial(() -> null);

    // Check rig for ammo after normal checks fail
    @Inject(method = "canReload", at = @At("RETURN"), cancellable = true, remap = false)
    private void checkRigForAmmo(LivingEntity shooter, ItemStack gunItem, CallbackInfoReturnable<Boolean> cir) {
        // Only proceed if the original method returned false
        if (cir.getReturnValue()) {
            return;
        }

        if (!(shooter instanceof Player player)) return;

        ResourceLocation gunId = ((AbstractGunItem)(Object)this).getGunId(gunItem);
        Optional<CommonGunIndex> optionalIndex = TimelessAPI.getCommonGunIndex(gunId);
        if (optionalIndex.isEmpty()) return;

        CommonGunIndex gunIndex = optionalIndex.get();
        int currentAmmoCount = ((AbstractGunItem)(Object)this).getCurrentAmmoCount(gunItem);
        int maxAmmoCount = AttachmentDataUtils.getAmmoCountWithAttachment(gunItem, gunIndex.getGunData());

        // Don't reload if already full
        if (currentAmmoCount >= maxAmmoCount) {
            cir.setReturnValue(false);
            return;
        }

        ItemStack rigItem = CuriosUtil.getRigItem(player);
        if (rigItem == null || rigItem.isEmpty()) return;

        int size = getRigInventorySize(rigItem);
        if (size <= 0) return;

        CompoundTag tag = rigItem.getTag();
        if (tag == null || !tag.contains("inventory")) return;

        ItemStackHandler inventory = new ItemStackHandler(size);
        inventory.deserializeNBT(tag.getCompound("inventory"));

        // Check if there's any compatible ammo in the rig
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof IAmmo iAmmo && iAmmo.isAmmoOfGun(gunItem, stack)) {
                // Set the current player so extraction can find them
                CURRENT_RELOADING_PLAYER.set(player);
                cir.setReturnValue(true);
                return;
            }

            if (stack.getItem() instanceof IAmmoBox iBox && iBox.isAmmoBoxOfGun(gunItem, stack)) {
                int boxAmmo = iBox.getAmmoCount(stack);
                if (boxAmmo > 0) {
                    // Set the current player so extraction can find them
                    CURRENT_RELOADING_PLAYER.set(player);
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }

    // Extract ammo from rig inventory AFTER normal inventory extraction
    @Inject(method = "findAndExtractInventoryAmmo", at = @At("RETURN"), cancellable = true, remap = false)
    private void extractAmmoFromRigAfter(IItemHandler itemHandler, ItemStack gunItem, int needAmmoCount, CallbackInfoReturnable<Integer> cir) {
        // Prevent recursion - if we're already extracting from rig, don't do it again
        if (EXTRACTING_FROM_RIG.get()) {
            return;
        }

        int alreadyFound = cir.getReturnValue();
        int remainingToFind = needAmmoCount - alreadyFound;

        // If we found everything we need, we're done
        if (remainingToFind <= 0) {
            // Clear the reloading player since we're done
            CURRENT_RELOADING_PLAYER.remove();
            return;
        }

        // Get the player from ThreadLocal
        Player player = CURRENT_RELOADING_PLAYER.get();

        if (player == null) {
            return;
        }

        ItemStack rigItem = CuriosUtil.getRigItem(player);
        if (rigItem == null || rigItem.isEmpty()) {
            CURRENT_RELOADING_PLAYER.remove();
            return;
        }

        int size = getRigInventorySize(rigItem);
        if (size <= 0) {
            CURRENT_RELOADING_PLAYER.remove();
            return;
        }

        CompoundTag tag = rigItem.getOrCreateTag();
        if (!tag.contains("inventory")) {
            CURRENT_RELOADING_PLAYER.remove();
            return;
        }

        ItemStackHandler rigInventory = new ItemStackHandler(size);
        rigInventory.deserializeNBT(tag.getCompound("inventory"));

        // Mark that we're extracting from rig to prevent recursion
        EXTRACTING_FROM_RIG.set(true);
        try {
            // Extract from rig
            int foundInRig = extractFromRig(rigInventory, gunItem, remainingToFind);

            if (foundInRig > 0) {
                // Save the updated rig inventory
                tag.put("inventory", rigInventory.serializeNBT());
                rigItem.setTag(tag);

                // Return the total found
                cir.setReturnValue(alreadyFound + foundInRig);
            }
        } finally {
            // Always reset the flags
            EXTRACTING_FROM_RIG.set(false);
        }
    }

    @Unique
    private int extractFromRig(ItemStackHandler inventory, ItemStack gunItem, int needAmmoCount) {
        MutableInt remaining = new MutableInt(needAmmoCount);
        int found = 0;

        for (int i = 0; i < inventory.getSlots() && remaining.value > 0; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof IAmmo iAmmo && iAmmo.isAmmoOfGun(gunItem, stack)) {
                int toExtract = Math.min(stack.getCount(), remaining.value);
                ItemStack extracted = inventory.extractItem(i, toExtract, false);
                found += extracted.getCount();
                remaining.value -= extracted.getCount();
            } else if (stack.getItem() instanceof IAmmoBox iBox && iBox.isAmmoBoxOfGun(gunItem, stack)) {
                int boxAmmo = iBox.getAmmoCount(stack);
                if (boxAmmo > 0) {
                    int extractCount = Math.min(boxAmmo, remaining.value);
                    iBox.setAmmoCount(stack, boxAmmo - extractCount);
                    if (boxAmmo - extractCount <= 0) {
                        iBox.setAmmoId(stack, DefaultAssets.EMPTY_AMMO_ID);
                    }
                    found += extractCount;
                    remaining.value -= extractCount;
                }
            }
        }

        return found;
    }

    @Unique
    private int getRigInventorySize(ItemStack rigItem) {
        if (rigItem.getItem() instanceof PlateCarrierItem plate) {
            if (!"ammo".equals(plate.getType())) return -1;
            return PlateCarrierItem.getNumberofLinesPlateCarrier(plate.getType())
                    * PlateCarrierItem.getNumberofCollumnsPlateCarrier(plate.getType());
        }

        if (rigItem.getItem() instanceof BandoleerItem) {
            return 6;
        }

        if (rigItem.getItem() instanceof ReconRigItem) {
            return 12;
        }

        return -1;
    }
}