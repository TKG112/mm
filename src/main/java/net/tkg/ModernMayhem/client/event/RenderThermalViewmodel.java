package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.client.thermal.render.ThermalRenderer;

public final class RenderThermalViewmodel {

    private RenderThermalViewmodel() { }

    private static final float MIN_TRANSLATION = 0.35f;

    public static void stageFromRenderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext ctx,
                                           boolean leftHand, PoseStack poseStack, int light) {
        try {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null || entity != player) return;
            if (ctx == null || !ctx.firstPerson()) return;
            if (stack == null || stack.isEmpty()) return;
            if (poseStack == null) return;
            if (!ThermalRenderer.isMaskActiveForArm()) return;

            final PoseStack snapshot = new PoseStack();
            snapshot.last().pose().set(poseStack.last().pose());
            snapshot.last().normal().set(poseStack.last().normal());

            org.joml.Matrix4f mp = snapshot.last().pose();
            float tlen = (float) Math.sqrt(mp.m30() * mp.m30() + mp.m31() * mp.m31() + mp.m32() * mp.m32());
            if (tlen < MIN_TRANSLATION) return;

            final ItemStack fStack = stack.copy();
            final LocalPlayer fPlayer = player;
            final ItemDisplayContext fCtx = ctx;
            final boolean fLeft = leftHand;
            final int fLight = light;

            ThermalRenderer.stageViewmodelOccluder(() -> {
                Minecraft m = Minecraft.getInstance();
                MultiBufferSource.BufferSource buf = ThermalRenderer.getMaskBufferSource();
                if (buf == null) return;
                ItemRenderer ir = m.getItemRenderer();
                BakedModel model = ir.getModel(fStack, fPlayer.level(), fPlayer, fPlayer.getId());
                if (model == null || model.isCustomRenderer()) return;   // BEWLR items (incl. TaCZ guns)

                PoseStack ps = new PoseStack();
                ps.last().pose().set(snapshot.last().pose());
                ps.last().normal().set(snapshot.last().normal());
                model = model.applyTransform(fCtx, ps, fLeft);
                ps.translate(-0.5F, -0.5F, -0.5F);

                VertexConsumer vc = buf.getBuffer(RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS));
                PoseStack.Pose pose = ps.last();
                RandomSource random = RandomSource.create();
                for (Direction d : Direction.values()) {
                    random.setSeed(42L);
                    for (BakedQuad q : model.getQuads(null, d, random)) {
                        vc.putBulkData(pose, q, 1f, 1f, 1f, fLight, OverlayTexture.NO_OVERLAY);
                    }
                }
                random.setSeed(42L);
                for (BakedQuad q : model.getQuads(null, null, random)) {
                    vc.putBulkData(pose, q, 1f, 1f, 1f, fLight, OverlayTexture.NO_OVERLAY);
                }
            });
        } catch (Throwable t) {
        }
    }
}