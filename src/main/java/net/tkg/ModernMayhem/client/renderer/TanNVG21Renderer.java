package net.tkg.ModernMayhem.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.models.TanNVG21Model;
import net.tkg.ModernMayhem.item.curios.facewear.BlackNVG21Item;
import net.tkg.ModernMayhem.item.curios.facewear.TanNVG21Item;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class TanNVG21Renderer extends GeoArmorRenderer<TanNVG21Item> implements ICurioRenderer {
	public TanNVG21Renderer() {
		super(new TanNVG21Model());
	}

	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing,
			float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		this.prepForRender(slotContext.entity(), stack, EquipmentSlot.HEAD, (HumanoidModel<?>) renderLayerParent.getModel());
		VertexConsumer consumer = renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(this.getTextureLocation((TanNVG21Item) stack.getItem())));
		this.renderToBuffer(matrixStack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setupAnim(@NotNull Entity entity, float pLimbswing, float pLimbswingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
	}
}
