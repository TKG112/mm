package net.tkg.ModernMayhem.client.compat.ar;

import com.github.argon4w.acceleratedrendering.features.entities.AcceleratedEntityRenderingFeature;
import com.github.argon4w.acceleratedrendering.features.items.AcceleratedItemRenderingFeature;
import com.github.argon4w.acceleratedrendering.features.text.AcceleratedTextRenderingFeature;

public class ARCompatImpl {
    public static void disableAcceleration() {
        AcceleratedEntityRenderingFeature.useVanillaPipeline();
        AcceleratedItemRenderingFeature.useVanillaPipeline();
        AcceleratedTextRenderingFeature.useVanillaPipeline();
    }

    public static void resetAcceleration() {
        AcceleratedEntityRenderingFeature.resetPipeline();
        AcceleratedItemRenderingFeature.resetPipeline();
        AcceleratedTextRenderingFeature.resetPipeline();
    }
}