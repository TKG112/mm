package net.tkg.ModernMayhem.client.thermal.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class EntityMaskRenderer {

    public static void renderEntityMask(Entity entity, double lerpX, double lerpY, double lerpZ,
                                        float partialTick, PoseStack poseStack, Matrix4f projectionMatrix,
                                        MultiBufferSource.BufferSource maskBufferSource) {
        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();

        double x = lerpX - cameraPos.x;
        double y = lerpY - cameraPos.y;
        double z = lerpZ - cameraPos.z;

        poseStack.pushPose();
        poseStack.translate(x, y, z);

        try {
            float lerpYaw = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTick;

            dispatcher.render(
                    entity,
                    0, 0, 0,
                    lerpYaw,
                    partialTick,
                    poseStack,
                    maskBufferSource,
                    15728880
            );

        } catch (Exception e) {
        }

        poseStack.popPose();
    }
}