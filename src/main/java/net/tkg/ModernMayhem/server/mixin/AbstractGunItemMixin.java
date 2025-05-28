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
import net.tkg.ModernMayhem.server.item.curios.body.PlateCarrierItem;
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
    private static final ThreadLocal<Player> RELOADING_PLAYER = new ThreadLocal<>();

    @Inject(method = "canReload", at = @At("HEAD"), remap = false)
    private void capturePlayerBeforeReload(LivingEntity shooter, ItemStack gunItem, CallbackInfoReturnable<Boolean> cir) {
        if (shooter instanceof Player player) {
            RELOADING_PLAYER.set(player);
        }
    }

    @Inject(method = "canReload", at = @At("RETURN"), cancellable = true, remap = false)
    private void checkPlateCarrierForAmmo(LivingEntity shooter, ItemStack gunItem, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }

        // Properly retrieve gunIndex and max ammo count
        ResourceLocation gunId = ((AbstractGunItem)(Object)this).getGunId(gunItem);
        Optional<CommonGunIndex> optionalIndex = TimelessAPI.getCommonGunIndex(gunId);
        if (optionalIndex.isEmpty()) {
            return;
        }

        CommonGunIndex gunIndex = optionalIndex.get();
        int currentAmmoCount = ((AbstractGunItem)(Object)this).getCurrentAmmoCount(gunItem);
        int maxAmmoCount = AttachmentDataUtils.getAmmoCountWithAttachment(gunItem, gunIndex.getGunData());

        if (currentAmmoCount >= maxAmmoCount) {
            cir.setReturnValue(false);
            return;
        }

        if (!(shooter instanceof Player player)) return;

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
        ItemStackHandler inventory = new ItemStackHandler(size);
        inventory.deserializeNBT(tag.getCompound("inventory"));

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);

            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof IAmmo iAmmo && iAmmo.isAmmoOfGun(gunItem, stack)) {
                cir.setReturnValue(true);
                return;
            }

            if (stack.getItem() instanceof IAmmoBox iBox && iBox.isAmmoBoxOfGun(gunItem, stack)) {
                cir.setReturnValue(true);
                return;
            }
        }

    }

    @Inject(method = "findAndExtractInventoryAmmo", at = @At("RETURN"), cancellable = true, remap = false)
    private void extractAmmoFromPlateCarrier(IItemHandler itemHandler, ItemStack gunItem, int needAmmoCount, CallbackInfoReturnable<Integer> cir) {
        int alreadyFound = cir.getReturnValue();
        int remainingToFind = needAmmoCount - alreadyFound;

        if (remainingToFind <= 0) {
            RELOADING_PLAYER.remove();
            return;
        }

        Player player = RELOADING_PLAYER.get();
        RELOADING_PLAYER.remove();
        if (player == null) return;

        ItemStack rigItem = CuriosUtil.getRigItem(player);
        if (rigItem == null || rigItem.isEmpty() || !(rigItem.getItem() instanceof PlateCarrierItem carrier)) {
            return;
        }

        if (!"ammo".equals(carrier.getType())) {
            return;
        }

        CompoundTag tag = rigItem.getOrCreateTag();
        if (!tag.contains("inventory")) return;

        int size = PlateCarrierItem.getNumberofLinesPlateCarrier(carrier.getType()) *
                PlateCarrierItem.getNumberofCollumnsPlateCarrier(carrier.getType());
        ItemStackHandler inventory = new ItemStackHandler(size);
        inventory.deserializeNBT(tag.getCompound("inventory"));

        MutableInt remaining = new MutableInt(remainingToFind);
        int found = 0;

        for (int i = 0; i < inventory.getSlots() && remaining.value > 0; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof IAmmo iAmmo && iAmmo.isAmmoOfGun(gunItem, stack)) {
                int toExtract = Math.min(stack.getCount(), remaining.value);
                ItemStack extracted = inventory.extractItem(i, toExtract, false);
                found += extracted.getCount();
                remaining.value -= extracted.getCount();
            }

            else if (stack.getItem() instanceof IAmmoBox iBox && iBox.isAmmoBoxOfGun(gunItem, stack)) {
                int boxAmmo = iBox.getAmmoCount(stack);
                int extractCount = Math.min(boxAmmo, remaining.value);
                iBox.setAmmoCount(stack, boxAmmo - extractCount);
                if (boxAmmo - extractCount <= 0) {
                    iBox.setAmmoId(stack, DefaultAssets.EMPTY_AMMO_ID);
                }
                found += extractCount;
                remaining.value -= extractCount;
            }
        }

        tag.put("inventory", inventory.serializeNBT());
        rigItem.setTag(tag);

        cir.setReturnValue(alreadyFound + found);
    }
}
