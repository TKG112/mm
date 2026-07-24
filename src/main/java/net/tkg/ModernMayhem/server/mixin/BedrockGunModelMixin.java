package net.tkg.ModernMayhem.server.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.client.model.BedrockGunModel;
import com.tacz.guns.client.model.bedrock.BedrockPart;
import com.tacz.guns.client.model.functional.AttachmentRender;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.client.thermal.render.ThermalRenderer;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = BedrockGunModel.class, remap = false)
public abstract class BedrockGunModelMixin {

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/client/renderer/RenderType;II)V",
            at = @At("HEAD"),
            remap = false
    )
    private void mm$stageGunOccluder(PoseStack matrixStack, ItemStack gunItem, ItemDisplayContext transformType, RenderType renderType, int light, int overlay, CallbackInfo ci) {
        try {
            if (transformType == null || !transformType.firstPerson()) return;
            if (ThermalRenderer.isReRenderingViewmodel()) return;
            if (!ThermalRenderer.isMaskActiveForArm()) return;

            final BedrockGunModel self = (BedrockGunModel) (Object) this;

            final PoseStack snapshot = new PoseStack();
            snapshot.last().pose().set(matrixStack.last().pose());
            snapshot.last().normal().set(matrixStack.last().normal());

            final Map<BedrockPart, float[]> bones = new IdentityHashMap<>();
            ArrayDeque<BedrockPart> stack = new ArrayDeque<>(self.getShouldRender());
            while (!stack.isEmpty()) {
                BedrockPart p = stack.pop();
                Quaternionf q = p.additionalQuaternion;
                bones.put(p, new float[]{p.offsetX, p.offsetY, p.offsetZ, q.x(), q.y(), q.z(), q.w(), p.xScale, p.yScale, p.zScale});
                if (!p.children.isEmpty()) stack.addAll(p.children);
            }

            final ItemStack gun = gunItem;
            final ItemDisplayContext ctx = transformType;
            final RenderType rt = renderType;
            final int fLight = light, fOverlay = overlay;

            ThermalRenderer.stageViewmodelOccluder(() -> {
                for (Map.Entry<BedrockPart, float[]> e : bones.entrySet()) {
                    BedrockPart p = e.getKey();
                    float[] s = e.getValue();
                    p.offsetX = s[0]; p.offsetY = s[1]; p.offsetZ = s[2];
                    p.additionalQuaternion.set(s[3], s[4], s[5], s[6]);
                    p.xScale = s[7]; p.yScale = s[8]; p.zScale = s[9];
                }
                boolean prev = self.getRenderHand();
                self.setRenderHand(false);
                try {
                    self.render(snapshot, gun, ctx, rt, fLight, fOverlay);
                    ItemStack scope = self.getCurrentAttachmentItem().get(AttachmentType.SCOPE);
                    List<BedrockPart> scopePos = self.getScopePosPath();
                    if (scopePos != null && scope != null && !scope.isEmpty()) {
                        RenderSystem.colorMask(false, false, false, false);
                        RenderSystem.depthMask(true);
                        RenderSystem.enableDepthTest();
                        try {
                            snapshot.pushPose();
                            for (BedrockPart p : scopePos) p.translateAndRotateAndScale(snapshot);
                            AttachmentRender.renderAttachment(scope, self.getCurrentGunItem(), snapshot, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, fLight, fOverlay);
                            snapshot.popPose();
                        } finally {
                            RenderSystem.colorMask(true, true, true, true);
                        }
                    }
                } finally {
                    self.setRenderHand(prev);
                    self.cleanAnimationTransform();
                }
            });
        } catch (Throwable ignored) {
        }
    }
}