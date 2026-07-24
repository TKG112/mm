package net.tkg.ModernMayhem.server.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tacz.guns.util.RenderHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.tkg.ModernMayhem.client.thermal.render.ThermalRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderHelper.class, remap = false)
public class RenderHelperArmMixin {

    @Inject(method = "renderFirstPersonArm", at = @At("HEAD"), remap = false)
    private static void mm$armToThermalMask(LocalPlayer player, HumanoidArm hand, PoseStack matrixStack, int combinedLight, CallbackInfo ci) {
        try {
            ThermalRenderer.stageArmForMask(player, hand, matrixStack);
        } catch (Throwable ignored) {
        }
    }
}