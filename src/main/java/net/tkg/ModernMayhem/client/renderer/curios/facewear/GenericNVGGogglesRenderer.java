package net.tkg.ModernMayhem.client.renderer.curios.facewear;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.client.models.curios.facewear.GenericNVGGogglesModel;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class GenericNVGGogglesRenderer<T extends GenericNVGGogglesItem> extends GeoArmorRenderer<T> implements ICurioRenderer {

    public GenericNVGGogglesRenderer() {
        super(new GenericNVGGogglesModel<>());
    }

    @Override
    public <L extends LivingEntity, M extends EntityModel<L>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack matrixStack,
            RenderLayerParent<L, M> renderLayerParent,
            MultiBufferSource renderTypeBuffer,
            int light, float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        this.prepForRender(slotContext.entity(), stack, EquipmentSlot.HEAD, (HumanoidModel<?>) renderLayerParent.getModel());

        VertexConsumer consumer = renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(this.getTextureLocation((T) stack.getItem())));

        this.renderToBuffer(matrixStack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void setupAnim(@NotNull Entity entity, float pLimbswing, float pLimbswingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    }

    public static class GenericNVGGogglesSlotRenderer<T extends GenericNVGGogglesItem> extends GeoItemRenderer<T> {
        public GenericNVGGogglesSlotRenderer() {
            super(new GenericNVGGogglesModel<>());
        }
    }
}