package net.tkg.ModernMayhem.server.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.tkg.ModernMayhem.client.Darkness;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DynamicTexture.class)
public class DynamicTextureMixin implements Darkness.DynamicTextureHook {
    @Shadow private NativeImage pixels;
    @Unique private boolean darkness$enabled;

    @Inject(method = "upload", at = @At(value = "HEAD"))
    private void inject$onUpload(CallbackInfo ci) {
        if (!Darkness.enabled || !this.darkness$enabled) return;

        if (Darkness.tvgActive) {
            for (int s = 0; s < 16; s++) {
                int brightColor = this.pixels.getPixelRGBA(15, s);
                for (int b = 0; b < 15; b++) {
                    this.pixels.setPixelRGBA(b, s, brightColor);
                }
            }
        }
        else {
            for (int b = 0; b < 16; b++) {
                for (int s = 0; s < 16; s++) {
                    final int color = Darkness.darken(this.pixels.getPixelRGBA(b, s), b, s);
                    this.pixels.setPixelRGBA(b, s, color);
                }
            }
        }
    }

    @Override
    public void darkness$enableDarkness() {
        this.darkness$enabled = true;
    }
}