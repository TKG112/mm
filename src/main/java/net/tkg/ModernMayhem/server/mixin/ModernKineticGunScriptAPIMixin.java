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
import net.tkg.ModernMayhem.server.item.generic.GenericBackpackItem;
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

    @Inject(method = "hasAmmoToConsume", at = @At("RETURN"), cancellable = true)
    private void checkRigForAmmoToConsume(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }

        if (!isReloadingNeedConsumeAmmo()) {
            return;
        }

        if (abstractGunItem.useDummyAmmo(itemStack)) {
            return;
        }

        if (!(shooter instanceof Player player)) {
            return;
        }

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

        if (!modernMayhem$canRigSupplyAmmo(rigItem)) return false;

        int size = modernMayhem$getRigInventorySize(rigItem);
        if (size <= 0) return false;

        CompoundTag tag = rigItem.getTag();
        if (tag == null || !tag.contains("inventory")) {
            return false;
        }

        ItemStackHandler inventory = new ItemStackHandler(size);
        inventory.deserializeNBT(tag.getCompound("inventory"));

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
        if (rigItem.getItem() instanceof GenericBackpackItem backpackItem) {
            return backpackItem.getInventorySize();
        }
        return -1;
    }

    @Unique
    private boolean modernMayhem$canRigSupplyAmmo(ItemStack rigItem) {
        if (rigItem.getItem() instanceof GenericBackpackItem backpackItem) {
            return backpackItem.canSupplyAmmo();
        }
        return false;
    }
}