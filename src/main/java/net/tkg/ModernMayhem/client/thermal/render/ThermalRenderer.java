package net.tkg.ModernMayhem.client.thermal.render;

import net.tkg.ModernMayhem.client.thermal.ThermalPalettes;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.client.shaderController.TVGShaderController;
import net.tkg.ModernMayhem.client.shaderController.NVGShaderController;
import net.tkg.ModernMayhem.client.config.ClientConfig;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.tkg.ModernMayhem.client.compat.ar.ARCompat;
import net.tkg.ModernMayhem.client.compat.entityculling.EntityCullingCompat;
import net.tkg.ModernMayhem.client.compat.oculus.OculusCompat;
import net.tkg.ModernMayhem.server.mixin.client.GameRendererInvoker;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;

public class ThermalRenderer {

    private static final Minecraft MC = Minecraft.getInstance();
    private static final boolean OCULUS_LOADED = ModList.get().isLoaded("oculus");
    private static final boolean AR_LOADED = ModList.get().isLoaded("acceleratedrendering");

    private static final boolean USE_ENTITYCULLING_CULL = true;

    private static TextureTarget maskTarget;
    private static TextureTarget blurHTarget;
    private static TextureTarget blurVTarget;
    private static TextureTarget occludedMaskTarget;
    private static TextureTarget coverageTarget;
    private static TextureTarget armTarget;

    private static ThermalShader blurShader;
    private static ThermalShader applyShader;
    private static ThermalShader depthWriteShader;
    private static ThermalShader depthOccludeShader;
    private static ThermalShader carveShader;

    private static int quadVAO = -1;
    private static int quadVBO = -1;

    private static BufferBuilder maskBuffer = null;
    private static MultiBufferSource.BufferSource maskBufferSource = null;

    private static int lastEntityCount = 0;
    private static int framesSinceLastCheck = 0;
    private static final int CHECK_INTERVAL = 5;

    private static boolean renderThisFrame = false;

    private static boolean compositePending = false;

    private static float maskNear = 0.05f;
    private static float maskFar = 1000.0f;

    private static float viewmodelNear = 0.05f, viewmodelFar = 1000.0f;

    public enum RenderMode { OFF, OUTLINE, OVERLAY }

    private static RenderMode renderMode = RenderMode.OUTLINE;
    private static Predicate<Entity> outlinePredicate = entity -> entity instanceof LivingEntity;
    private static Function<Entity, Integer> colorProvider = null;

    private static float outlineR = 1.0f, outlineG = 1.0f, outlineB = 1.0f, outlineA = 1.0f;
    private static boolean useColoredOutline = true;
    private static boolean useBlackOutline = true;

    public enum ThermalPalette { WHITE_HOT, BLACK_HOT, IRONBOW, RED_HOT, AMBER_HOT, PREDATOR, GREEN_HOT, ARCTIC }

    private static int thermalPalette = ThermalPalette.WHITE_HOT.ordinal();
    private static float detailStrength = 0.5f;

    public static void setRenderMode(RenderMode mode) { renderMode = mode; }
    public static RenderMode getRenderMode() { return renderMode; }
    public static void setOutlineColorProvider(Function<Entity, Integer> provider) { colorProvider = provider; }
    public static void setOutlinePredicate(Predicate<Entity> predicate) { outlinePredicate = predicate; }
    public static void setOutlineColor(float r, float g, float b, float a) { outlineR = r; outlineG = g; outlineB = b; outlineA = a; }
    public static void setUseColoredOutline(boolean use) { useColoredOutline = use; }
    public static boolean isUsingColoredOutline() { return useColoredOutline; }
    public static void setUseBlackOutline(boolean use) { useBlackOutline = use; }
    public static boolean isUsingBlackOutline() { return useBlackOutline; }

    public static RenderTarget getBlurTarget() { return blurVTarget; }
    public static RenderTarget getOccludedMaskTarget() { return occludedMaskTarget; }
    public static boolean isUseSourceColor() { return colorProvider != null; }
    public static float getOutlineR() { return outlineR; }
    public static float getOutlineG() { return outlineG; }
    public static float getOutlineB() { return outlineB; }
    public static float getOutlineA() { return outlineA; }

    public static void setThermalPalette(ThermalPalette palette) { thermalPalette = palette.ordinal(); }
    public static void setThermalPalette(int paletteId) { thermalPalette = paletteId; }
    public static int getThermalPalette() { return thermalPalette; }
    /**
     * Steps to the next palette <i>within the equipped goggle's own range</i>, so a pack-defined
     * thermal cycles only the palettes it declared rather than wandering into ModernMayhem's.
     */
    public static void cycleThermalPalette() {
        int[] range = currentPaletteRange();
        int offset = range[0];
        int count = Math.max(1, range[1]);
        int relative = ((thermalPalette - offset) % count + count) % count;
        thermalPalette = offset + ((relative + 1) % count);
    }

    /** {first row, row count} of the palettes the equipped thermal goggle may cycle. */
    private static int[] currentPaletteRange() {
        Minecraft mc = Minecraft.getInstance();
        ItemStack face = mc.player == null ? ItemStack.EMPTY : CuriosUtil.getFaceWearItem(mc.player);
        return ThermalPalettes.rangeFor(face);
    }
    /** As {@link #cycleThermalPalette()}, in reverse. */
    public static void cycleThermalPaletteBack() {
        int[] range = currentPaletteRange();
        int offset = range[0];
        int count = Math.max(1, range[1]);
        int relative = ((thermalPalette - offset) % count + count) % count;
        thermalPalette = offset + ((relative - 1 + count) % count);
    }
    public static void setDetailStrength(float strength) { detailStrength = Math.max(0.0f, Math.min(1.0f, strength)); }
    public static float getDetailStrength() { return detailStrength; }

