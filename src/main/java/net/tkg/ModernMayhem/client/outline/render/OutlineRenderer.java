package net.tkg.ModernMayhem.client.outline.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.tkg.ModernMayhem.server.compat.ARCompat;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;

public class OutlineRenderer {

    private static OutlineFramebuffer maskFramebuffer;
    private static OutlineFramebuffer edgeFramebuffer;
    private static OutlineFramebuffer blurFramebuffer1;
    private static OutlineFramebuffer blurFramebuffer2;
    private static OutlineFramebuffer blackOutlineFramebuffer;

    private static OutlineShader maskShader;
    private static OutlineShader blurShader;
    private static OutlineShader applyShader;
    private static OutlineShader blackOutlineShader;

    private static int processedMaskTexture = 0;

    private static int quadVAO = -1;
    private static int quadVBO = -1;

    private static BufferBuilder maskBuffer = null;
    private static MultiBufferSource.BufferSource maskBufferSource = null;

    private static int lastEntityCount = 0;
    private static int framesSinceLastCheck = 0;
    private static final int CHECK_INTERVAL = 5;

    public enum RenderMode {
        OFF,
        OUTLINE,
        OVERLAY
    }

    private static RenderMode renderMode = RenderMode.OUTLINE;

    public static void setRenderMode(RenderMode mode) {
        renderMode = mode;
    }

    public static RenderMode getRenderMode() {
        return renderMode;
    }

    private static Predicate<Entity> outlinePredicate = entity -> entity instanceof LivingEntity;
    private static Function<Entity, Integer> colorProvider = null;

    private static float outlineR = 1.0f;
    private static float outlineG = 1.0f;
    private static float outlineB = 1.0f;
    private static float outlineA = 1.0f;

    private static boolean useColoredOutline = true;
    private static boolean useBlackOutline = true;

    public static void setOutlineColorProvider(Function<Entity, Integer> provider) { colorProvider = provider; }
    public static void setOutlinePredicate(Predicate<Entity> predicate) { outlinePredicate = predicate; }
    public static void setOutlineColor(float r, float g, float b, float a) { outlineR = r; outlineG = g; outlineB = b; outlineA = a; }
    public static void setUseColoredOutline(boolean use) { useColoredOutline = use; }
    public static boolean isUsingColoredOutline() { return useColoredOutline; }
    public static void setUseBlackOutline(boolean use) { useBlackOutline = use; }
    public static boolean isUsingBlackOutline() { return useBlackOutline; }

    public static void init() {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getWidth();
        int height = mc.getWindow().getHeight();

        maskFramebuffer = new OutlineFramebuffer(width, height, true);
        edgeFramebuffer = new OutlineFramebuffer(width, height);
        blurFramebuffer1 = new OutlineFramebuffer(width, height);
        blurFramebuffer2 = new OutlineFramebuffer(width, height);
        blackOutlineFramebuffer = new OutlineFramebuffer(width, height);

        try {
            maskShader = new OutlineShader("mask");
            blurShader = new OutlineShader("blur");
            applyShader = new OutlineShader("apply");
            blackOutlineShader = new OutlineShader("black_outline");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load outline shaders", e);
        }

        createQuadVAO();
        maskBuffer = new BufferBuilder(262144);
        maskBufferSource = MultiBufferSource.immediate(maskBuffer);
    }

    private static void minimalStateReset() {
        GL20.glUseProgram(0);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        for (int i = 0; i < 4; i++) {
            RenderSystem.activeTexture(GL13.GL_TEXTURE0 + i);
            GlStateManager._bindTexture(0);
        }
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.depthMask(true);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        GL30.glBindVertexArray(0);
    }

