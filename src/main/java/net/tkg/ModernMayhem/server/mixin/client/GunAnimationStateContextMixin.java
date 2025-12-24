package net.tkg.ModernMayhem.server.mixin.client;

import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.IAmmoBox;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.client.animation.statemachine.GunAnimationStateContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.tkg.ModernMayhem.server.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GunAnimationStateContext.class, remap = false)
public class GunAnimationStateContextMixin {

    @Shadow
    private ItemStack currentGunItem;

    @Shadow
    private IGun iGun;

    @Inject(method = "hasAmmoToConsume", at = @At("RETURN"), cancellable = true)
    private void checkRigForAmmoOnClient(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        if (iGun != null && iGun.useDummyAmmo(currentGunItem)) {
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

        if (!modernMayhem$canRigSupplyAmmo(rigItem)) {
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

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof IAmmo iAmmo && iAmmo.isAmmoOfGun(currentGunItem, stack)) {
                return true;
            }

            if (stack.getItem() instanceof IAmmoBox iBox && iBox.isAmmoBoxOfGun(currentGunItem, stack)) {
                int boxAmmo = iBox.getAmmoCount(stack);
                if (boxAmmo > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    @Unique
    private boolean modernMayhem$canRigSupplyAmmo(ItemStack rigItem) {
        if (rigItem.getItem() instanceof GenericBackpackItem backpackItem) {
            return backpackItem.canSupplyAmmo();
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
}