    // World tint and inversion come from the palette itself (see PaletteDefinition). They used to be a
    // parallel array here, which meant a palette was described in two places -- shader and Java.
    public static float getWorldTintR() { return ThermalPalettes.get(thermalPalette).worldColor()[0]; }
    public static float getWorldTintG() { return ThermalPalettes.get(thermalPalette).worldColor()[1]; }
    public static float getWorldTintB() { return ThermalPalettes.get(thermalPalette).worldColor()[2]; }
    public static boolean isWorldInverted() { return ThermalPalettes.get(thermalPalette).inverted(); }

    public static void init() {
        Minecraft mc = Minecraft.getInstance();
        int width  = mc.getWindow().getWidth();
        int height = mc.getWindow().getHeight();

        maskTarget  = new TextureTarget(width, height, true, Minecraft.ON_OSX);
        blurHTarget = new TextureTarget(width, height, false, Minecraft.ON_OSX);
        blurVTarget = new TextureTarget(width, height, false, Minecraft.ON_OSX);
        occludedMaskTarget = new TextureTarget(width, height, true, Minecraft.ON_OSX);   // depth: vanilla sorts the two gun hands in here
        coverageTarget = new TextureTarget(width, height, true, Minecraft.ON_OSX);   // keeps depth for depth-aware carve
        armTarget = new TextureTarget(width, height, true, Minecraft.ON_OSX);        // arm heat + arm depth (hand projection)
        maskTarget.setClearColor(0, 0, 0, 0);
        blurHTarget.setClearColor(0, 0, 0, 0);
        blurVTarget.setClearColor(0, 0, 0, 0);
        occludedMaskTarget.setClearColor(0, 0, 0, 0);
        coverageTarget.setClearColor(0, 0, 0, 0);
        armTarget.setClearColor(0, 0, 0, 0);

        // The viewmodel coverage replay runs TaCZ's real scope path, which stencil-masks the lens (so the gun
        // body/optic body leave a see-through hole that grows with aim progress). That needs a stencil buffer on
        // the coverage target. occludedMaskTarget must match its depth format because flushArmsSortedBlend blits
        // depth coverageTarget -> occludedMaskTarget, and a depth blit requires matching depth formats.
        occludedMaskTarget.enableStencil();
        coverageTarget.enableStencil();

        try {
            blurShader  = new ThermalShader("thermal_blur", "thermal_sobel");
            applyShader = new ThermalShader("thermal_apply", "thermal_sobel");
            depthWriteShader = new ThermalShader("thermal_depth", "thermal_sobel");
            depthOccludeShader = new ThermalShader("thermal_occlude", "thermal_sobel");
            carveShader = new ThermalShader("thermal_carve", "thermal_sobel");
        } catch (IOException e) {
            ModernMayhemMod.LOGGER.error("[ThermalRenderer] Failed to load shaders", e);
        }

        createQuadVAO();
        maskBuffer       = new BufferBuilder(262144);
        maskBufferSource = MultiBufferSource.immediate(maskBuffer);
    }

    public static void cleanup() {
        if (maskTarget  != null) { maskTarget.destroyBuffers();  maskTarget  = null; }
        if (blurHTarget != null) { blurHTarget.destroyBuffers(); blurHTarget = null; }
        if (blurVTarget != null) { blurVTarget.destroyBuffers(); blurVTarget = null; }
        if (occludedMaskTarget != null) { occludedMaskTarget.destroyBuffers(); occludedMaskTarget = null; }
        if (coverageTarget != null) { coverageTarget.destroyBuffers(); coverageTarget = null; }
        if (armTarget != null) { armTarget.destroyBuffers(); armTarget = null; }
        if (blurShader  != null) { blurShader.close();  blurShader  = null; }
        if (applyShader != null) { applyShader.close(); applyShader = null; }
        if (depthWriteShader != null) { depthWriteShader.close(); depthWriteShader = null; }
        if (depthOccludeShader != null) { depthOccludeShader.close(); depthOccludeShader = null; }
        if (carveShader != null) { carveShader.close(); carveShader = null; }
        if (quadVAO != -1) {
            GL30.glDeleteVertexArrays(quadVAO);
            GL30.glDeleteBuffers(quadVBO);
            quadVAO = quadVBO = -1;
        }
    }

    public static void resize(int width, int height) {
        if (maskTarget  != null) maskTarget.resize(width, height, Minecraft.ON_OSX);
        if (blurHTarget != null) blurHTarget.resize(width, height, Minecraft.ON_OSX);
        if (blurVTarget != null) blurVTarget.resize(width, height, Minecraft.ON_OSX);
        if (occludedMaskTarget != null) occludedMaskTarget.resize(width, height, Minecraft.ON_OSX);
        if (coverageTarget != null) coverageTarget.resize(width, height, Minecraft.ON_OSX);
        if (armTarget != null) armTarget.resize(width, height, Minecraft.ON_OSX);
    }

