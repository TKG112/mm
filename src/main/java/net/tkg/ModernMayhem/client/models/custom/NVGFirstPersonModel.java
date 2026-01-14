package net.tkg.ModernMayhem.client.models.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;
import net.tkg.ModernMayhem.client.models.curios.facewear.GenericNVGGogglesModel;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.item.curios.facewear.TVGGogglesItem;
import net.tkg.ModernMayhem.server.item.curios.facewear.VisorItem;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class NVGFirstPersonModel extends GeoModel<NVGFirstPersonFakeItem> {

    private final Minecraft mc = Minecraft.getInstance();

    @Override
    public ResourceLocation getModelResource(NVGFirstPersonFakeItem animatable) {
        // 1. Get the actual item from the player
        ItemStack stack = getStack();
        boolean hasCoti = GenericNVGGogglesItem.hasCoti(stack);

        return switch (getType()) {
            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/gpnvg_fpm.geo.json");
            case 1 -> {
                if (hasCoti) {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/pvs14_coti_fpm.geo.json");
                }
                yield fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/pvs14_fpm.geo.json");
            }
            case 2 -> {
                if (hasCoti) {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/pvs7_coti_fpm.geo.json");
                }
                yield fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/pvs7_fpm.geo.json");
            }
            case 3 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/visor_fpm.geo.json");
            case 4 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/fpm/facewear/tvg_fpm.geo.json");
            default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
        };
    }

    @Override
    public ResourceLocation getTextureResource(NVGFirstPersonFakeItem animatable) {
        int type = getType();
        int variant = getVariant();

        ItemStack stack = getStack();
        boolean hasCoti = GenericNVGGogglesItem.hasCoti(stack);

        if (type == 3) {
            return switch (variant) {
                case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_visor_transparent.png");
                case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_visor_transparent.png");
                default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
            };
        }

        return GenericNVGGogglesModel.getTextureResourceWithCoti(type, variant, hasCoti);
    }

    @Override
    public ResourceLocation getAnimationResource(NVGFirstPersonFakeItem animatable) {
        ItemStack stack = getStack();
        boolean hasCoti = GenericNVGGogglesItem.hasCoti(stack);

        return switch (getType()) {
            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/gpnvg_fpa.animation.json");
            case 1 -> {
                if (hasCoti) {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/pvs14_coti_fpa.animation.json");
                }
                yield fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/pvs14_fpa.animation.json");
            }
            case 2 -> {
                if (hasCoti) {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/pvs7_coti_fpa.animation.json");
                }
                yield fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/pvs7_fpa.animation.json");
            }
            case 3 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/visor_fpa.animation.json");
            case 4 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/fpa/facewear/tvg_fpa.animation.json");
            default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
        };
    }

    private ItemStack getStack() {
        if (mc.player == null) return ItemStack.EMPTY;
        return CuriosUtil.getFaceWearItem(mc.player);
    }

    private int getType() {
        ItemStack facewear = getStack();
        if (facewear.getItem() instanceof NVGGogglesItem nvgGogglesItem) {
            return nvgGogglesItem.getConfig().getType();
        } else if (facewear.getItem() instanceof VisorItem visorItem) {
            return visorItem.getConfig().getType();
        } else if (facewear.getItem() instanceof TVGGogglesItem tvgGogglesItem) {
            return tvgGogglesItem.getConfig().getType();
        }
        return -1;
    }

    private int getVariant() {
        ItemStack facewear = getStack();
        if (facewear.getItem() instanceof NVGGogglesItem nvgGogglesItem) {
            return nvgGogglesItem.getConfig().getVariant();
        } else if (facewear.getItem() instanceof VisorItem visorItem) {
            return visorItem.getConfig().getVariant();
        } else if (facewear.getItem() instanceof TVGGogglesItem tvgGogglesItem) {
            return tvgGogglesItem.getConfig().getVariant();
        }
        return -1;
    }
}