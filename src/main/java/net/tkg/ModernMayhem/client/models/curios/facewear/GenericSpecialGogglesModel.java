package net.tkg.ModernMayhem.client.models.curios.facewear;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import software.bernie.geckolib.model.GeoModel;

public class GenericSpecialGogglesModel<T extends GenericSpecialGogglesItem> extends GeoModel<T> {

    private ItemStack currentStack = ItemStack.EMPTY;

    public void setCurrentStack(ItemStack stack) {
        this.currentStack = stack;
    }

    private boolean hasCoti() {
        return !this.currentStack.isEmpty() && GenericSpecialGogglesItem.hasCoti(this.currentStack);
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return animatable.getGoggleModel(hasCoti());
    }

    public ResourceLocation getModelResource(T animatable, ItemStack stack) {
        return animatable.getGoggleModel(GenericSpecialGogglesItem.hasCoti(stack));
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return animatable.getGoggleTexture(hasCoti());
    }

    public ResourceLocation getTextureResource(T animatable, ItemStack stack) {
        return animatable.getGoggleTexture(GenericSpecialGogglesItem.hasCoti(stack));
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return animatable.getGoggleAnimation(hasCoti());
    }

}