    private static void createQuadVAO() {
        quadVAO = GL30.glGenVertexArrays();
        quadVBO = GL30.glGenBuffers();
        float[] verts = {
                -1f,  1f,  0f, 1f,
                -1f, -1f,  0f, 0f,
                1f, -1f,  1f, 0f,
                -1f,  1f,  0f, 1f,
                1f, -1f,  1f, 0f,
                1f,  1f,  1f, 1f
        };
        GL30.glBindVertexArray(quadVAO);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, quadVBO);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, verts, GL30.GL_STATIC_DRAW);
        GL30.glEnableVertexAttribArray(0);
        GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        GL30.glBindVertexArray(0);
    }

    private static void drawFullscreenQuad() {
        GL30.glBindVertexArray(quadVAO);
        GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 6);
        GL30.glBindVertexArray(0);
    }

    private static void runBlur(RenderTarget source) {
        int w = source.width, h = source.height;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableBlend();
        blurShader.use();

        blurHTarget.bindWrite(false);
        GlStateManager._viewport(0, 0, blurHTarget.width, blurHTarget.height);
        RenderSystem.clearColor(0, 0, 0, 0);
        RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);
        blurShader.setTexture("DiffuseSampler", source.getColorTextureId());
        blurShader.setUniform("InSize",  (float)w, (float)h);
        blurShader.setUniform("OutSize", (float)w, (float)h);
        blurShader.setUniform("BlurDir", 1f, 0f);
        blurShader.setUniform("Radius", 3f);
        drawFullscreenQuad();

        blurVTarget.bindWrite(false);
        GlStateManager._viewport(0, 0, blurVTarget.width, blurVTarget.height);
        RenderSystem.clearColor(0, 0, 0, 0);
        RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);
        blurShader.setTexture("DiffuseSampler", blurHTarget.getColorTextureId());
        blurShader.setUniform("BlurDir", 0f, 1f);
        drawFullscreenQuad();

        GL20.glUseProgram(0);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
    }

    private static void compositeToMain(RenderTarget blurSrc, RenderTarget maskSrc) {
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, MC.getMainRenderTarget().frameBufferId);
        GlStateManager._viewport(0, 0, MC.getMainRenderTarget().width, MC.getMainRenderTarget().height);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,       GlStateManager.DestFactor.ZERO);

        RenderSystem.colorMask(true, true, true, false);

        applyShader.use();
        applyShader.setTexture("BlurSampler", blurSrc.getColorTextureId());
        applyShader.setTexture("MaskSampler", maskSrc.getColorTextureId());
        applyShader.setUniform("RenderMode",     renderMode == RenderMode.OVERLAY ? 2f : 1f);
        applyShader.setUniform("UseSourceColor", colorProvider != null ? 1f : 0f);
        applyShader.setUniform("OutlineColor",   outlineR, outlineG, outlineB, outlineA);
        drawFullscreenQuad();

        RenderSystem.colorMask(true, true, true, true);
        GL20.glUseProgram(0);
        for (int i = 0; i < 4; i++) { RenderSystem.activeTexture(GL13.GL_TEXTURE0 + i); GlStateManager._bindTexture(0); }
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        BufferUploader.reset();
        MC.getMainRenderTarget().bindWrite(false);
    }

    private static void occludeWithSceneDepth() {
        // Entities were captured under the world projection, so linearise with the world near/far.
        occludePass(maskTarget, occludedMaskTarget, maskNear, maskFar, false);
    }

    public static void reoccludeMaskAgainstCurrentDepth() {
        boolean proceed = renderMode != RenderMode.OFF && compositePending && (TVGShaderController.isEnabled() || NVGShaderController.isEnabled()) && occludedMaskTarget != null && maskTarget != null;
        if (!proceed) { pendingArms.clear(); pendingViewmodelOccluders.clear(); return; }

        boolean armStaged = !pendingArms.isEmpty();
        boolean viewmodelStaged = !pendingViewmodelOccluders.isEmpty();
        boolean shaderpack = isIrisShaderpackActive();
        int savedFbo = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        try {
            if (viewmodelStaged) renderViewmodelCoverage();

            if (shaderpack) {
                if (viewmodelStaged) carveViewmodelFromOccluded();
                if (armStaged) {
                    if (viewmodelStaged) flushArmsSortedBlend(occludedMaskTarget, coverageTarget);
                    else flushPendingArmsToMask(occludedMaskTarget, false);
                }
            } else {
                occludeWithSceneDepth();
                if (viewmodelStaged) carveViewmodelFromOccluded();
                if (armStaged) {
                    if (viewmodelStaged)
                        flushArmsSortedBlend(occludedMaskTarget, coverageTarget);
                    else
                        flushPendingArmsToMask(occludedMaskTarget, false);
                }
            }

            runBlur(occludedMaskTarget);
        } catch (Exception e) {
        } finally {
            pendingArms.clear();
            pendingViewmodelOccluders.clear();
            GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, savedFbo);
        }
    }

    private static boolean handHeatReadingEnabled() {
        try {
            return Boolean.TRUE.equals(ClientConfig.THERMAL_HAND_HEAT_READING.get());
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean isMaskActiveForArm() {
        if (!handHeatReadingEnabled()) return false;
        if (renderMode == RenderMode.OFF) return false;
        if (!(TVGShaderController.isEnabled() || NVGShaderController.isEnabled())) return false;
        if (maskTarget == null || maskBufferSource == null) return false;
        return MC.options.getCameraType().isFirstPerson();
    }

    private static final class ArmStage {
        final HumanoidArm side;
        final AbstractClientPlayer player;
        final PoseStack pose = new PoseStack();
        final Matrix4f proj = new Matrix4f();
        final Matrix4f modelView = new Matrix4f();
        ArmStage(AbstractClientPlayer p, HumanoidArm s, PoseStack src) {
            player = p; side = s;
            pose.last().pose().set(src.last().pose());
            pose.last().normal().set(src.last().normal());
            proj.set(RenderSystem.getProjectionMatrix());
            modelView.set(RenderSystem.getModelViewMatrix());
        }
    }
    private static final java.util.List<ArmStage> pendingArms = new java.util.ArrayList<>(2);
    private static float armNear = 0.05f, armFar = 1000.0f;

    private static final class ViewmodelOccluder {
        final Runnable reRender;
        final Matrix4f proj = new Matrix4f();
        final Matrix4f modelView = new Matrix4f();
        ViewmodelOccluder(Runnable r) {
            reRender = r;
            proj.set(RenderSystem.getProjectionMatrix());
            modelView.set(RenderSystem.getModelViewMatrix());
        }
    }

    private static final java.util.List<ViewmodelOccluder> pendingViewmodelOccluders = new java.util.ArrayList<>(2);
    private static boolean reRenderingViewmodel = false;

    public static MultiBufferSource.BufferSource getMaskBufferSource() { return maskBufferSource; }

    // True only while the offscreen viewmodel-coverage (cold-occluder) replay is running. The TaCZ attachment
    // mixin uses this to render optics as plain solid geometry instead of their first-person stencil/lens
    // routine, which can't be captured into the stencil-less coverage target.
    public static boolean isReRenderingViewmodel() { return reRenderingViewmodel; }

    public static void stageViewmodelOccluder(Runnable reRender) {
        try {
            if (reRender == null) return;
            if (reRenderingViewmodel) return;
            if (!isMaskActiveForArm()) return;
            if (pendingViewmodelOccluders.size() >= 4) return;
            pendingViewmodelOccluders.add(new ViewmodelOccluder(reRender));
        } catch (Throwable ignored) { }
    }

    private static void renderViewmodelCoverage() {
        if (pendingViewmodelOccluders.isEmpty() || coverageTarget == null || maskBufferSource == null) return;

        int savedFbo = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        int[] savedViewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, savedViewport);
        Matrix4f savedProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        boolean arDisabled = false;
        float savedClearDepth = GL11.glGetFloat(GL11.GL_DEPTH_CLEAR_VALUE);
        boolean reversedZ = savedClearDepth < 0.1f;
        try {
            coverageTarget.bindWrite(false);
            GlStateManager._viewport(0, 0, coverageTarget.width, coverageTarget.height);
            GL11.glDepthRange(reversedZ ? 1.0 : 0.0, reversedZ ? 0.0 : 1.0);
            GlStateManager._clearDepth(1.0);   // far in the normalised mapping
            RenderSystem.clearColor(0, 0, 0, 0);
            RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT | GL30.GL_STENCIL_BUFFER_BIT, false);
            if (AR_LOADED) { try { ARCompat.disableAcceleration(); arDisabled = true; } catch (Throwable t) { } }
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();

            PoseStack mv = RenderSystem.getModelViewStack();
            reRenderingViewmodel = true;
            for (ViewmodelOccluder o : pendingViewmodelOccluders) {
                RenderSystem.setProjectionMatrix(o.proj, VertexSorting.DISTANCE_TO_ORIGIN);
                viewmodelNear = o.proj.perspectiveNear();
                viewmodelFar  = o.proj.perspectiveFar();
                mv.pushPose();
                mv.setIdentity();
                mv.mulPoseMatrix(o.modelView);
                RenderSystem.applyModelViewMatrix();
                try {
                    o.reRender.run();
                    if (!OculusCompat.endBatch(maskBufferSource)) maskBufferSource.endBatch();
                    MultiBufferSource.BufferSource main = MC.renderBuffers().bufferSource();
                    if (!OculusCompat.endBatch(main)) main.endBatch();
                } finally {
                    mv.popPose();
                    RenderSystem.applyModelViewMatrix();
                }
            }
        } catch (Throwable t) {
        } finally {
            reRenderingViewmodel = false;
            if (arDisabled && AR_LOADED) { try { ARCompat.resetAcceleration(); } catch (Throwable t) { } }
            try {
                GL11.glDepthRange(0.0, 1.0);
                GlStateManager._clearDepth(savedClearDepth);
                RenderSystem.depthMask(false);
                RenderSystem.disableDepthTest();
                RenderSystem.disableBlend();
                RenderSystem.setProjectionMatrix(savedProj, VertexSorting.DISTANCE_TO_ORIGIN);
                GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, savedFbo);
                GlStateManager._viewport(savedViewport[0], savedViewport[1], savedViewport[2], savedViewport[3]);
                BufferUploader.reset();
            } catch (Throwable t) {

            }
        }
    }

    private static void carveViewmodelFromOccluded() {
        if (carveShader == null || coverageTarget == null || occludedMaskTarget == null || maskTarget == null) return;
        int savedFbo = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        boolean reversedZ = GL11.glGetFloat(GL11.GL_DEPTH_CLEAR_VALUE) < 0.1f;
        int itemDepthTex   = coverageTarget.getDepthTextureId();
        int entityDepthTex = maskTarget.getDepthTextureId();
        try {
            occludedMaskTarget.bindWrite(false);
            GlStateManager._viewport(0, 0, occludedMaskTarget.width, occludedMaskTarget.height);
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);

            GlStateManager._bindTexture(itemDepthTex);
            int itemCmp = GL30.glGetTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, GL30.GL_NONE);
            GlStateManager._bindTexture(entityDepthTex);
            int entCmp = GL30.glGetTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, GL30.GL_NONE);
            GlStateManager._bindTexture(0);

            carveShader.use();
            carveShader.setTexture("CoverageSampler",    coverageTarget.getColorTextureId());
            carveShader.setTexture("ItemDepthSampler",   itemDepthTex);
            carveShader.setTexture("EntityDepthSampler", entityDepthTex);
            carveShader.setUniform("ItemNearFar",   viewmodelNear, viewmodelFar);
            carveShader.setUniform("EntityNearFar", maskNear, maskFar);
            carveShader.setUniform("IsReversedZItem",   0.0f);
            carveShader.setUniform("IsReversedZEntity", reversedZ ? 1.0f : 0.0f);
            carveShader.setUniform("DepthAware",    0.0f);
            carveShader.setUniform("DepthBias",     0.02f);
            drawFullscreenQuad();
            GL20.glUseProgram(0);

            GlStateManager._bindTexture(entityDepthTex);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, entCmp);
            GlStateManager._bindTexture(itemDepthTex);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, itemCmp);
            for (int i = 0; i < 3; i++) { RenderSystem.activeTexture(GL13.GL_TEXTURE0 + i); GlStateManager._bindTexture(0); }
            RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        } catch (Throwable t) {

        } finally {
            try { GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, savedFbo); } catch (Throwable ignored) { }
        }
    }

    public static void stageArmForMask(AbstractClientPlayer player, HumanoidArm arm, PoseStack poseStack) {
        try {
            if (player == null || poseStack == null) return;
            if (!isMaskActiveForArm()) return;
            if (pendingArms.size() >= 4) return;
            pendingArms.add(new ArmStage(player, arm, poseStack));
        } catch (Throwable ignored) { }
    }

    private static void flushPendingArmsToMask(RenderTarget target, boolean pinDepth) {
        if (pendingArms.isEmpty() || target == null || maskBufferSource == null) return;
        EntityRenderDispatcher dispatcher = MC.getEntityRenderDispatcher();

        int savedFbo = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        int[] savedViewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, savedViewport);
        Matrix4f savedProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        boolean arDisabled = false;
        try {
            target.bindWrite(false);
            GlStateManager._viewport(0, 0, target.width, target.height);
            if (AR_LOADED) { try { ARCompat.disableAcceleration(); arDisabled = true; } catch (Throwable t) { } }
            if (pinDepth) {
                RenderSystem.enableDepthTest();
                RenderSystem.depthFunc(GL11.GL_LEQUAL);
                RenderSystem.depthMask(true);
                GL11.glDepthRange(0.0, 0.0);   // front-pin: the occlude pass can never cull the arm
            } else {
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            }

            BufferUploader.reset();

            PoseStack mv = RenderSystem.getModelViewStack();
            for (ArmStage a : pendingArms) {
                if (a.player == null) continue;
                if (!(dispatcher.getRenderer(a.player) instanceof PlayerRenderer pr)) continue;
                armNear = a.proj.perspectiveNear();   // all first-person arms share one projection
                armFar  = a.proj.perspectiveFar();
                RenderSystem.setProjectionMatrix(a.proj, VertexSorting.DISTANCE_TO_ORIGIN);
                mv.pushPose();
                mv.setIdentity();
                mv.mulPoseMatrix(a.modelView);
                RenderSystem.applyModelViewMatrix();
                try {
                    if (a.side == HumanoidArm.LEFT)
                        pr.renderLeftHand(a.pose, maskBufferSource, 15728880, a.player);
                    else
                        pr.renderRightHand(a.pose, maskBufferSource, 15728880, a.player);
                    if (!OculusCompat.endBatch(maskBufferSource)) maskBufferSource.endBatch();
                } finally {
                    mv.popPose();
                    RenderSystem.applyModelViewMatrix();
                }
            }
        } catch (Throwable t) {

        } finally {
            if (arDisabled && AR_LOADED) { try { ARCompat.resetAcceleration(); } catch (Throwable t) { } }
            try {
                if (pinDepth) GL11.glDepthRange(0.0, 1.0);   // undo the front-pin
                else RenderSystem.disableBlend();
                RenderSystem.setProjectionMatrix(savedProj, VertexSorting.DISTANCE_TO_ORIGIN);
                GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, savedFbo);
                GlStateManager._viewport(savedViewport[0], savedViewport[1], savedViewport[2], savedViewport[3]);
                BufferUploader.reset();
            } catch (Throwable t) {

            }
        }
    }

    private static void flushArmsSortedBlend(RenderTarget target, RenderTarget gunDepthSrc) {
        if (pendingArms.isEmpty() || target == null || maskBufferSource == null) return;
        EntityRenderDispatcher dispatcher = MC.getEntityRenderDispatcher();

        int savedFbo = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        int[] savedViewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, savedViewport);
        Matrix4f savedProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        boolean arDisabled = false;
        boolean reversedZ = GL11.glGetFloat(GL11.GL_DEPTH_CLEAR_VALUE) < 0.1f;
        try {
            target.bindWrite(false);
            GlStateManager._viewport(0, 0, target.width, target.height);
            if (gunDepthSrc != null) {
                GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, gunDepthSrc.frameBufferId);
                GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, target.frameBufferId);
                GL30.glBlitFramebuffer(0, 0, gunDepthSrc.width, gunDepthSrc.height,
                        0, 0, target.width, target.height,
                        GL30.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
                target.bindWrite(false);
            } else {
                RenderSystem.clear(GL30.GL_DEPTH_BUFFER_BIT, false);
            }
            if (AR_LOADED) { try { ARCompat.disableAcceleration(); arDisabled = true; } catch (Throwable t) {

            }
            }
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GL11.glDepthRange(reversedZ ? 1.0 : 0.0, reversedZ ? 0.0 : 1.0);

            BufferUploader.reset();

            PoseStack mv = RenderSystem.getModelViewStack();
            for (ArmStage a : pendingArms) {
                if (a.player == null) continue;
                if (!(dispatcher.getRenderer(a.player) instanceof PlayerRenderer pr)) continue;
                RenderSystem.setProjectionMatrix(a.proj, VertexSorting.DISTANCE_TO_ORIGIN);
                mv.pushPose();
                mv.setIdentity();
                mv.mulPoseMatrix(a.modelView);
                RenderSystem.applyModelViewMatrix();
                try {
                    if (a.side == HumanoidArm.LEFT)
                        pr.renderLeftHand(a.pose, maskBufferSource, 15728880, a.player);
                    else
                        pr.renderRightHand(a.pose, maskBufferSource, 15728880, a.player);
                    if (!OculusCompat.endBatch(maskBufferSource)) maskBufferSource.endBatch();
                } finally {
                    mv.popPose();
                    RenderSystem.applyModelViewMatrix();
                }
            }
        } catch (Throwable t) {
        } finally {
            if (arDisabled && AR_LOADED) { try { ARCompat.resetAcceleration(); } catch (Throwable t) { } }
            try {
                GL11.glDepthRange(0.0, 1.0);   // restore default window mapping
                RenderSystem.depthMask(false);
                RenderSystem.disableDepthTest();
                RenderSystem.disableBlend();
                RenderSystem.setProjectionMatrix(savedProj, VertexSorting.DISTANCE_TO_ORIGIN);
                GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, savedFbo);
                GlStateManager._viewport(savedViewport[0], savedViewport[1], savedViewport[2], savedViewport[3]);
                BufferUploader.reset();
            } catch (Throwable t) {

            }
        }
    }

    private static void renderArmsToArmTarget() {
        if (pendingArms.isEmpty() || armTarget == null || maskBufferSource == null) return;
        EntityRenderDispatcher dispatcher = MC.getEntityRenderDispatcher();

        int savedFbo = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        int[] savedViewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, savedViewport);
        Matrix4f savedProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        boolean arDisabled = false;
        float savedClearDepth = GL11.glGetFloat(GL11.GL_DEPTH_CLEAR_VALUE);
        boolean reversedZ = savedClearDepth < 0.1f;
        try {
            armTarget.bindWrite(false);
            GlStateManager._viewport(0, 0, armTarget.width, armTarget.height);
            GL11.glDepthRange(reversedZ ? 1.0 : 0.0, reversedZ ? 0.0 : 1.0);
            GlStateManager._clearDepth(1.0);
            RenderSystem.clearColor(0, 0, 0, 0);
            RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT, false);
            if (AR_LOADED) { try { ARCompat.disableAcceleration(); arDisabled = true; } catch (Throwable t) {

            }
            }
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();

            PoseStack mv = RenderSystem.getModelViewStack();
            for (ArmStage a : pendingArms) {
                if (a.player == null) continue;
                if (!(dispatcher.getRenderer(a.player) instanceof PlayerRenderer pr)) continue;
                armNear = a.proj.perspectiveNear();
                armFar  = a.proj.perspectiveFar();
                RenderSystem.setProjectionMatrix(a.proj, VertexSorting.DISTANCE_TO_ORIGIN);
                mv.pushPose();
                mv.setIdentity();
                mv.mulPoseMatrix(a.modelView);
                RenderSystem.applyModelViewMatrix();
                try {
                    if (a.side == HumanoidArm.LEFT)
                        pr.renderLeftHand(a.pose, maskBufferSource, 15728880, a.player);
                    else
                        pr.renderRightHand(a.pose, maskBufferSource, 15728880, a.player);
                    if (!OculusCompat.endBatch(maskBufferSource)) maskBufferSource.endBatch();
                } finally {
                    mv.popPose();
                    RenderSystem.applyModelViewMatrix();
                }
            }
        } catch (Throwable t) {

        } finally {
            if (arDisabled && AR_LOADED) { try { ARCompat.resetAcceleration(); } catch (Throwable t) { } }
            try {
                GL11.glDepthRange(0.0, 1.0);
                GlStateManager._clearDepth(savedClearDepth);
                RenderSystem.depthMask(false);
                RenderSystem.disableDepthTest();
                RenderSystem.setProjectionMatrix(savedProj, VertexSorting.DISTANCE_TO_ORIGIN);
                GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, savedFbo);
                GlStateManager._viewport(savedViewport[0], savedViewport[1], savedViewport[2], savedViewport[3]);
                BufferUploader.reset();
            } catch (Throwable t) {

            }
        }
    }

    private static void occludePass(RenderTarget colorSource, RenderTarget out,
                                    float near, float far, boolean blendIntoOut) {
        occludePass(colorSource, out, near, far, blendIntoOut,
                MC.getMainRenderTarget().getDepthTextureId(), 0.25f);
    }

    private static void occludePass(RenderTarget colorSource, RenderTarget out,
                                    float near, float far, boolean blendIntoOut,
                                    int sceneDepthTex, float epsilon) {
        occludePass(colorSource, out, near, far, blendIntoOut, sceneDepthTex, epsilon,
                GL11.glGetFloat(GL11.GL_DEPTH_CLEAR_VALUE) < 0.1f);
    }

    private static void occludePass(RenderTarget colorSource, RenderTarget out,
                                    float near, float far, boolean blendIntoOut,
                                    int sceneDepthTex, float epsilon, boolean reversedZ) {
        if (depthOccludeShader == null || out == null || colorSource == null) return;


        out.bindWrite(false);
        GlStateManager._viewport(0, 0, out.width, out.height);
        if (blendIntoOut) {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        } else {
            RenderSystem.clearColor(0, 0, 0, 0);
            RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);
            RenderSystem.disableBlend();
        }
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        int maskDepthTex  = colorSource.getDepthTextureId();

        GlStateManager._bindTexture(maskDepthTex);
        int maskCompareSaved = GL30.glGetTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, GL30.GL_NONE);

        GlStateManager._bindTexture(sceneDepthTex);
        int sceneCompareSaved = GL30.glGetTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, GL30.GL_NONE);

        GlStateManager._bindTexture(0);

        depthOccludeShader.use();
        depthOccludeShader.setTexture("MaskSampler",        colorSource.getColorTextureId());
        depthOccludeShader.setTexture("EntityDepthSampler", maskDepthTex);
        depthOccludeShader.setTexture("SceneDepthSampler",  sceneDepthTex);
        depthOccludeShader.setUniform("InSize",      (float) out.width, (float) out.height);
        depthOccludeShader.setUniform("IsReversedZ", reversedZ ? 1.0f : 0.0f);
        depthOccludeShader.setUniform("NearFar",     near, far);
        depthOccludeShader.setUniform("WorldEpsilon", epsilon);
        drawFullscreenQuad();

        GL20.glUseProgram(0);

        GlStateManager._bindTexture(sceneDepthTex);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, sceneCompareSaved);
        GlStateManager._bindTexture(maskDepthTex);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, maskCompareSaved);

        for (int i = 0; i < 3; i++) { RenderSystem.activeTexture(GL13.GL_TEXTURE0 + i); GlStateManager._bindTexture(0); }
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        MC.getMainRenderTarget().bindWrite(false);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (renderMode == RenderMode.OFF) return;
        if (MC.options.hideGui) return;
        if (OCULUS_LOADED && OculusCompat.isRenderShadow()) return;

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            boolean arDisabled = false;
            int savedFbo = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
            try {
                MultiBufferSource.BufferSource bufferSource = MC.renderBuffers().bufferSource();
                if (!OculusCompat.endBatch(bufferSource)) bufferSource.endBatch();
                if (AR_LOADED) { try { ARCompat.disableAcceleration(); arDisabled = true; } catch (Throwable t) { } }

                if (isIrisShaderpackActive()) {
                    double fov = ((GameRendererInvoker) MC.gameRenderer).invokeGetFov(MC.gameRenderer.getMainCamera(), MC.getFrameTime(), true);
                    Matrix4f unjitteredProjection = MC.gameRenderer.getProjectionMatrix(fov);

                    maskNear = unjitteredProjection.perspectiveNear();
                    maskFar = unjitteredProjection.perspectiveFar();

                    Matrix4f originalProjection = new Matrix4f(RenderSystem.getProjectionMatrix());
                    RenderSystem.setProjectionMatrix(unjitteredProjection, VertexSorting.DISTANCE_TO_ORIGIN);

                    renderThisFrame = captureMobMasks(event.getPoseStack(), unjitteredProjection);

                    RenderSystem.setProjectionMatrix(originalProjection, VertexSorting.DISTANCE_TO_ORIGIN);

                } else {
                    Matrix4f proj = event.getProjectionMatrix();
                    maskNear = proj.perspectiveNear();
                    maskFar = proj.perspectiveFar();

                    renderThisFrame = captureMobMasks(event.getPoseStack(), proj);

                    boolean chainActive = TVGShaderController.isEnabled() || NVGShaderController.isEnabled();
                    if (renderThisFrame && !chainActive) {
                        runBlur(maskTarget);
                        compositeToMain(blurVTarget, maskTarget);
                        if (renderMode == RenderMode.OUTLINE) {
                            writeDepthExtension();
                        }
                        renderThisFrame = false;
                    }
                }

            } catch (Exception e) {

            } finally {
                if (arDisabled && AR_LOADED) { try { ARCompat.resetAcceleration(); } catch (Throwable t) { } }
                if (isIrisShaderpackActive()) {
                    MC.getMainRenderTarget().bindWrite(false);
                }
                GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, savedFbo);
            }

        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL && renderThisFrame
                && (isIrisShaderpackActive() || TVGShaderController.isEnabled() || NVGShaderController.isEnabled())) {
            renderThisFrame = false;
            try {
                MultiBufferSource.BufferSource bufferSource = MC.renderBuffers().bufferSource();
                if (!OculusCompat.endBatch(bufferSource)) bufferSource.endBatch();

                occludeWithSceneDepth();
                runBlur(occludedMaskTarget);
                compositePending = true;

            } catch (Exception e) {

            } finally {
                GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, MC.getMainRenderTarget().frameBufferId);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        if (!compositePending) return;
        compositePending = false;
        if (TVGShaderController.isEnabled() || NVGShaderController.isEnabled()) return;
        if (renderMode == RenderMode.OFF || blurVTarget == null || occludedMaskTarget == null) return;
        try {
            compositeToMain(blurVTarget, occludedMaskTarget);
        } catch (Exception e) {

        } finally {
            RenderTarget main = MC.getMainRenderTarget();
            main.bindWrite(false);
            GlStateManager._viewport(0, 0, main.width, main.height);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glDisable(GL11.GL_STENCIL_TEST);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.colorMask(true, true, true, true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    private static void writeDepthExtension() {
        if (depthWriteShader == null) return;

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, MC.getMainRenderTarget().frameBufferId);
        GlStateManager._viewport(0, 0, MC.getMainRenderTarget().width, MC.getMainRenderTarget().height);

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LESS);
        RenderSystem.depthMask(true);
        GL11.glColorMask(false, false, false, false);

        GlStateManager._bindTexture(maskTarget.getDepthTextureId());
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, GL30.GL_NONE);

        depthWriteShader.use();
        depthWriteShader.setTexture("BlurSampler", blurVTarget.getColorTextureId());
        depthWriteShader.setTexture("MaskSampler", maskTarget.getColorTextureId());
        depthWriteShader.setTexture("MaskDepthSampler", maskTarget.getDepthTextureId());
        depthWriteShader.setUniform("InSize",
                (float) maskTarget.width, (float) maskTarget.height);
        drawFullscreenQuad();

        GL20.glUseProgram(0);
        GL11.glColorMask(true, true, true, true);
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        for (int i = 0; i < 3; i++) {
            RenderSystem.activeTexture(GL13.GL_TEXTURE0 + i);
            GlStateManager._bindTexture(0);
        }
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        MC.getMainRenderTarget().bindWrite(false);
    }

    public static boolean isIrisShaderpackActive() {
        if (!OCULUS_LOADED) return false;
        try {
            return net.irisshaders.iris.api.v0.IrisApi.getInstance().isShaderPackInUse();
        } catch (Throwable t) {
            return false;
        }
    }

    private static boolean captureMobMasks(PoseStack poseStack, Matrix4f projectionMatrix) {
        if (MC.level == null || maskTarget == null) return false;

        int count = 0;
        framesSinceLastCheck++;
        if (framesSinceLastCheck >= CHECK_INTERVAL) {
            for (Entity entity : MC.level.entitiesForRendering()) {
                if (outlinePredicate.test(entity)) count++;
            }
            lastEntityCount = count;
            framesSinceLastCheck = 0;
        } else {
            count = lastEntityCount;
        }

        if (count == 0) return false;
        if (!useColoredOutline && !useBlackOutline) return false;

        try {
            renderEntityMasks(poseStack, projectionMatrix);
            return true;
        } catch (Throwable t) {

            return false;
        }
    }

    private static void renderEntityMasks(PoseStack poseStack, Matrix4f projectionMatrix) {
        int targetWidth  = MC.getMainRenderTarget().width;
        int targetHeight = MC.getMainRenderTarget().height;

        if (maskTarget.width != targetWidth || maskTarget.height != targetHeight) {
            resize(targetWidth, targetHeight);
        }

        int[] savedViewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, savedViewport);
        boolean savedStencil   = GL11.glIsEnabled(GL11.GL_STENCIL_TEST);
        boolean savedScissor   = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
        boolean savedBlend     = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean savedCull      = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean savedDepthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        boolean savedDepthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        int     savedDepthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);

        if (!isIrisShaderpackActive()) {
            if (MC.getMainRenderTarget().isStencilEnabled() && !maskTarget.isStencilEnabled()) {
                maskTarget.enableStencil();
            }
            GL11.glGetError();
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, MC.getMainRenderTarget().frameBufferId);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, maskTarget.frameBufferId);
            GL30.glBlitFramebuffer(0, 0, targetWidth, targetHeight, 0, 0, maskTarget.width, maskTarget.height, GL30.GL_DEPTH_BUFFER_BIT, GL30.GL_NEAREST);
            boolean depthBlitOk = (GL11.glGetError() == GL11.GL_NO_ERROR);

            maskTarget.bindWrite(false);
            GlStateManager._viewport(0, 0, maskTarget.width, maskTarget.height);
            RenderSystem.clearColor(0, 0, 0, 0);
            RenderSystem.clear(depthBlitOk ? GL30.GL_COLOR_BUFFER_BIT : GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT, false);
        } else {
            maskTarget.bindWrite(false);
            GlStateManager._viewport(0, 0, maskTarget.width, maskTarget.height);
            RenderSystem.clearColor(0, 0, 0, 0);
            RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT, false);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.depthMask(isIrisShaderpackActive());
        RenderSystem.colorMask(true, true, true, true);
        GL11.glDisable(GL11.GL_STENCIL_TEST);

        if (maskBufferSource == null) {
            maskBuffer = new BufferBuilder(262144);
            maskBufferSource = MultiBufferSource.immediate(maskBuffer);
        }

        boolean oldRenderShadows = MC.options.entityShadows().get();
        boolean oldRenderHitboxes = MC.getEntityRenderDispatcher().shouldRenderHitBoxes();
        MC.getEntityRenderDispatcher().setRenderShadow(false);
        MC.getEntityRenderDispatcher().setRenderHitBoxes(false);

        float partialTick = MC.getFrameTime();

        Vec3 cameraPos = MC.gameRenderer.getMainCamera().getPosition();
        double camX = cameraPos.x, camY = cameraPos.y, camZ = cameraPos.z;
        Frustum frustum = new Frustum(poseStack.last().pose(), projectionMatrix);
        frustum.prepare(camX, camY, camZ);

        for (Entity entity : MC.level.entitiesForRendering()) {
            if (!outlinePredicate.test(entity)) continue;
            if (entity == MC.player && MC.options.getCameraType() == CameraType.FIRST_PERSON) continue;
            if (!MC.getEntityRenderDispatcher().shouldRender(entity, frustum, camX, camY, camZ)) continue;
            if (USE_ENTITYCULLING_CULL && EntityCullingCompat.isOccluded(entity)) continue;

            double lerpX = entity.xOld + (entity.getX() - entity.xOld) * partialTick;
            double lerpY = entity.yOld + (entity.getY() - entity.yOld) * partialTick;
            double lerpZ = entity.zOld + (entity.getZ() - entity.zOld) * partialTick;

            if (colorProvider != null) {
                int color = colorProvider.apply(entity);
                float r = ((color >> 16) & 0xFF) / 255.0f;
                float g = ((color >> 8)  & 0xFF) / 255.0f;
                float b = (color         & 0xFF) / 255.0f;
                float a = ((color >> 24) & 0xFF) / 255.0f;
                if (a == 0.0f) a = 1.0f;
                RenderSystem.setShaderColor(r, g, b, a);
            } else {
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }

            EntityMaskRenderer.renderEntityMask(entity, lerpX, lerpY, lerpZ, partialTick, poseStack, projectionMatrix, maskBufferSource);

            if (colorProvider != null) {
                if (!OculusCompat.endBatch(maskBufferSource)) maskBufferSource.endBatch();
            }
        }

        if (colorProvider == null) {
            if (!OculusCompat.endBatch(maskBufferSource)) maskBufferSource.endBatch();
        }

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        MC.getEntityRenderDispatcher().setRenderShadow(oldRenderShadows);
        MC.getEntityRenderDispatcher().setRenderHitBoxes(oldRenderHitboxes);

        if (savedStencil)   GL11.glEnable(GL11.GL_STENCIL_TEST);  else GL11.glDisable(GL11.GL_STENCIL_TEST);
        if (savedScissor)   GL11.glEnable(GL11.GL_SCISSOR_TEST);  else GL11.glDisable(GL11.GL_SCISSOR_TEST);
        if (savedBlend)     RenderSystem.enableBlend();           else RenderSystem.disableBlend();
        if (savedCull)      RenderSystem.enableCull();            else RenderSystem.disableCull();
        if (savedDepthTest) RenderSystem.enableDepthTest();       else RenderSystem.disableDepthTest();
        RenderSystem.depthMask(savedDepthMask);
        RenderSystem.depthFunc(savedDepthFunc);
        GlStateManager._viewport(savedViewport[0], savedViewport[1], savedViewport[2], savedViewport[3]);

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, MC.getMainRenderTarget().frameBufferId);
    }
}