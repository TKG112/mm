package net.tkg.ModernMayhem.client.thermal.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.compat.ar.ARCompat;
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

    private static TextureTarget maskTarget;
    private static TextureTarget blurHTarget;
    private static TextureTarget blurVTarget;
    private static TextureTarget occludedMaskTarget;

    private static ThermalShader blurShader;
    private static ThermalShader applyShader;
    private static ThermalShader depthWriteShader;
    private static ThermalShader depthOccludeShader;

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

    public enum RenderMode { OFF, OUTLINE, OVERLAY }

    private static RenderMode renderMode = RenderMode.OUTLINE;
    private static Predicate<Entity> outlinePredicate = entity -> entity instanceof LivingEntity;
    private static Function<Entity, Integer> colorProvider = null;

    private static float outlineR = 1.0f, outlineG = 1.0f, outlineB = 1.0f, outlineA = 1.0f;
    private static boolean useColoredOutline = true;
    private static boolean useBlackOutline = true;

    public static void setRenderMode(RenderMode mode) { renderMode = mode; }
    public static RenderMode getRenderMode() { return renderMode; }
    public static void setOutlineColorProvider(Function<Entity, Integer> provider) { colorProvider = provider; }
    public static void setOutlinePredicate(Predicate<Entity> predicate) { outlinePredicate = predicate; }
    public static void setOutlineColor(float r, float g, float b, float a) { outlineR = r; outlineG = g; outlineB = b; outlineA = a; }
    public static void setUseColoredOutline(boolean use) { useColoredOutline = use; }
    public static boolean isUsingColoredOutline() { return useColoredOutline; }
    public static void setUseBlackOutline(boolean use) { useBlackOutline = use; }
    public static boolean isUsingBlackOutline() { return useBlackOutline; }

    public static void init() {
        Minecraft mc = Minecraft.getInstance();
        int width  = mc.getWindow().getWidth();
        int height = mc.getWindow().getHeight();

        maskTarget  = new TextureTarget(width, height, true, Minecraft.ON_OSX);
        blurHTarget = new TextureTarget(width, height, false, Minecraft.ON_OSX);
        blurVTarget = new TextureTarget(width, height, false, Minecraft.ON_OSX);
        occludedMaskTarget = new TextureTarget(width, height, false, Minecraft.ON_OSX);
        maskTarget.setClearColor(0, 0, 0, 0);
        blurHTarget.setClearColor(0, 0, 0, 0);
        blurVTarget.setClearColor(0, 0, 0, 0);
        occludedMaskTarget.setClearColor(0, 0, 0, 0);

        try {
            blurShader  = new ThermalShader("thermal_blur", "thermal_sobel");
            applyShader = new ThermalShader("thermal_apply", "thermal_sobel");
            depthWriteShader = new ThermalShader("thermal_depth", "thermal_sobel");
            depthOccludeShader = new ThermalShader("thermal_occlude", "thermal_sobel");
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
        if (blurShader  != null) { blurShader.close();  blurShader  = null; }
        if (applyShader != null) { applyShader.close(); applyShader = null; }
        if (depthWriteShader != null) { depthWriteShader.close(); depthWriteShader = null; }
        if (depthOccludeShader != null) { depthOccludeShader.close(); depthOccludeShader = null; }
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

        GL11.glColorMask(true, true, true, false);

        applyShader.use();
        applyShader.setTexture("BlurSampler", blurSrc.getColorTextureId());
        applyShader.setTexture("MaskSampler", maskSrc.getColorTextureId());
        applyShader.setUniform("RenderMode",     renderMode == RenderMode.OVERLAY ? 2f : 1f);
        applyShader.setUniform("UseSourceColor", colorProvider != null ? 1f : 0f);
        applyShader.setUniform("OutlineColor",   outlineR, outlineG, outlineB, outlineA);
        drawFullscreenQuad();

        GL11.glColorMask(true, true, true, true);
        GL20.glUseProgram(0);
        for (int i = 0; i < 4; i++) { RenderSystem.activeTexture(GL13.GL_TEXTURE0 + i); GlStateManager._bindTexture(0); }
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        MC.getMainRenderTarget().bindWrite(false);
    }

    private static void occludeWithSceneDepth() {
        if (depthOccludeShader == null || occludedMaskTarget == null) return;

        boolean reversedZ = GL11.glGetFloat(GL11.GL_DEPTH_CLEAR_VALUE) < 0.1f;

        occludedMaskTarget.bindWrite(false);
        GlStateManager._viewport(0, 0, occludedMaskTarget.width, occludedMaskTarget.height);
        RenderSystem.clearColor(0, 0, 0, 0);
        RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableBlend();

        int maskDepthTex  = maskTarget.getDepthTextureId();
        int sceneDepthTex = MC.getMainRenderTarget().getDepthTextureId();

        GlStateManager._bindTexture(maskDepthTex);
        int maskCompareSaved = GL30.glGetTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, GL30.GL_NONE);

        GlStateManager._bindTexture(sceneDepthTex);
        int sceneCompareSaved = GL30.glGetTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, GL30.GL_NONE);

        GlStateManager._bindTexture(0);

        depthOccludeShader.use();
        depthOccludeShader.setTexture("MaskSampler",        maskTarget.getColorTextureId());
        depthOccludeShader.setTexture("EntityDepthSampler", maskDepthTex);
        depthOccludeShader.setTexture("SceneDepthSampler",  sceneDepthTex);
        depthOccludeShader.setUniform("InSize",      (float) maskTarget.width, (float) maskTarget.height);
        depthOccludeShader.setUniform("IsReversedZ", reversedZ ? 1.0f : 0.0f);
        depthOccludeShader.setUniform("NearFar",     maskNear, maskFar);
        depthOccludeShader.setUniform("WorldEpsilon", 0.25f);
        drawFullscreenQuad();

        GL20.glUseProgram(0);

        GlStateManager._bindTexture(sceneDepthTex);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, sceneCompareSaved);
        GlStateManager._bindTexture(maskDepthTex);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, maskCompareSaved);

        for (int i = 0; i < 3; i++) { RenderSystem.activeTexture(GL13.GL_TEXTURE0 + i); GlStateManager._bindTexture(0); }
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
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
                    renderThisFrame = captureMobMasks(event.getPoseStack(), event.getProjectionMatrix());

                    if (renderThisFrame) {
                        runBlur(maskTarget);
                        compositeToMain(blurVTarget, maskTarget);
                        if (renderMode == RenderMode.OUTLINE) {
                            writeDepthExtension();
                        }
                        renderThisFrame = false;
                    }
                }

            } catch (Exception e) {
                ModernMayhemMod.LOGGER.error("[ThermalRenderer] Capture error", e);
            } finally {
                if (arDisabled && AR_LOADED) { try { ARCompat.resetAcceleration(); } catch (Throwable t) { } }
                if (isIrisShaderpackActive()) {
                    MC.getMainRenderTarget().bindWrite(false);
                }
                GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, savedFbo);
            }

        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL && isIrisShaderpackActive() && renderThisFrame) {
            renderThisFrame = false;
            try {
                MultiBufferSource.BufferSource bufferSource = MC.renderBuffers().bufferSource();
                if (!OculusCompat.endBatch(bufferSource)) bufferSource.endBatch();

                occludeWithSceneDepth();
                runBlur(occludedMaskTarget);
                compositePending = true;

            } catch (Exception e) {
                ModernMayhemMod.LOGGER.error("[ThermalRenderer] Iris prepare error", e);
            } finally {
                GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, MC.getMainRenderTarget().frameBufferId);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        if (!compositePending) return;
        compositePending = false;
        if (renderMode == RenderMode.OFF || blurVTarget == null || occludedMaskTarget == null) return;
        try {
            compositeToMain(blurVTarget, occludedMaskTarget);
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.error("[ThermalRenderer] GUI composite error", e);
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

    private static boolean isIrisShaderpackActive() {
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
            ModernMayhemMod.LOGGER.error("[ThermalRenderer] Error rendering entity masks", t);
            return false;
        }
    }

    private static void renderEntityMasks(PoseStack poseStack, Matrix4f projectionMatrix) {
        int targetWidth = MC.getMainRenderTarget().width;
        int targetHeight = MC.getMainRenderTarget().height;

        if (maskTarget.width != targetWidth || maskTarget.height != targetHeight) {
            resize(targetWidth, targetHeight);
        }

        int[] savedViewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, savedViewport);
        boolean savedStencil = GL11.glIsEnabled(GL11.GL_STENCIL_TEST);
        boolean savedScissor = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
        boolean savedBlend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean savedCull = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean savedDepthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        boolean savedDepthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        int savedDepthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);

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