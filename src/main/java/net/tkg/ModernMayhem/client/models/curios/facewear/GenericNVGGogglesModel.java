package net.tkg.ModernMayhem.client.models.curios.facewear;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class GenericNVGGogglesModel<T extends GenericSpecialGogglesItem> extends GeoModel<T> {

    private ItemStack currentStack = ItemStack.EMPTY;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        boolean hasCoti = !currentStack.isEmpty() && GenericSpecialGogglesItem.hasCoti(currentStack);

        return getModelResourceWithCoti(animatable.getConfig().getType(), hasCoti);
    }

    public ResourceLocation getModelResource(T animatable, ItemStack stack) {
        boolean hasCoti = GenericSpecialGogglesItem.hasCoti(stack);
        return getModelResourceWithCoti(animatable.getConfig().getType(), hasCoti);
    }

    private ResourceLocation getModelResourceWithCoti(int type, boolean hasCoti) {
        return switch (type) {
            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/gpnvg.geo.json");
            case 1 -> {
                if (hasCoti) {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/pvs14_coti.geo.json");
                } else {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/pvs14.geo.json");
                }
            }
            case 2 -> {
                if (hasCoti) {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/pvs7_coti.geo.json");
                } else {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/pvs7.geo.json");
                }
            }
            case 3 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/visor.geo.json");
            case 4 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/item/curios/facewear/tvg.geo.json");
            default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
        };
    }

    public void setCurrentStack(ItemStack stack) {
        this.currentStack = stack;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        boolean hasCoti = !this.currentStack.isEmpty() && GenericSpecialGogglesItem.hasCoti(this.currentStack);

        return getTextureResourceWithCoti(animatable.getConfig().getType(), animatable.getConfig().getVariant(), hasCoti);
    }

    public ResourceLocation getTextureResource(T animatable, ItemStack stack) {
        int type = animatable.getConfig().getType();
        int variant = animatable.getConfig().getVariant();
        boolean hasCoti = GenericSpecialGogglesItem.hasCoti(stack);

        return getTextureResourceWithCoti(type, variant, hasCoti);
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        boolean hasCoti = !currentStack.isEmpty() && GenericSpecialGogglesItem.hasCoti(currentStack);

        return getAnimationResourceWithCoti(animatable.getConfig().getType(), hasCoti);
    }

    private ResourceLocation getAnimationResourceWithCoti(int type, boolean hasCoti) {
        return switch (type) {
            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/gpnvg.animation.json");
            case 1 -> {
                if (hasCoti) {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/pvs14_coti.animation.json");
                } else {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/pvs14.animation.json");
                }
            }
            case 2 -> {
                if (hasCoti) {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/pvs7_coti.animation.json");
                } else {
                    yield fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/pvs7.animation.json");
                }
            }
            case 3 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/visor.animation.json");
            case 4 -> fromNamespaceAndPath(ModernMayhemMod.ID, "animations/item/curios/facewear/tvg.animation.json");
            default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
        };
    }

    public static ResourceLocation getTextureResource(int type, int variant) {
        return getTextureResourceWithCoti(type, variant, false);
    }

    public static ResourceLocation getTextureResourceWithCoti(int type, int variant, boolean hasCoti) {
        switch (type) {
            case 0 -> {
                return switch (variant) {
                    case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_gpnvg.png");
                    case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_gpnvg.png");
                    case 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/green_gpnvg.png");
                    case 99 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/ultra_gamer_gpnvg.png");
                    default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                };
            }
            case 1 -> {
                if (hasCoti) {
                    return switch (variant) {
                        case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_pvs14_coti.png");
                        case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_pvs14_coti.png");
                        case 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/green_pvs14_coti.png");
                        default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                    };
                } else {
                    return switch (variant) {
                        case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_pvs14.png");
                        case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_pvs14.png");
                        case 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/green_pvs14.png");
                        default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                    };
                }
            }
            case 2 -> {
                if (hasCoti) {
                    return switch (variant) {
                        case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_pvs7_coti.png");
                        default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                    };
                } else {
                    return switch (variant) {
                        case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_pvs7.png");
                        default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                    };
                }
            }
            case 3 -> {
                return switch (variant) {
                    case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_visor.png");
                    case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/tan_visor.png");
                    default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                };
            }
            case 4 -> {
                return switch (variant) {
                    case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "textures/item/curios/facewear/black_tvg.png");
                    default -> fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
                };
            }
        }
        return fromNamespaceAndPath(ModernMayhemMod.ID, "NOT_FOUND");
    }
}