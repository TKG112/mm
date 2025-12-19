package net.tkg.ModernMayhem.server.mixin;

import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.IAmmoBox;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.tkg.ModernMayhem.server.item.curios.body.BandoleerItem;
import net.tkg.ModernMayhem.server.item.curios.body.PlateCarrierItem;
import net.tkg.ModernMayhem.server.item.curios.body.ReconRigItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModernKineticGunScriptAPI.class, remap = false)
public class ModernKineticGunScriptAPIMixin {

    @Shadow
    private LivingEntity shooter;

    @Shadow
    private ItemStack itemStack;

    @Shadow
    private AbstractGunItem abstractGunItem;

    @Shadow
    public boolean isReloadingNeedConsumeAmmo() {
        return false;
    }

    // Inject into hasAmmoToConsume to also check rig inventory
    @Inject(method = "hasAmmoToConsume", at = @At("RETURN"), cancellable = true)
    private void checkRigForAmmoToConsume(CallbackInfoReturnable<Boolean> cir) {
        // If the original check already found ammo, we're done
        if (cir.getReturnValue()) {
            return;
        }

        // Only check rig if we need to consume ammo
        if (!isReloadingNeedConsumeAmmo()) {
            return;
        }

        // Don't interfere with dummy ammo logic
        if (abstractGunItem.useDummyAmmo(itemStack)) {
            return;
        }

        if (!(shooter instanceof Player player)) {
            return;
        }

        // Check if there's ammo in the rig
        boolean hasRigAmmo = modernMayhem$checkRigHasAmmo(player);

        if (hasRigAmmo) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    private boolean modernMayhem$checkRigHasAmmo(Player player) {
        ItemStack rigItem = CuriosUtil.getRigItem(player);
        if (rigItem == null || rigItem.isEmpty()) {
            return false;
        }

        int size = modernMayhem$getRigInventorySize(rigItem);
        if (size <= 0) {
            return false;
        }

        CompoundTag tag = rigItem.getTag();
        if (tag == null || !tag.contains("inventory")) {
            return false;
        }

        ItemStackHandler inventory = new ItemStackHandler(size);
        inventory.deserializeNBT(tag.getCompound("inventory"));

        // Check if there's any compatible ammo in the rig
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof IAmmo iAmmo && iAmmo.isAmmoOfGun(itemStack, stack)) {
                return true;
            }

            if (stack.getItem() instanceof IAmmoBox iBox && iBox.isAmmoBoxOfGun(itemStack, stack)) {
                int boxAmmo = iBox.getAmmoCount(stack);
                if (boxAmmo > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    @Unique
    private int modernMayhem$getRigInventorySize(ItemStack rigItem) {
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