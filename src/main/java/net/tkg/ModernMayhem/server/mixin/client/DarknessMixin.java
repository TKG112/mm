package net.tkg.ModernMayhem.server.mixin.client;

import me.srrapero720.chloride.impl.Darkness;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
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

        // Only cancel Chloride darkness if NVG is enabled
        if (GenericNVGGogglesItem.isNVGEnabled(player)) {
            Darkness.enabled = false;
            ci.cancel(); // Prevent Chloride from updating luminance
        }
    }
}
