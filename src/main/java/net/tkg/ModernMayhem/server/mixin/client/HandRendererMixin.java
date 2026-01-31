package net.tkg.ModernMayhem.server.mixin.client;

import net.irisshaders.iris.pathways.HandRenderer;
import net.tkg.ModernMayhem.client.compat.oculus.OculusCompat;
import net.tkg.ModernMayhem.client.event.RenderNVGFirstPerson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandRenderer.class)
public class HandRendererMixin {

    @Inject(method = "isAnyHandTranslucent", at = @At("HEAD"), cancellable = true, remap = false)
    private void forceTranslucentPassForVisor(CallbackInfoReturnable<Boolean> cir) {
        if (RenderNVGFirstPerson.shouldRender()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "renderTranslucent", at = @At("HEAD"), remap = false)
    private void setTranslucentFlagStart(CallbackInfo ci) {
        OculusCompat.isTranslucentHandPass = true;
    }

    @Inject(method = "renderTranslucent", at = @At("RETURN"), remap = false)
    private void setTranslucentFlagEnd(CallbackInfo ci) {
        OculusCompat.isTranslucentHandPass = false;
    }

    @Inject(method = "renderSolid", at = @At("HEAD"), remap = false)
    private void setSolidFlagStart(CallbackInfo ci) {
        OculusCompat.isSolidHandPass = true;
    }

    @Inject(method = "renderSolid", at = @At("RETURN"), remap = false)
    private void setSolidFlagEnd(CallbackInfo ci) {
        OculusCompat.isSolidHandPass = false;
    }
}