package net.tkg.ModernMayhem.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom render types that properly handle transparency with shader packs
 */
public class ShaderCompatibleRenderTypes extends RenderType {

    // Dummy constructor - never called
    private ShaderCompatibleRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode,
                                        int bufferSize, boolean affectsCrumbling, boolean sortOnUpload,
                                        Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    /**
     * Render type for visor that doesn't write depth on transparent pixels
     * This allows shader water/fog to render through transparent areas
     */
    public static RenderType visorTranslucent(ResourceLocation texture) {
        CompositeState state = CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                .setTextureState(new TextureStateShard(texture, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setWriteMaskState(COLOR_WRITE) // Only write color, not depth
                .setDepthTestState(LEQUAL_DEPTH_TEST) // Respect existing depth
                .createCompositeState(true);

        return create("visor_translucent",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true,
                true,
                state);
    }

    /**
     * Alternative: Render type that writes depth only for opaque pixels (alpha > 0.5)
     * Uses alpha testing to conditionally write depth
     */
    public static RenderType visorAlphaTested(ResourceLocation texture) {
        CompositeState state = CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER)
                .setTextureState(new TextureStateShard(texture, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setDepthTestState(LEQUAL_DEPTH_TEST)
                .createCompositeState(true);

        return create("visor_alpha_tested",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true,
                false,
                state);
    }

    /**
     * Custom depth test that uses LEQUAL instead of ALWAYS
     */
    private static final RenderStateShard.DepthTestStateShard LEQUAL_DEPTH_TEST =
            new RenderStateShard.DepthTestStateShard("lequal", 515); // GL_LEQUAL = 515

    /**
     * Only write to color buffer, not depth
     */
    private static final RenderStateShard.WriteMaskStateShard COLOR_WRITE =
            new RenderStateShard.WriteMaskStateShard(true, false);
}