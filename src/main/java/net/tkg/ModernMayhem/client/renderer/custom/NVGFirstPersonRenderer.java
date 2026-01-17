package net.tkg.ModernMayhem.client.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;
import net.tkg.ModernMayhem.client.models.custom.NVGFirstPersonModel;
import net.tkg.ModernMayhem.server.util.AnimUtils;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class NVGFirstPersonRenderer extends GeoItemRenderer<NVGFirstPersonFakeItem> {
    public NVGFirstPersonRenderer() {
        super(new NVGFirstPersonModel());
    }

    private static final float SCALE_RECIPROCAL = 1.0f / 16.0f;
    protected boolean renderArms = false;
    protected MultiBufferSource currentBuffer;
    protected RenderType renderType;

    @Override
    public RenderType getRenderType(NVGFirstPersonFakeItem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucentCull(getTextureLocation(animatable));
    }

    public void initCurrentItemStack(NVGFirstPersonFakeItem item) {
        // This method is used to initialize the current item stack for rendering
        this.currentItemStack = new ItemStack(item);
    }

    @Override
    public void actuallyRender(
            PoseStack poseStack,
            NVGFirstPersonFakeItem animatable,
            BakedGeoModel model,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        LocalPlayer player = Minecraft.getInstance().player;
        this.currentBuffer = bufferSource;
        this.renderType = renderType;
        super.actuallyRender(
                poseStack,
                animatable,
                model,
                renderType,
                bufferSource,
                buffer,
                isReRender,
                partialTick,
                packedLight,
                packedOverlay,
                red, green, blue, alpha
        );
        if (!this.renderArms) return;
        this.renderArms = false;
    }

    @Override
    public void renderRecursively(
            PoseStack poseStack,
            NVGFirstPersonFakeItem animatable,
            GeoBone bone,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            String boneName = bone.getName();
            boolean renderingArms = false;
            if (boneName.equals("left_arm") || boneName.equals("right_arm")) {
                bone.setHidden(true);
                renderingArms = true;
            }
            if (renderingArms) {
                float armsAlpha = player.isInvisible() ? 0.0F : 1.0F;
                PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(player);
                PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
                poseStack.pushPose();
                RenderUtils.translateMatrixToBone(poseStack, bone);
                RenderUtils.translateToPivotPoint(poseStack, bone);
                RenderUtils.rotateMatrixAroundBone(poseStack, bone);
                RenderUtils.scaleMatrixForBone(poseStack, bone);
                RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
                poseStack.translate(0f, -0.8f, 0f);
                ResourceLocation playerSkin = player.getSkinTextureLocation();
                VertexConsumer armBuilder = this.currentBuffer.getBuffer(RenderType.entitySolid(playerSkin));
                VertexConsumer sleeveBuilder = this.currentBuffer.getBuffer(RenderType.entityTranslucentCull(playerSkin));
                if (boneName.equals("left_arm")) {
                    poseStack.translate(-SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
                    AnimUtils.renderPartOverBone(model.leftArm, bone, poseStack, armBuilder, packedLight, OverlayTexture.NO_OVERLAY, armsAlpha);
                    AnimUtils.renderPartOverBone(model.leftSleeve, bone, poseStack, sleeveBuilder, packedLight, OverlayTexture.NO_OVERLAY, armsAlpha);
                } else {
                    poseStack.translate(SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
                    AnimUtils.renderPartOverBone(model.rightArm, bone, poseStack, armBuilder, packedLight, OverlayTexture.NO_OVERLAY, armsAlpha);
                    AnimUtils.renderPartOverBone(model.rightSleeve, bone, poseStack, sleeveBuilder, packedLight, OverlayTexture.NO_OVERLAY, armsAlpha);
                }
                this.currentBuffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(this.animatable)));
                poseStack.popPose();
            }
        }
        super.renderRecursively(
                poseStack,
                animatable,
                bone,
                renderType,
                bufferSource,
                buffer,
                isReRender,
                partialTick,
                packedLight,
                packedOverlay,
                red, green, blue, alpha
        );
    }
}
