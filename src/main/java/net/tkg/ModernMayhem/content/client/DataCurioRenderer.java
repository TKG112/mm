package net.tkg.ModernMayhem.content.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
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
import net.tkg.ModernMayhem.content.item.DataCurioItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

/**
 * Worn renderer for every data-driven curio, following the same pattern as ModernMayhem's hardcoded
 * curio renderers: a {@link GeoArmorRenderer} that also implements {@link ICurioRenderer} so Curios
 * can draw it on the player.
 */
public class DataCurioRenderer extends GeoArmorRenderer<DataCurioItem> implements ICurioRenderer {

    public DataCurioRenderer() {
        super(new DataCurioModel());
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource renderTypeBuffer,
            int light,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        DataCurioItem item = (DataCurioItem) stack.getItem();
        EquipmentSlot attachesTo = item.definition().attachesTo();

        this.prepForRender(slotContext.entity(), stack, attachesTo, (HumanoidModel<?>) renderLayerParent.getModel());

        if (item.definition().transparent()) {
            flushPendingGeometry(renderTypeBuffer);
        }

        VertexConsumer consumer = renderTypeBuffer.getBuffer(
                renderTypeFor(item, this.getTextureLocation(item)));
        this.renderToBuffer(matrixStack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public RenderType getRenderType(DataCurioItem animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return renderTypeFor(animatable, texture);
    }

    /**
     * Draws everything queued so far, so this curio lands on top of the wearer rather than under them.
     * <p>
     * Minecraft batches geometry by render type and only draws it when the batch is flushed, so
     * submission order is not draw order. A curio submitted during the player's render layers is
     * flushed <em>before</em> the player's own body geometry, which is invisible for opaque gear but
     * wrecks a translucent lens: the head is drawn afterwards and paints straight over it, leaving
     * only the parts of the lens that hang off the head's silhouette.
     * <p>
     * Flushing here puts the wearer on screen first, so the lens then blends over a head that is
     * already there. Only done for translucent curios -- it costs a batch break, and nothing else
     * needs it.
     */
    private static void flushPendingGeometry(MultiBufferSource bufferSource) {
        if (bufferSource instanceof MultiBufferSource.BufferSource batched) {
            batched.endBatch();
        }
        MultiBufferSource.BufferSource global =
                Minecraft.getInstance().renderBuffers().bufferSource();
        if (global != bufferSource) {
            global.endBatch();
        }
    }

    /**
     * How the model is blended, for both the worn model and the inventory icon.
     * <p>
     * Translucent gear uses vanilla's plain translucent type, which keeps back-face culling off and
     * depth writes on. Both matter here. GeckoLib cubes aren't reliably wound for culling -- which is
     * why every GeckoLib armor render type is a {@code NoCull} one -- and gear models are commonly a
     * closed box around the body part with the detail painted on in an otherwise transparent texture.
     * Such a box can only sort its own front and back faces by writing depth.
     * <p>
     * Depth writing is what made the wearer's head disappear, but the cause there was draw order, not
     * the depth mask; see {@link #flushPendingGeometry}.
     */
    private static RenderType renderTypeFor(DataCurioItem item, ResourceLocation texture) {
        return item.definition().transparent()
                ? RenderType.entityTranslucent(texture)
                : RenderType.armorCutoutNoCull(texture);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    /** Inventory / in-hand (BEWLR) renderer for data-driven curios. */
    public static class Icon extends GeoItemRenderer<DataCurioItem> {
        public Icon() {
            super(new DataCurioModel());
        }

        @Override
        public RenderType getRenderType(DataCurioItem animatable, ResourceLocation texture,
                                        MultiBufferSource bufferSource, float partialTick) {
            return renderTypeFor(animatable, texture);
        }
    }
}
