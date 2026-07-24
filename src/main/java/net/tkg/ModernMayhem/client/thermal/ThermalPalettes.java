package net.tkg.ModernMayhem.client.thermal;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.content.DataDrivenContent;
import net.tkg.ModernMayhem.content.def.PaletteDefinition;
import net.tkg.ModernMayhem.content.item.DataGogglesItem;
import net.tkg.ModernMayhem.client.utils.DynamicPostChain;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The thermal palette registry and the lookup texture the shader samples it from.
 * <p>
 * Palettes were previously hardcoded twice over -- the heat gradient as an {@code if/else} chain in
 * {@code thermal_composite.fsh}, the world tint as a parallel array here -- so adding one meant
 * editing GLSL. They are now {@link PaletteDefinition} data baked into a {@code 256 x paletteCount}
 * LUT; the shader just samples {@code (heat, paletteRow)} and knows nothing about individual palettes.
 * <p>
 * The registry is ModernMayhem's built-ins followed by every palette declared by a data-driven thermal
 * goggle. Each goggle owns a contiguous <i>range</i> of rows, so cycling stays within the palettes its
 * own definition declared.
 * <p>
 * <b>Evaluation must match the original shader.</b> It was never a plain gradient: it chained
 * {@code mix(colour, next, smoothstep(from, to, heat))}, so each stop blends the <i>accumulated</i>
 * colour with easing -- while the two greyscale palettes were straight linear ramps.
 */
@OnlyIn(Dist.CLIENT)
public final class ThermalPalettes {
    private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();

    /** Effect that samples the palette LUT. */
    private static final String COMPOSITE_PASS = "mm:thermal_composite";

    /** Horizontal resolution of the ramp; one texel per 1/255 of heat. */
    public static final int LUT_WIDTH = 256;

    private static PaletteDefinition.Stop stop(float at, float r, float g, float b) {
        return new PaletteDefinition.Stop(at, r, g, b);
    }

    /**
     * Ported one-for-one from the original {@code thermalPalette()} plus {@code WORLD_TINTS}.
     * Order is load-bearing: it is the palette index stored in existing goggles' NBT.
     */
    public static final List<PaletteDefinition> BUILTIN = List.of(
            new PaletteDefinition("White Hot", new float[]{1.00f, 1.00f, 1.00f}, false,
                    new float[]{0f, 0f, 0f}, List.of(stop(1.00f, 1f, 1f, 1f)), true),
            new PaletteDefinition("Black Hot", new float[]{1.00f, 1.00f, 1.00f}, true,
                    new float[]{1f, 1f, 1f}, List.of(stop(1.00f, 0f, 0f, 0f)), true),
            new PaletteDefinition("Ironbow", new float[]{0.50f, 0.42f, 0.78f}, false,
                    new float[]{0.10f, 0.00f, 0.22f}, List.of(
                    stop(0.22f, 0.55f, 0.00f, 0.45f),
                    stop(0.45f, 0.90f, 0.10f, 0.12f),
                    stop(0.68f, 1.00f, 0.50f, 0.00f),
                    stop(0.90f, 1.00f, 0.90f, 0.20f),
                    stop(1.00f, 1.00f, 1.00f, 1.00f)), false),
            new PaletteDefinition("Red Hot", new float[]{0.62f, 0.64f, 0.72f}, false,
                    new float[]{0.05f, 0.05f, 0.05f}, List.of(
                    stop(0.55f, 1.00f, 1.00f, 1.00f),
                    stop(1.00f, 1.00f, 0.05f, 0.00f)), false),
            new PaletteDefinition("Amber Hot", new float[]{0.70f, 0.48f, 0.22f}, false,
                    new float[]{0.14f, 0.06f, 0.00f}, List.of(
                    stop(0.40f, 0.55f, 0.22f, 0.00f),
                    stop(0.72f, 1.00f, 0.55f, 0.00f),
                    stop(1.00f, 1.00f, 0.95f, 0.35f)), false),
            new PaletteDefinition("Predator", new float[]{0.30f, 0.45f, 1.00f}, false,
                    new float[]{0.00f, 0.00f, 0.55f}, List.of(
                    stop(0.25f, 0.00f, 0.45f, 0.70f),
                    stop(0.48f, 0.00f, 0.80f, 0.10f),
                    stop(0.68f, 0.95f, 0.90f, 0.00f),
                    stop(0.88f, 0.95f, 0.10f, 0.00f),
                    stop(1.00f, 1.00f, 1.00f, 1.00f)), false),
            new PaletteDefinition("Green Hot", new float[]{0.35f, 0.75f, 0.40f}, false,
                    new float[]{0.00f, 0.02f, 0.00f}, List.of(
                    stop(0.50f, 0.00f, 0.50f, 0.00f),
                    stop(0.80f, 0.10f, 1.00f, 0.10f),
                    stop(1.00f, 0.75f, 1.00f, 0.75f)), false),
            new PaletteDefinition("Arctic", new float[]{0.40f, 0.62f, 1.00f}, false,
                    new float[]{0.00f, 0.04f, 0.25f}, List.of(
                    stop(0.40f, 0.00f, 0.40f, 0.80f),
                    stop(0.70f, 0.10f, 0.90f, 1.00f),
                    stop(1.00f, 1.00f, 1.00f, 1.00f)), false)
    );

    /** Built-ins followed by every pack-declared palette. */
    private static final List<PaletteDefinition> REGISTRY = new ArrayList<>();
    /** Item -> {first row, row count} for goggles that declare their own palettes. */
    private static final Map<Item, int[]> RANGES = new HashMap<>();

