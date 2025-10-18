package net.tkg.ModernMayhem.client.models.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;
import net.tkg.ModernMayhem.client.models.curios.facewear.NVGGogglesModel;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.item.curios.facewear.VisorItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class NVGFirstPersonModel extends GeoModel<NVGFirstPersonFakeItem> {

    private Minecraft mc = Minecraft.getInstance();

    @Override
    public ResourceLocation getModelResource(NVGFirstPersonFakeItem animatable) {
        return switch (getType()) {
            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/gpnvgs_fpm.geo.json");
            case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/pvs14_fpm.geo.json");
            case 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/pvs7_fpm.geo.json");
            case 3 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/visor_fpm.geo.json");
            default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
        };
    }

    @Override
    public ResourceLocation getTextureResource(NVGFirstPersonFakeItem animatable) {
        // Would have been duplicated code if we didn't use the NVGGogglesModel
        return getTextureResource(getType(), getVariant());
    }

    public static ResourceLocation getTextureResource(int type, int variant) {
        switch (type) {
            case 3 -> {
                return switch (variant) {
                    case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_visor_transparent.png");
                    case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_visor_transparent.png");
                    default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                };
            }
            default -> NVGGogglesModel.getTextureResource(type, variant);
        }
        return NVGGogglesModel.getTextureResource(type, variant);
    }

    @Override
    public ResourceLocation getAnimationResource(NVGFirstPersonFakeItem animatable) {
        return switch (getType()) {
            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/gpnvg_fpa.animation.json");
            case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/pvs14_fpa.animation.json");
            case 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/pvs7_fpa.animation.json");
            case 3 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/visor_fpa.animation.json");
            default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
        };
    }

    // This method only gets the type of the player's NVG goggles (0 for GPNVG, 1 for PVS-14, 2 for PVS-7)
    private int getType() {
        ItemStack facewear = CuriosUtil.getFaceWearItem(mc.player);
        if (facewear.getItem() instanceof NVGGogglesItem nvgGogglesItem) {
            return nvgGogglesItem.getConfig().getType();
        } else if (facewear.getItem() instanceof VisorItem visorItem) {
            return visorItem.getConfig().getType();
        }
        return -1; // No NVG goggles equipped
    }

    // This method only gets the variant of the player's NVG goggles (0 for black, 1 for tan, 2 for green, 99 for ultra gamer)
    private int getVariant() {
        ItemStack facewear = CuriosUtil.getFaceWearItem(mc.player);
        if (facewear.getItem() instanceof NVGGogglesItem nvgGogglesItem) {
            return nvgGogglesItem.getConfig().getVariant();
        } else if (facewear.getItem() instanceof VisorItem visorItem) {
            return visorItem.getConfig().getVariant();
        }
        return -1; // No NVG goggles equipped
    }
}
