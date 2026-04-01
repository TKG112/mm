package net.tkg.ModernMayhem.client.shaderRenderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.tkg.ModernMayhem.client.compat.ar.ARCompat;
import net.tkg.ModernMayhem.client.compat.oculus.OculusCompat;
import net.tkg.ModernMayhem.client.outline.OutlineFramebuffer;
import net.tkg.ModernMayhem.client.outline.OutlineShader;
import net.tkg.ModernMayhem.client.outline.ThermalMaskPass;
import net.tkg.ModernMayhem.client.shaderController.ThermalHighlightShaderController;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ThermalHighlightRenderer {

    private static final boolean OCULUS_LOADED = ModList.get().isLoaded("oculus");
    private static final boolean AR_LOADED = ModList.get().isLoaded("acceleratedrendering");

    private static OutlineFramebuffer maskFramebuffer;
    private static OutlineFramebuffer edgeFramebuffer;
    private static OutlineFramebuffer blurFramebuffer1;
    private static OutlineFramebuffer blurFramebuffer2;

    private static OutlineShader maskShader;
    private static OutlineShader blurShader;
    private static OutlineShader compositeShader;

    private static BufferBuilder maskBuffer;
    private static MultiBufferSource.BufferSource maskBufferSource;

    private static int quadVAO = -1;
    private static int quadVBO = -1;

    private ThermalHighlightRenderer() {}

    public static void init() {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getWidth();
        int height = mc.getWindow().getHeight();

        maskFramebuffer = new OutlineFramebuffer(width, height, true);
        edgeFramebuffer = new OutlineFramebuffer(width, height);
        blurFramebuffer1 = new OutlineFramebuffer(width, height);
        blurFramebuffer2 = new OutlineFramebuffer(width, height);

        try {
            maskShader = new OutlineShader("mask");
            blurShader = new OutlineShader("blur");
            compositeShader = new OutlineShader("apply");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load thermal highlight shaders", e);
        }

        createQuadVAO();

        maskBuffer = new BufferBuilder(262144);
        maskBufferSource = MultiBufferSource.immediate(maskBuffer);
    }

    public static void resize(int width, int height) {
        if (maskFramebuffer == null) return;
        maskFramebuffer.resize(width, height);
        edgeFramebuffer.resize(width, height);
        blurFramebuffer1.resize(width, height);
        blurFramebuffer2.resize(width, height);
    }

    public static void cleanup() {
        if (maskFramebuffer != null) {
            maskFramebuffer.destroy();
            edgeFramebuffer.destroy();
            blurFramebuffer1.destroy();
            blurFramebuffer2.destroy();
            maskFramebuffer = null;
            edgeFramebuffer = null;
            blurFramebuffer1 = null;
            blurFramebuffer2 = null;
        }

        if (maskShader != null) {
            maskShader.close();
            blurShader.close();
            compositeShader.close();
            maskShader = null;
            blurShader = null;
            compositeShader = null;
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
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) return;

        ThermalHighlightShaderController.recompute();
        if (!ThermalHighlightShaderController.isEnabled()) return;
        if (ThermalHighlightShaderController.getMode() == ThermalHighlightShaderController.ThermalHighlightMode.OFF) return;

        if (OCULUS_LOADED && OculusCompat.isRenderShadow()) return;

        Minecraft mc = Minecraft.getInstance();
        boolean arDisabled = false;

        try {
            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
            if (!OculusCompat.endBatch(bufferSource)) {
                bufferSource.endBatch();
            }

            if (AR_LOADED) {
                ARCompat.disableAcceleration();
                arDisabled = true;
            }

            if (captureEntityMask(event.getPoseStack())) {
                extractMask();
                applyBlur(true);
                applyBlur(false);
                compositeToScreen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (arDisabled && AR_LOADED) {
                try {
                    ARCompat.resetAcceleration();
                } catch (Throwable ignored) {}
            }
            minimalStateReset();
            mc.getMainRenderTarget().bindWrite(false);
        }
    }

    private static boolean captureEntityMask(PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || maskFramebuffer == null) return false;

        Predicate<Entity> predicate = ThermalHighlightShaderController.getPredicate();
        Function<Entity, Integer> colorProvider = ThermalHighlightShaderController.getColorProvider();

        int targetWidth = mc.getMainRenderTarget().width;
        int targetHeight = mc.getMainRenderTarget().height;
        if (maskFramebuffer.width != targetWidth || maskFramebuffer.height != targetHeight) {
            resize(targetWidth, targetHeight);
        }

        int mainFbo = mc.getMainRenderTarget().frameBufferId;
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mainFbo);

        int depthType = GL30.glGetFramebufferAttachmentParameteri(
                GL30.GL_READ_FRAMEBUFFER,
                GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE
        );
        int depthId = GL30.glGetFramebufferAttachmentParameteri(
                GL30.GL_READ_FRAMEBUFFER,
                GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME
        );

        boolean canShareDepth = depthId != 0;
        if (canShareDepth) {
            maskFramebuffer.attachExternalDepth(depthType, depthId);
        } else {
            maskFramebuffer.restoreInternalDepth();
        }

        maskFramebuffer.bind();
        RenderSystem.clearColor(0f, 0f, 0f, 0f);
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

        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        boolean oldShadows = mc.options.entityShadows().get();
        boolean oldHitboxes = dispatcher.shouldRenderHitBoxes();

        dispatcher.setRenderShadow(false);
        dispatcher.setRenderHitBoxes(false);

        float partialTick = mc.getFrameTime();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (!predicate.test(entity)) continue;
            if (mc.gameRenderer.getMainCamera().getFluidInCamera() == FogType.LAVA) continue;

            double lerpX = entity.xOld + (entity.getX() - entity.xOld) * partialTick;
            double lerpY = entity.yOld + (entity.getY() - entity.yOld) * partialTick;
            double lerpZ = entity.zOld + (entity.getZ() - entity.zOld) * partialTick;

            if (colorProvider != null) {
                int rgb = colorProvider.apply(entity);
                float r = ((rgb >> 16) & 0xFF) / 255.0f;
                float g = ((rgb >> 8) & 0xFF) / 255.0f;
                float b = (rgb & 0xFF) / 255.0f;
                RenderSystem.setShaderColor(r, g, b, 1.0f);
            } else {
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            }

            ThermalMaskPass.renderEntityMask(
                    entity,
                    lerpX, lerpY, lerpZ,
                    partialTick,
                    poseStack,
                    maskBufferSource
            );

            if (colorProvider != null && !OculusCompat.endBatch(maskBufferSource)) {
                maskBufferSource.endBatch();
            }
        }

        if (colorProvider == null && !OculusCompat.endBatch(maskBufferSource)) {
            maskBufferSource.endBatch();
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        dispatcher.setRenderShadow(oldShadows);
        dispatcher.setRenderHitBoxes(oldHitboxes);

        RenderSystem.depthMask(true);
        maskFramebuffer.restoreInternalDepth();
        mc.getMainRenderTarget().bindWrite(false);

        return true;
    }

    private static void extractMask() {
        edgeFramebuffer.bind();
        RenderSystem.clearColor(0f, 0f, 0f, 0f);
        RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        maskShader.use();
        maskShader.setTexture("DiffuseSampler", maskFramebuffer.getColorTexture());
        maskShader.setUniform("InSize", (float) maskFramebuffer.width, (float) maskFramebuffer.height);
        maskShader.setUniform("OutSize", (float) edgeFramebuffer.width, (float) edgeFramebuffer.height);

        drawFullscreenQuad();
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    }

    private static void applyBlur(boolean horizontal) {
        OutlineFramebuffer source = horizontal ? edgeFramebuffer : blurFramebuffer1;
        OutlineFramebuffer target = horizontal ? blurFramebuffer1 : blurFramebuffer2;

        target.bind();
        RenderSystem.clearColor(0f, 0f, 0f, 0f);
        RenderSystem.clear(GL30.GL_COLOR_BUFFER_BIT, false);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        blurShader.use();
        blurShader.setTexture("DiffuseSampler", source.getColorTexture());
        blurShader.setUniform("InSize", (float) source.width, (float) source.height);
        blurShader.setUniform("OutSize", (float) target.width, (float) target.height);
        blurShader.setUniform("Horizontal", horizontal ? 1 : 0);
        blurShader.setUniform("Radius", ThermalHighlightShaderController.getBlurRadius());

        drawFullscreenQuad();
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    }

    private static void compositeToScreen() {
        Minecraft mc = Minecraft.getInstance();
        mc.getMainRenderTarget().bindWrite(false);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
        );

        compositeShader.use();
        compositeShader.setTexture("DiffuseSampler", edgeFramebuffer.getColorTexture());
        compositeShader.setTexture("GlowSampler", blurFramebuffer2.getColorTexture());

        compositeShader.setUniform(
                "OutlineColor",
                ThermalHighlightShaderController.getColorR(),
                ThermalHighlightShaderController.getColorG(),
                ThermalHighlightShaderController.getColorB(),
                ThermalHighlightShaderController.getColorA()
        );

        compositeShader.setUniform("FillAlpha", ThermalHighlightShaderController.getFillAlpha());
        compositeShader.setUniform("OutlineAlpha", ThermalHighlightShaderController.getOutlineAlpha());
        compositeShader.setUniform("GlowStrength", ThermalHighlightShaderController.getGlowStrength());

        int modeValue = switch (ThermalHighlightShaderController.getMode()) {
            case OFF -> 0;
            case OUTLINE -> 1;
            case FILL -> 2;
            case FILL_AND_OUTLINE -> 3;
        };
        compositeShader.setUniform("Mode", modeValue);

        drawFullscreenQuad();

        RenderSystem.disableBlend();
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

    private static void drawFullscreenQuad() {
        GL30.glBindVertexArray(quadVAO);
        GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 6);
        GL30.glBindVertexArray(0);
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
}