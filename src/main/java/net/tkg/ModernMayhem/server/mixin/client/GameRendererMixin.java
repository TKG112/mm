package net.tkg.ModernMayhem.server.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.tkg.ModernMayhem.client.Darkness;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final public LightTexture lightTexture;
    @Shadow @Final public Minecraft minecraft;

    @Inject(method = "renderLevel", at = @At(value = "HEAD"))
    private void inject$renderLevel(float tickDelta, long nanos, PoseStack matrixStack, CallbackInfo ci) {
        // Cast to accessor to check the flag
        boolean shouldUpdate = ((LightTextureAccessor) this.lightTexture).getUpdateLightTexture();

        if (shouldUpdate) {
            this.minecraft.getProfiler().push("darkenLightTexture");

            // Cast to accessor to get the flicker value
            float flicker = ((LightTextureAccessor) this.lightTexture).getBlockLightRedFlicker();

            Darkness.updateLuminance(tickDelta, this.minecraft, (GameRenderer) (Object) this, flicker);
            this.minecraft.getProfiler().pop();
        }
    }
}
