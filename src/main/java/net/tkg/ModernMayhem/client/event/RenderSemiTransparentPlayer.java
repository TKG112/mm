package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderSemiTransparentPlayer {

    // Flag to prevent recursive rendering when we render the player to the ghost target.
    private static final ThreadLocal<Boolean> IS_CUSTOM_PLAYER_RENDER =
            ThreadLocal.withInitial(() -> false);

    // Flag to set the alpha value TODO: Replace with config option (and maybe armor quality ?)
    private static final float GLOBAL_ALPHA = 0.40F;

    // Depth bias to reduce z-fighting when compositing the ghost target over the main target.
    private static final float DEPTH_BIAS = 0.0005F;

    // Temporary flag TODO: Find out if the rendering works with Oculus shaders, and if not find a workaround (like skipping the custom render when Oculus shaders are detected).
    private static final boolean ATTEMPT_WITH_OCULUS = true;

    private static final List<QueuedPlayerRender> QUEUED_RENDERS = new ArrayList<>();

    // Cached pose stack from the world render, to be used when rendering the player to the ghost target.
    private static final PoseStack CACHED_WORLD_POSE_STACK = new PoseStack();
    private static boolean HAS_CACHED_WORLD_POSE_STACK = false;

    @Nullable
    private static ShaderInstance DEPTH_COMPOSITE_SHADER;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        if (IS_CUSTOM_PLAYER_RENDER.get()) {
            return;
        }

        AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();

        if (!shouldRenderPlayerAsSemiTransparent(player)) {
            return;
        }

        if (!canAttemptRender()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }

        event.setCanceled(true);

        QUEUED_RENDERS.add(new QueuedPlayerRender(
                player,
                event.getPartialTick(),
                event.getPackedLight()
        ));
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            copyPoseStack(event.getPoseStack(), CACHED_WORLD_POSE_STACK);
            HAS_CACHED_WORLD_POSE_STACK = true;
            return;
        }

        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            return;
        }

        if (QUEUED_RENDERS.isEmpty()) {
            return;
        }

        if (!HAS_CACHED_WORLD_POSE_STACK) {
            QUEUED_RENDERS.clear();
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            QUEUED_RENDERS.clear();
            HAS_CACHED_WORLD_POSE_STACK = false;
            return;
        }

        if (DEPTH_COMPOSITE_SHADER == null) {
            QUEUED_RENDERS.clear();
            HAS_CACHED_WORLD_POSE_STACK = false;
            return;
        }

        RenderTarget mainTarget = mc.getMainRenderTarget();
        DepthAwareTargets.ensure(mainTarget.width, mainTarget.height);

        // Copy the scene depth to our custom depth texture so that we can use it in the shader when compositing the ghost target over the main target.
        DepthAwareTargets.copySceneDepth(mainTarget);

        Camera camera = event.getCamera();

        for (QueuedPlayerRender queued : QUEUED_RENDERS) {
            renderQueuedPlayerToGhostTarget(queued, camera, CACHED_WORLD_POSE_STACK);
            compositeGhostToMain(mainTarget, GLOBAL_ALPHA, DEPTH_BIAS);
        }


        QUEUED_RENDERS.clear();
        HAS_CACHED_WORLD_POSE_STACK = false;
    }

    public static boolean shouldRenderPlayerAsSemiTransparent(Player player) {
        return true;
    }

    // TODO: Replace with better check
    private static boolean canAttemptRender() {
        if (!ModList.get().isLoaded("oculus")) {
            return true;
        }

        return ATTEMPT_WITH_OCULUS;
    }

    private static void renderQueuedPlayerToGhostTarget(
            QueuedPlayerRender queued,
            Camera camera,
            PoseStack capturedWorldPoseStack
    ) {
        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        RenderTarget mainTarget = mc.getMainRenderTarget();

        DepthAwareTargets.bindGhostForWrite();

        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        double px = Mth.lerp(queued.partialTick(), queued.player().xOld, queued.player().getX());
        double py = Mth.lerp(queued.partialTick(), queued.player().yOld, queued.player().getY());
        double pz = Mth.lerp(queued.partialTick(), queued.player().zOld, queued.player().getZ());

        Vec3 camPos = camera.getPosition();

        double relX = px - camPos.x;
        double relY = py - camPos.y;
        double relZ = pz - camPos.z;

        float entityYaw = Mth.rotLerp(queued.partialTick(), queued.player().yRotO, queued.player().getYRot());

        PoseStack poseStack = new PoseStack();
        copyPoseStack(capturedWorldPoseStack, poseStack);

        try {
            IS_CUSTOM_PLAYER_RENDER.set(true);

            dispatcher.overrideCameraOrientation(camera.rotation());
            dispatcher.setRenderShadow(false);

            RenderSystem.enableDepthTest();

            dispatcher.render(
                    queued.player(),
                    relX,
                    relY,
                    relZ,
                    entityYaw,
                    queued.partialTick(),
                    poseStack,
                    bufferSource,
                    queued.packedLight()
            );
        } finally {
            bufferSource.endBatch();
            dispatcher.setRenderShadow(true);
            IS_CUSTOM_PLAYER_RENDER.set(false);
            mainTarget.bindWrite(false);
            RenderSystem.viewport(0, 0, mainTarget.viewWidth, mainTarget.viewHeight);
            RenderSystem.enableDepthTest();
        }
    }

    private static void compositeGhostToMain(RenderTarget mainTarget, float alpha, float depthBias) {
        if (DEPTH_COMPOSITE_SHADER == null) {
            return;
        }

        mainTarget.bindWrite(false);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();

        DEPTH_COMPOSITE_SHADER.setSampler("GhostColorSampler", DepthAwareTargets.ghostColorTexId);
        DEPTH_COMPOSITE_SHADER.setSampler("GhostDepthSampler", DepthAwareTargets.ghostDepthTexId);
        DEPTH_COMPOSITE_SHADER.setSampler("SceneDepthSampler", DepthAwareTargets.sceneDepthTexId);

        if (DEPTH_COMPOSITE_SHADER.getUniform("GlobalAlpha") != null) {
            DEPTH_COMPOSITE_SHADER.getUniform("GlobalAlpha").set(alpha);
        }
        if (DEPTH_COMPOSITE_SHADER.getUniform("DepthBias") != null) {
            DEPTH_COMPOSITE_SHADER.getUniform("DepthBias").set(depthBias);
        }

        RenderSystem.setShader(() -> DEPTH_COMPOSITE_SHADER);

        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // Full screen quad vertices with UVs
        builder.vertex(-1.0F, -1.0F, 0.0F).uv(0.0F, 0.0F).endVertex();
        builder.vertex( 1.0F, -1.0F, 0.0F).uv(1.0F, 0.0F).endVertex();
        builder.vertex( 1.0F,  1.0F, 0.0F).uv(1.0F, 1.0F).endVertex();
        builder.vertex(-1.0F,  1.0F, 0.0F).uv(0.0F, 1.0F).endVertex();

        BufferUploader.drawWithShader(builder.end());

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    @Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusHandler {

        @SubscribeEvent
        public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
            event.registerShader(
                    new ShaderInstance(
                            event.getResourceProvider(),
                            ResourceLocation.fromNamespaceAndPath(ModernMayhemMod.ID, "ghost_player_depth"),
                            DefaultVertexFormat.POSITION_TEX
                    ),
                    shader -> DEPTH_COMPOSITE_SHADER = shader
            );
        }
    }

    private static void copyPoseStack(PoseStack from, PoseStack to) {
        to.last().pose().set(from.last().pose());
        to.last().normal().set(from.last().normal());
    }

    private record QueuedPlayerRender(
            AbstractClientPlayer player,
            float partialTick,
            int packedLight
    ) {
    }

    public static final class DepthAwareTargets {

        private static int width = -1;
        private static int height = -1;

        private static int ghostFboId = 0;
        public static int ghostColorTexId = 0;
        public static int ghostDepthTexId = 0;

        private static int sceneDepthFboId = 0;
        public static int sceneDepthTexId = 0;

        private DepthAwareTargets() {
        }

        public static void ensure(int targetWidth, int targetHeight) {
            if (width == targetWidth && height == targetHeight && ghostFboId != 0 && sceneDepthFboId != 0) {
                return;
            }

            destroy();

            width = targetWidth;
            height = targetHeight;

            createGhostTarget();
            createSceneDepthTarget();
        }

        private static void createGhostTarget() {
            ghostFboId = GL30.glGenFramebuffers();
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, ghostFboId);

            ghostColorTexId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ghostColorTexId);
            GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL11.GL_RGBA8,
                    width,
                    height,
                    0,
                    GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE,
                    0
            );
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL30.glFramebufferTexture2D(
                    GL30.GL_FRAMEBUFFER,
                    GL30.GL_COLOR_ATTACHMENT0,
                    GL11.GL_TEXTURE_2D,
                    ghostColorTexId,
                    0
            );

            ghostDepthTexId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ghostDepthTexId);
            GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL30.GL_DEPTH_COMPONENT24,
                    width,
                    height,
                    0,
                    GL11.GL_DEPTH_COMPONENT,
                    GL11.GL_FLOAT,
                    0
            );
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL30.glFramebufferTexture2D(
                    GL30.GL_FRAMEBUFFER,
                    GL30.GL_DEPTH_ATTACHMENT,
                    GL11.GL_TEXTURE_2D,
                    ghostDepthTexId,
                    0
            );

            checkComplete("ghost");
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        }

        private static void createSceneDepthTarget() {
            sceneDepthFboId = GL30.glGenFramebuffers();
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, sceneDepthFboId);

            sceneDepthTexId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, sceneDepthTexId);
            GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL30.GL_DEPTH_COMPONENT24,
                    width,
                    height,
                    0,
                    GL11.GL_DEPTH_COMPONENT,
                    GL11.GL_FLOAT,
                    0
            );
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL30.glFramebufferTexture2D(
                    GL30.GL_FRAMEBUFFER,
                    GL30.GL_DEPTH_ATTACHMENT,
                    GL11.GL_TEXTURE_2D,
                    sceneDepthTexId,
                    0
            );

            GL11.glDrawBuffer(GL11.GL_NONE);
            GL11.glReadBuffer(GL11.GL_NONE);

            checkComplete("scene_depth");
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        }

        public static void bindGhostForWrite() {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, ghostFboId);
            RenderSystem.viewport(0, 0, width, height);
            RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
            RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
        }

        public static void copySceneDepth(RenderTarget mainTarget) {
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mainTarget.frameBufferId);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, sceneDepthFboId);

            GL30.glBlitFramebuffer(
                    0, 0, width, height,
                    0, 0, width, height,
                    GL11.GL_DEPTH_BUFFER_BIT,
                    GL11.GL_NEAREST
            );

            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mainTarget.frameBufferId);
            RenderSystem.viewport(0, 0, mainTarget.viewWidth, mainTarget.viewHeight);
        }

        private static void checkComplete(String name) {
            int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
            if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
                throw new IllegalStateException("Framebuffer '" + name + "' incomplete: " + status);
            }
        }

        private static void destroy() {
            if (ghostColorTexId != 0) {
                GL11.glDeleteTextures(ghostColorTexId);
                ghostColorTexId = 0;
            }
            if (ghostDepthTexId != 0) {
                GL11.glDeleteTextures(ghostDepthTexId);
                ghostDepthTexId = 0;
            }
            if (sceneDepthTexId != 0) {
                GL11.glDeleteTextures(sceneDepthTexId);
                sceneDepthTexId = 0;
            }
            if (ghostFboId != 0) {
                GL30.glDeleteFramebuffers(ghostFboId);
                ghostFboId = 0;
            }
            if (sceneDepthFboId != 0) {
                GL30.glDeleteFramebuffers(sceneDepthFboId);
                sceneDepthFboId = 0;
            }
        }
    }
}