package net.tkg.ModernMayhem.content.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.content.item.DataArmorItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

/**
 * Shared worn-armor renderer for every data-driven armor piece.
 * <p>
 * Carries the slim/wide arm context the same way the hardcoded {@code CustomArmorRenderer} does, so
 * {@link DataArmorModel} can pick the slim model/texture when the wearer uses the slim player model.
 */
public class DataArmorRenderer extends GeoArmorRenderer<DataArmorItem> {
    public static final ThreadLocal<Boolean> SLIM_CONTEXT = ThreadLocal.withInitial(() -> false);

    public DataArmorRenderer() {
        super(new DataArmorModel());
    }

    @Override
    public void prepForRender(@Nullable Entity entity, ItemStack stack, @Nullable EquipmentSlot slot, @Nullable HumanoidModel<?> baseModel) {
        if (entity instanceof AbstractClientPlayer player) {
            SLIM_CONTEXT.set("slim".equals(player.getModelName()));
        } else {
            SLIM_CONTEXT.set(false);
        }
        super.prepForRender(entity, stack, slot, baseModel);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        SLIM_CONTEXT.remove();
    }
}
