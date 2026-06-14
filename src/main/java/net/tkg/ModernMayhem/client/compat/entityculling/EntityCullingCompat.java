package net.tkg.ModernMayhem.client.compat.entityculling;

import net.minecraft.world.entity.Entity;
import net.tkg.ModernMayhem.ModernMayhemMod;

import java.lang.reflect.Method;

public final class EntityCullingCompat {

    private static boolean resolved = false;
    private static boolean usable = false;
    private static Class<?> cullableClass;
    private static Method isCulledMethod;
    private static Method isForcedVisibleMethod;

    private EntityCullingCompat() {}

    public static boolean isActive() {
        resolve();
        return usable;
    }

    private static void resolve() {
        if (resolved) return;
        resolved = true;
        try {
            cullableClass = Class.forName("dev.tr7zw.entityculling.access.Cullable");
            isCulledMethod = cullableClass.getMethod("isCulled");
            isForcedVisibleMethod = cullableClass.getMethod("isForcedVisible");
            usable = true;
            ModernMayhemMod.LOGGER.info("[ThermalRenderer] EntityCulling detected - reusing its occlusion flag to cull thermal masks.");
        } catch (ClassNotFoundException notInstalled) {
            usable = false;
        } catch (Throwable t) {
            usable = false;
            ModernMayhemMod.LOGGER.warn("[ThermalRenderer] EntityCulling is present but its Cullable interface could not be resolved; thermal occlusion-cull compat disabled.", t);
        }
    }

    public static boolean isOccluded(Entity entity) {
        if (entity == null) return false;
        resolve();
        if (!usable) return false;
        try {
            if (!cullableClass.isInstance(entity)) return false;
            if ((boolean) isForcedVisibleMethod.invoke(entity)) return false;
            return (boolean) isCulledMethod.invoke(entity);
        } catch (Throwable t) {
            return false;
        }
    }
}