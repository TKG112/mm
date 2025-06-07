package net.tkg.ModernMayhem.server.mixin.client;

import me.srrapero720.chloride.impl.Darkness;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Darkness.class, remap = false)
public class DarknessMixin {

    @Inject(method = "updateLuminance", at = @At("HEAD"), cancellable = true)
    private static void onUpdateLuminance(float tickDelta, Minecraft client, GameRenderer gameRenderer, float prevFlicker, CallbackInfo ci) {
        Player player = client.player;
        if (player == null) return;

        ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
        if (facewearItem == null) return;

        if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
            if (GenericNVGGogglesItem.getNVGCheck(facewearItem) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
                Darkness.enabled = false;
                ci.cancel();
            }
        }
    }
}