    private static DynamicTexture lut;
    private static boolean built;

    private ThermalPalettes() {}

    /** Collects built-ins plus every data-driven thermal goggle's palettes, then bakes the LUT. */
    private static void build() {
        if (BUILTIN.size() != PaletteDefinition.BUILTIN_COUNT) {
            // Palette cycling wraps on the server using that constant, so a mismatch would make the
            // last palettes unreachable -- exactly the bug a stale count caused before.
            LOGGER.error("[MM] PaletteDefinition.BUILTIN_COUNT is {} but there are {} built-in palettes"
                    + " -- cycling will not reach them all", PaletteDefinition.BUILTIN_COUNT, BUILTIN.size());
        }
        REGISTRY.clear();
        RANGES.clear();
        REGISTRY.addAll(BUILTIN);

        for (Item item : DataDrivenContent.registeredItems()) {
            if (!(item instanceof DataGogglesItem goggles)) continue;
            List<PaletteDefinition> own = goggles.definition().palettes();
            if (own.isEmpty()) continue;

            RANGES.put(item, new int[]{REGISTRY.size(), own.size()});
            REGISTRY.addAll(own);
        }

        built = true;
        bake();
    }

    private static void ensureBuilt() {
        if (!built) build();
    }

    public static int paletteCount() {
        ensureBuilt();
        return REGISTRY.size();
    }

    public static PaletteDefinition get(int index) {
        ensureBuilt();
        if (REGISTRY.isEmpty()) return BUILTIN.get(0);
        int i = ((index % REGISTRY.size()) + REGISTRY.size()) % REGISTRY.size();
        return REGISTRY.get(i);
    }

    /**
     * The palette rows the given goggle may cycle through: its own if it declared any, otherwise
     * ModernMayhem's built-ins.
     */
    public static int[] rangeFor(ItemStack goggles) {
        ensureBuilt();
        int[] range = goggles == null ? null : RANGES.get(goggles.getItem());
        return range != null ? range : new int[]{0, BUILTIN.size()};
    }

    /** GL id of the LUT, baking on first use. Suitable for {@code PostPass.addAuxAsset}. */
    public static int lutTextureId() {
        ensureBuilt();
        return lut.getId();
    }

    /**
     * Attaches the palette LUT to a chain's thermal composite pass, if it has one.
     * <p>
     * Both the night-vision and thermal chains include {@code mm:thermal_composite} (night vision
     * composites thermal for the AR overlay), so both need this or they would sample an unbound
     * texture. A chain without the pass is fine -- it simply doesn't use palettes.
     */
    public static void bindTo(DynamicPostChain chain) {
        for (PostPass pass : chain.getPasses()) {
            if (COMPOSITE_PASS.equals(pass.getEffect().getName())) {
                pass.addAuxAsset("PaletteSampler", ThermalPalettes::lutTextureId, LUT_WIDTH, paletteCount());
                return;
            }
        }
    }

    private static void bake() {
        int height = Math.max(1, REGISTRY.size());
        NativeImage image = new NativeImage(NativeImage.Format.RGBA, LUT_WIDTH, height, false);

        for (int y = 0; y < REGISTRY.size(); y++) {
            PaletteDefinition palette = REGISTRY.get(y);
            for (int x = 0; x < LUT_WIDTH; x++) {
                float heat = x / (float) (LUT_WIDTH - 1);
                float[] c = evaluate(palette, heat);
                image.setPixelRGBA(x, y, packAbgr(c[0], c[1], c[2]));
            }
        }

        if (lut != null) {
            lut.close();
        }
        lut = new DynamicTexture(image);
        lut.upload();

        // NEAREST + CLAMP_TO_EDGE matters here: filtering across V would blend two palettes together,
        // and wrapping in U would let heat 0 pick up the colour from heat 1.
        RenderSystem.bindTexture(lut.getId());
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
    }

    /** NativeImage stores pixels little-endian, i.e. 0xAABBGGRR. */
    private static int packAbgr(float r, float g, float b) {
        return (0xFF << 24) | (toByte(b) << 16) | (toByte(g) << 8) | toByte(r);
    }

    private static int toByte(float v) {
        return Math.max(0, Math.min(255, Math.round(v * 255f)));
    }

    /**
     * Colour of {@code palette} at {@code heat}, reproducing the original shader's maths: start at the
     * base colour, then blend toward each stop across its range -- eased with smoothstep, or linearly
     * for the greyscale palettes.
     */
    public static float[] evaluate(PaletteDefinition palette, float heat) {
        heat = Math.max(0f, Math.min(1f, heat));
        float[] c = palette.base().clone();

        float from = 0f;
        for (PaletteDefinition.Stop stop : palette.stops()) {
            float t = palette.linear()
                    ? clampedRange(from, stop.at(), heat)
                    : smoothstep(from, stop.at(), heat);
            c[0] += (stop.r() - c[0]) * t;
            c[1] += (stop.g() - c[1]) * t;
            c[2] += (stop.b() - c[2]) * t;
            from = stop.at();
        }
        return c;
    }

    private static float clampedRange(float edge0, float edge1, float x) {
        if (edge1 <= edge0) return x >= edge1 ? 1f : 0f;
        return Math.max(0f, Math.min(1f, (x - edge0) / (edge1 - edge0)));
    }

    /** GLSL smoothstep. */
    private static float smoothstep(float edge0, float edge1, float x) {
        float t = clampedRange(edge0, edge1, x);
        return t * t * (3f - 2f * t);
    }
}
