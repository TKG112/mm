package net.tkg.ModernMayhem.client.compat.ar;

import net.minecraftforge.fml.ModList;

public class ARCompat {
    public static final String MOD_ID = "acceleratedrendering";
    private static boolean LOADED;

    public static void init() {
        LOADED = ModList.get().isLoaded(MOD_ID);
    }

    public static void disableAcceleration() {
        if (LOADED) {
            try {
                ARCompatImpl.disableAcceleration();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static void resetAcceleration() {
        if (LOADED) {
            try {
                ARCompatImpl.resetAcceleration();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}