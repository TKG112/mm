package net.tkg.ModernMayhem.server.mixin.client;

import com.tacz.guns.client.gui.overlay.GunHudOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GunHudOverlay.class)
public interface GunHudOverlayAccessor {
    @Accessor("cacheInventoryAmmoCount")
    static int getCacheInventoryAmmoCount() {
        throw new AssertionError();
    }

    @Accessor("cacheInventoryAmmoCount")
    static void setCacheInventoryAmmoCount(int value) {
        throw new AssertionError();
    }
}
