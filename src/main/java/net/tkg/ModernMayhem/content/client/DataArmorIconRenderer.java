package net.tkg.ModernMayhem.content.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.tkg.ModernMayhem.content.item.DataArmorItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * Inventory / in-hand (BEWLR) renderer for data-driven armor. Reuses {@link DataArmorModel}, so the
 * icon is the item's own GeckoLib model -- no separate icon model required.
 * <p>
 * A GeckoLib armor {@code .geo.json} contains the whole humanoid (head, body, arms, legs, boots).
 * When worn, {@code GeoArmorRenderer} hides the bones that don't belong to the equipped slot; the item
 * renderer has no such logic and would otherwise draw the entire suit as the icon. We reproduce the
 * same slot -> bone visibility here so a chestplate shows only the chest, a helmet only the head, etc.
 * This relies on the standard GeckoLib armor bone names, which the model must already use to render
 * when worn -- so if it works equipped, it works as an icon.
 */
public class DataArmorIconRenderer extends GeoItemRenderer<DataArmorItem> {
    private static final String HEAD = "armorHead";
    private static final String BODY = "armorBody";
    private static final String RIGHT_ARM = "armorRightArm";
    private static final String LEFT_ARM = "armorLeftArm";
    private static final String RIGHT_LEG = "armorRightLeg";
    private static final String LEFT_LEG = "armorLeftLeg";
    private static final String RIGHT_BOOT = "armorRightBoot";
    private static final String LEFT_BOOT = "armorLeftBoot";

    private static final String[] ALL_ARMOR_BONES = { HEAD, BODY, RIGHT_ARM, LEFT_ARM, RIGHT_LEG, LEFT_LEG, RIGHT_BOOT, LEFT_BOOT };

    public DataArmorIconRenderer() {
        super(new DataArmorModel());
    }

    @Override
    public void preRender(PoseStack poseStack, DataArmorItem animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                          float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        applyIconVisibility(animatable, model);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender,
                partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    /** Show only the bones belonging to this piece's armor slot; hide the rest of the humanoid. */
    private static void applyIconVisibility(DataArmorItem item, BakedGeoModel model) {
        setHidden(model, true, ALL_ARMOR_BONES);
        switch (item.getType()) {
            case HELMET -> setHidden(model, false, HEAD);
            case CHESTPLATE -> setHidden(model, false, BODY, RIGHT_ARM, LEFT_ARM);
            case LEGGINGS -> setHidden(model, false, RIGHT_LEG, LEFT_LEG);
            case BOOTS -> setHidden(model, false, RIGHT_BOOT, LEFT_BOOT);
        }
    }

    private static void setHidden(BakedGeoModel model, boolean hidden, String... boneNames) {
        for (String name : boneNames) {
            model.getBone(name).ifPresent(bone -> bone.setHidden(hidden));
        }
    }
}