    private static void createQuadVAO() {
        quadVAO = GL30.glGenVertexArrays();
        quadVBO = GL30.glGenBuffers();
        float[] quadVertices = {
                -1.0f,  1.0f,  0.0f, 1.0f,
                -1.0f, -1.0f,  0.0f, 0.0f,
                1.0f, -1.0f,  1.0f, 0.0f,
                -1.0f,  1.0f,  0.0f, 1.0f,
                1.0f, -1.0f,  1.0f, 0.0f,
                1.0f,  1.0f,  1.0f, 1.0f
        };
        GL30.glBindVertexArray(quadVAO);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, quadVBO);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, quadVertices, GL30.GL_STATIC_DRAW);
        GL30.glEnableVertexAttribArray(0);
        GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        GL30.glBindVertexArray(0);
    }

    public static void resize(int width, int height) {
        if (maskFramebuffer != null) {
            maskFramebuffer.resize(width, height);
            edgeFramebuffer.resize(width, height);
            blurFramebuffer1.resize(width, height);
            blurFramebuffer2.resize(width, height);
            blackOutlineFramebuffer.resize(width, height);
        }
    }

    public static void cleanup() {
        if (maskFramebuffer != null) {
            maskFramebuffer.destroy();
            edgeFramebuffer.destroy();
            blurFramebuffer1.destroy();
            blurFramebuffer2.destroy();
            blackOutlineFramebuffer.destroy();
            maskFramebuffer = null;
        }
        if (maskShader != null) {
            maskShader.close();
            blurShader.close();
            applyShader.close();
            blackOutlineShader.close();
            maskShader = null;
        }
        if (quadVAO != -1) {
            GL30.glDeleteVertexArrays(quadVAO);
            GL30.glDeleteBuffers(quadVBO);
            quadVAO = -1;
            quadVBO = -1;
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (renderMode == RenderMode.OFF) return;

        // Unified Logic: Everything happens at AFTER_BLOCK_ENTITIES
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {

            Minecraft mc = Minecraft.getInstance();
            boolean arDisabled = false;

            try {
                // CRITICAL FIX: Flush all pending batched rendering BEFORE disabling AR
                // This ensures the depth buffer contains the complete scene including block entities
                mc.renderBuffers().bufferSource().endBatch();

                // Now disable AR to ensure immediate rendering for our mask
                try {
                    ARCompat.disableAcceleration();
                    arDisabled = true;
                } catch (Throwable ignored) {
                    // AR might not be present or failed, continue with vanilla logic
                }

                // Capture the mask with the now-complete depth buffer
                if (captureMobMasks(event.getPoseStack(), event.getProjectionMatrix())) {
                    // Draw the overlay immediately
                    drawOverlayToScreen();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Reset AR immediately after we are done
                if (arDisabled) {
                    try {
                        ARCompat.resetAcceleration();
                    } catch (Throwable ignored) {}
                }
            }
        }
    }

    private static boolean captureMobMasks(PoseStack poseStack, Matrix4f projectionMatrix) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || maskFramebuffer == null) return false;

        int count = 0;
        framesSinceLastCheck++;
        if (framesSinceLastCheck >= CHECK_INTERVAL) {
            for (Entity entity : mc.level.entitiesForRendering()) {
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
            t.printStackTrace();
            return false;
        }
    }

    private static void drawOverlayToScreen() {
        try {
            RenderSystem.clearStencil(0);
            GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);

            Minecraft mc = Minecraft.getInstance();
            // Note: We already flushed before capturing the mask, but we flush again here
            // to ensure any rendering we did in renderEntityMasks is complete
            mc.renderBuffers().bufferSource().endBatch();
            mc.getMainRenderTarget().bindWrite(false);

            extractEdges();
            applyBlur(true);
            applyBlur(false);

            if (useBlackOutline) {
                createBlackOutline();
            }

            compositeOutline();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            minimalStateReset();
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        }
    }

    private static void renderEntityMasks(PoseStack poseStack, Matrix4f projectionMatrix) {
        Minecraft mc = Minecraft.getInstance();
        int targetWidth = mc.getMainRenderTarget().width;
        int targetHeight = mc.getMainRenderTarget().height;

        if (maskFramebuffer.width != targetWidth || maskFramebuffer.height != targetHeight) {
            resize(targetWidth, targetHeight);
        }

        int mainFbo = mc.getMainRenderTarget().frameBufferId;
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mainFbo);

        int depthType = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_READ_FRAMEBUFFER,
                GL30.GL_DEPTH_ATTACHMENT, GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE);
        int depthId = GL30.glGetFramebufferAttachmentParameteri(GL30.GL_READ_FRAMEBUFFER,
                GL30.GL_DEPTH_ATTACHMENT, GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME);

        boolean canShareDepth = (depthId != 0);

        if (canShareDepth) {
            maskFramebuffer.attachExternalDepth(depthType, depthId);
        } else {
            maskFramebuffer.restoreInternalDepth();
        }

        maskFramebuffer.bind();

        RenderSystem.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
        if (canShareDepth) {
            RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);
        } else {
            RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT, false);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL30.GL_LEQUAL);

        RenderSystem.depthMask(false);
        RenderSystem.colorMask(true, true, true, true);
        GL11.glDisable(GL11.GL_STENCIL_TEST);

        if (maskBufferSource == null) {
            maskBuffer = new BufferBuilder(262144);
            maskBufferSource = MultiBufferSource.immediate(maskBuffer);
        }

        boolean oldRenderShadows = mc.options.entityShadows().get();
        boolean oldRenderHitboxes = mc.getEntityRenderDispatcher().shouldRenderHitBoxes();

        mc.getEntityRenderDispatcher().setRenderShadow(false);
        mc.getEntityRenderDispatcher().setRenderHitBoxes(false);

        float partialTick = mc.getFrameTime();

        // Get render distance in blocks
        int renderDistanceChunks = mc.options.renderDistance().get();
        double renderDistanceBlocks = renderDistanceChunks * 16.0;

        // Check if camera is underwater or in lava - adjust render distance to match fog
        FogType fogType = mc.gameRenderer.getMainCamera().getFluidInCamera();
        if (fogType == FogType.WATER || fogType == FogType.LAVA) {
            // Underwater fog is very short, limit to about 16-32 blocks to prevent depth issues
            renderDistanceBlocks = Math.min(renderDistanceBlocks, 32.0);
        }

        double renderDistanceSquared = renderDistanceBlocks * renderDistanceBlocks;

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (!outlinePredicate.test(entity)) continue;
            if (entity == mc.player && mc.options.getCameraType() == CameraType.FIRST_PERSON) continue;

            double lerpX = entity.xOld + (entity.getX() - entity.xOld) * partialTick;
            double lerpY = entity.yOld + (entity.getY() - entity.yOld) * partialTick;
            double lerpZ = entity.zOld + (entity.getZ() - entity.zOld) * partialTick;

            // Check if entity is within render distance
            Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
            double dx = lerpX - cameraPos.x;
            double dy = lerpY - cameraPos.y;
            double dz = lerpZ - cameraPos.z;
            double distanceSquared = dx * dx + dy * dy + dz * dz;

            if (distanceSquared > renderDistanceSquared) continue;

            if (colorProvider != null) {
                int color = colorProvider.apply(entity);
                float r = ((color >> 16) & 0xFF) / 255.0f;
                float g = ((color >> 8) & 0xFF) / 255.0f;
                float b = (color & 0xFF) / 255.0f;
                float a = ((color >> 24) & 0xFF) / 255.0f;
                if (a == 0.0f) a = 1.0f;

                RenderSystem.setShaderColor(r, g, b, a);
            } else {
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }

            EntityMaskRenderer.renderEntityMask(entity, lerpX, lerpY, lerpZ, partialTick, poseStack, projectionMatrix, maskBufferSource);

            if (colorProvider != null) {
                maskBufferSource.endBatch();
            }
        }

        if (colorProvider == null) {
            maskBufferSource.endBatch();
        }

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        mc.getEntityRenderDispatcher().setRenderShadow(oldRenderShadows);
        mc.getEntityRenderDispatcher().setRenderHitBoxes(oldRenderHitboxes);

        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(GL30.GL_LEQUAL);
        maskFramebuffer.restoreInternalDepth();
        mc.getMainRenderTarget().bindWrite(false);
    }

    private static void extractEdges() {
        edgeFramebuffer.bind();
        RenderSystem.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
        RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        GL30.glBindVertexArray(quadVAO);

        maskShader.use();
        int sourceMaskTex = (processedMaskTexture != 0) ? processedMaskTexture : maskFramebuffer.getColorTexture();
        maskShader.setTexture("DiffuseSampler", sourceMaskTex);
        maskShader.setUniform("InSize", (float) maskFramebuffer.width, (float) maskFramebuffer.height);
        maskShader.setUniform("OutSize", (float) edgeFramebuffer.width, (float) edgeFramebuffer.height);

        drawFullscreenQuad();

        blurFramebuffer1.bind();
        RenderSystem.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
        RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);

        applyShader.use();
        applyShader.setTexture("DiffuseSampler", edgeFramebuffer.getColorTexture());
        applyShader.setUniform("OutlineColor", 1.0f, 1.0f, 1.0f, 1.0f);
        applyShader.setUniform("UseSourceColor", 1);

        drawFullscreenQuad();

        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    }

    private static void applyBlur(boolean horizontal) {
        OutlineFramebuffer source = horizontal ? blurFramebuffer1 : blurFramebuffer2;
        OutlineFramebuffer target = horizontal ? blurFramebuffer2 : blurFramebuffer1;

        target.bind();
        RenderSystem.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
        RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableBlend();

        GL30.glBindVertexArray(quadVAO);

        blurShader.use();
        blurShader.setTexture("DiffuseSampler", source.getColorTexture());
        blurShader.setUniform("InSize", (float) source.width, (float) source.height);
        blurShader.setUniform("OutSize", (float) target.width, (float) target.height);
        blurShader.setUniform("BlurDir", horizontal ? 1.0f : 0.0f, horizontal ? 0.0f : 1.0f);
        blurShader.setUniform("Radius", 1.0f);

        drawFullscreenQuad();

        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    }

    private static void createBlackOutline() {
        blackOutlineFramebuffer.bind();
        RenderSystem.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
        RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableBlend();

        GL30.glBindVertexArray(quadVAO);

        blackOutlineShader.use();
        blackOutlineShader.setTexture("DiffuseSampler", edgeFramebuffer.getColorTexture());
        blackOutlineShader.setTexture("EntityMask", maskFramebuffer.getColorTexture());
        blackOutlineShader.setUniform("InSize", (float) edgeFramebuffer.width, (float) edgeFramebuffer.height);
        blackOutlineShader.setUniform("OutSize", (float) blackOutlineFramebuffer.width, (float) blackOutlineFramebuffer.height);
        blackOutlineShader.setUniform("Radius", 1.5f);

        drawFullscreenQuad();

        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    }

    private static void compositeOutline() {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
        );

        GL30.glBindVertexArray(quadVAO);

        int sourceMaskTex = (processedMaskTexture != 0) ? processedMaskTexture : maskFramebuffer.getColorTexture();

        if (renderMode == RenderMode.OUTLINE) {
            if (useBlackOutline) {
                applyShader.use();
                applyShader.setTexture("DiffuseSampler", blackOutlineFramebuffer.getColorTexture());
                applyShader.setUniform("OutlineColor", 0.0f, 0.0f, 0.0f, 1.0f);
                applyShader.setUniform("UseSourceColor", 0);
                drawFullscreenQuad();
            }

            if (useColoredOutline) {
                applyShader.use();
                applyShader.setTexture("DiffuseSampler", blurFramebuffer1.getColorTexture());
                applyShader.setUniform("OutlineColor", outlineR, outlineG, outlineB, outlineA);

                if (colorProvider != null) {
                    applyShader.setUniform("UseSourceColor", 1);
                } else {
                    applyShader.setUniform("UseSourceColor", 0);
                }

                drawFullscreenQuad();
            }
        } else if (renderMode == RenderMode.OVERLAY) {
            applyShader.use();
            applyShader.setTexture("DiffuseSampler", sourceMaskTex);
            applyShader.setUniform("OutlineColor", outlineR, outlineG, outlineB, outlineA);

            if (colorProvider != null) {
                applyShader.setUniform("UseSourceColor", 1);
            } else {
                applyShader.setUniform("UseSourceColor", 0);
            }
            drawFullscreenQuad();
        }

        processedMaskTexture = 0;
        RenderSystem.disableBlend();
    }

    private static void drawFullscreenQuad() {
        GL30.glBindVertexArray(quadVAO);
        GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 6);
    }
}