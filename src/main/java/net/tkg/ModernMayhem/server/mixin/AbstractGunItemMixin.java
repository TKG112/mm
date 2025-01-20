package net.tkg.ModernMayhem.server.mixin;

import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractGunItem.class)
public abstract class AbstractGunItemMixin implements IGun {


    // Mixin method to modify original one to also check if the player has ammo in their inventory (like ammo pouches)
    @Inject(
            method = "canReload",
            remap = false,
            cancellable = true,
            at = @At("HEAD")
    )
    public void mm_canReload(LivingEntity shooter, ItemStack gunItem, CallbackInfoReturnable<Boolean> cir) {

    }

    // Mixin method to modify original one to also remove ammo if the player has ammo in their inventory (like ammo pouches)
    @Inject(
            method = "findAndExtractInventoryAmmos",
            remap = false,
            cancellable = true,
            at = @At("HEAD")
    )
    public void mm_findAndExtractInventoryAmmos(IItemHandler itemHandler, ItemStack gunItem, int needAmmoCount, CallbackInfoReturnable<Integer> cir) {

    }

    // Mixin method to modify original one to also remove ammo if the player has ammo in their inventory (like ammo pouches)
    @Inject(
            method = "findAndExtractDummyAmmo",
            remap = false,
            cancellable = true,
            at = @At("RETURN")
    )
    public void mm_findAndExtractDummyAmmo(ItemStack gunItem, int needAmmoCount, CallbackInfoReturnable<Integer> cir) {

    }
}
