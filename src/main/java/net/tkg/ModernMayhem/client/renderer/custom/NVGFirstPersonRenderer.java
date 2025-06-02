package net.tkg.ModernMayhem.client.renderer.custom;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;
import net.tkg.ModernMayhem.client.models.custom.NVGFirstPersonModel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class NVGFirstPersonRenderer extends GeoItemRenderer<NVGFirstPersonFakeItem> {
    public NVGFirstPersonRenderer() {
        super(new NVGFirstPersonModel());
    }

    @Override
    public RenderType getRenderType(NVGFirstPersonFakeItem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    public void initCurrentItemStack(NVGFirstPersonFakeItem item) {
        // This method is used to initialize the current item stack for rendering
        this.currentItemStack = new ItemStack(item);
    }
}
