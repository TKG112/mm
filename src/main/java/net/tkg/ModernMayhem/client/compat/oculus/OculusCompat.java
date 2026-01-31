package net.tkg.ModernMayhem.client.compat.oculus;

import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.fml.ModList;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.function.Function;
import java.util.function.Supplier;

public final class OculusCompat {
    private static final String OCULUS_MOD_ID = "oculus";
    private static final DefaultArtifactVersion VERSION_1_7_0 = new DefaultArtifactVersion("1.7.0");

    private static Function<MultiBufferSource.BufferSource, Boolean> END_BATCH_FUNCTION;
    private static Supplier<Boolean> IS_RENDER_SHADOW_SUPPLIER;

    public static void initCompat() {
        ModList.get().getModContainerById(OCULUS_MOD_ID).ifPresent(mod -> {
            END_BATCH_FUNCTION = OculusCompatImpl::endBatch;
            IS_RENDER_SHADOW_SUPPLIER = OculusCompatImpl::isRenderShadow;
        });
    }

    public static boolean isRenderShadow() {
        if (ModList.get().isLoaded(OCULUS_MOD_ID)) {
            return IS_RENDER_SHADOW_SUPPLIER.get();
        }
        return false;
    }

    public static boolean isShaderPackInUse() {
        if (ModList.get().isLoaded(OCULUS_MOD_ID)) {
            return IrisApi.getInstance().isShaderPackInUse();
        }
        return false;
    }

    public static boolean endBatch(MultiBufferSource.BufferSource bufferSource) {
        if (ModList.get().isLoaded(OCULUS_MOD_ID)) {
            return END_BATCH_FUNCTION.apply(bufferSource);
        }
        return false;
    }

    public static boolean isTranslucentHandPass = false;

    public static boolean shouldRenderVisor() {
        if (!isShaderPackInUse()) return true;

        return isTranslucentHandPass;
    }

    public static boolean isSolidHandPass = false;

    public static boolean shouldRenderNVG() {
        if (!isShaderPackInUse()) return true;

        return isSolidHandPass;
    }
}