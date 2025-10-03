package net.tkg.ModernMayhem.client.renderer.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.client.models.armor.CustomArmorModel;
import net.tkg.ModernMayhem.client.models.curios.back.BackpackModels;
import net.tkg.ModernMayhem.server.item.armor.CustomArmorItem;
import net.tkg.ModernMayhem.server.item.curios.back.BackpackItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CustomArmorRenderer extends GeoArmorRenderer<CustomArmorItem> {
    public CustomArmorRenderer(EquipmentSlot slot) {
        super(new CustomArmorModel());
    }

    public static class CustomBackpackItemRenderer extends GeoItemRenderer<BackpackItem> {
        public CustomBackpackItemRenderer() {
            super(new BackpackModels());
        }
    }

    public static final ThreadLocal<Boolean> SLIM_CONTEXT = ThreadLocal.withInitial(() -> false);

    @Override
    public void prepForRender(@Nullable Entity entity, ItemStack stack, @Nullable EquipmentSlot slot, @Nullable HumanoidModel<?> baseModel) {
        // Set the slim status before rendering
        if (entity instanceof AbstractClientPlayer player) {
            SLIM_CONTEXT.set("slim".equals(player.getModelName()));
        } else {
            SLIM_CONTEXT.set(false);
        }

        // Call super to handle the rendering - don't remove the ThreadLocal yet
        super.prepForRender(entity, stack, slot, baseModel);
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
                               com.mojang.blaze3d.vertex.VertexConsumer buffer,
                               int packedLight,
                               int packedOverlay,
                               float red,
                               float green,
                               float blue,
                               float alpha) {
        // Render with the current context
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        // Clean up ThreadLocal after rendering is complete
        SLIM_CONTEXT.remove();
    }
}