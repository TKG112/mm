package net.tkg.ModernMayhem.client.models.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class NVGFirstPersonModel extends GeoModel<NVGFirstPersonFakeItem> {

    // Path must be lowercase [a-z0-9/._-]: an uppercase sentinel throws at class-load, crashing the
    // whole mod during construction. Only ever a placeholder anyway -- the fake item is only rendered
    // while goggles are equipped, so this is never actually resolved to a resource.
    private static final ResourceLocation NOT_FOUND = fromNamespaceAndPath(ModernMayhemMod.ID, "not_found");

    private final Minecraft mc = Minecraft.getInstance();

    @Override
    public ResourceLocation getModelResource(NVGFirstPersonFakeItem animatable) {
        GenericSpecialGogglesItem goggles = getEquippedGoggles();
        return goggles == null ? NOT_FOUND : goggles.getFirstPersonModel(hasCoti());
    }

    @Override
    public ResourceLocation getTextureResource(NVGFirstPersonFakeItem animatable) {
        GenericSpecialGogglesItem goggles = getEquippedGoggles();
        return goggles == null ? NOT_FOUND : goggles.getFirstPersonTexture(hasCoti());
    }

    @Override
    public ResourceLocation getAnimationResource(NVGFirstPersonFakeItem animatable) {
        GenericSpecialGogglesItem goggles = getEquippedGoggles();
        return goggles == null ? NOT_FOUND : goggles.getFirstPersonAnimation(hasCoti());
    }

    private ItemStack getStack() {
        if (mc.player == null) return ItemStack.EMPTY;
        return CuriosUtil.getFaceWearItem(mc.player);
    }

    private boolean hasCoti() {
        return GenericSpecialGogglesItem.hasCoti(getStack());
    }

    @Nullable
    private GenericSpecialGogglesItem getEquippedGoggles() {
        ItemStack facewear = getStack();
        return facewear.getItem() instanceof GenericSpecialGogglesItem goggles ? goggles : null;
    }
}
