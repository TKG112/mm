package net.tkg.ModernMayhem.server.mixin.client;

import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LightTexture.class)
public interface LightTextureAccessor {
    @Accessor("updateLightTexture")
    boolean getUpdateLightTexture();

    @Accessor("blockLightRedFlicker")
    float getBlockLightRedFlicker();
}
