package net.tkg.ModernMayhem.server.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tacz.guns.client.model.functional.RightHandRender;
import net.minecraft.world.item.ItemDisplayContext;
import net.tkg.ModernMayhem.client.event.RenderNVGFirstPerson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RightHandRender.class)
public class RightHandRenderMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelRightHandRender(PoseStack poseStack, VertexConsumer vertexBuffer, ItemDisplayContext transformType, int light, int overlay, CallbackInfo ci) {
        if (!RenderNVGFirstPerson.shouldRenderRightArm) {
            ci.cancel();
        }
    }
}
