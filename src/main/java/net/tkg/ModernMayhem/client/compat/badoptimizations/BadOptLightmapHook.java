package net.tkg.ModernMayhem.client.compat.badoptimizations;

import java.util.function.BooleanSupplier;

public final class BadOptLightmapHook implements BooleanSupplier {

    public static volatile boolean LIGHTMAP_DRIVEN = false;

    private int linger = 0;

    @Override
    public boolean getAsBoolean() {
        if (LIGHTMAP_DRIVEN) {
            linger = 3;
            return true;
        }
        if (linger > 0) {
            linger--;
            return true;
        }
        return false;
    }
}