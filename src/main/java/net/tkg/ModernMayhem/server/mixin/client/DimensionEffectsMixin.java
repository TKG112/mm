package net.tkg.ModernMayhem.server.mixin.client;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import net.tkg.ModernMayhem.client.Darkness;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.class)
public class DimensionEffectsMixin {

    @Mixin(DimensionSpecialEffects.NetherEffects.class)
    public static class NetherMixin {
        @Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
        private void inject$brightFogColor(CallbackInfoReturnable<Vec3> cir) {
            if (Darkness.Config.getMode() == Darkness.DarkMode.VANILLA || !Darkness.Config.getOnNether()) return;
            cir.setReturnValue(Darkness.getFogColor(cir.getReturnValue(), Darkness.Config.getNetherFogBright()));
        }
    }

    @Mixin(DimensionSpecialEffects.EndEffects.class)
    public static class EndMixin {
        @Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
        private void inject$brightFogColor(CallbackInfoReturnable<Vec3> cir) {
            if (Darkness.Config.getMode() == Darkness.DarkMode.VANILLA || !Darkness.Config.getOnEnd()) return;
            cir.setReturnValue(Darkness.getFogColor(cir.getReturnValue(), Darkness.Config.getEndFogBright()));
        }
    }
}
