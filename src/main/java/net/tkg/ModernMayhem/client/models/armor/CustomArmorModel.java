package net.tkg.ModernMayhem.client.models.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.renderer.armor.CustomArmorRenderer;
import net.tkg.ModernMayhem.server.item.armor.CustomArmorItem;
import software.bernie.geckolib.model.GeoModel;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class CustomArmorModel extends GeoModel<CustomArmorItem> {

    @Override
    public ResourceLocation getModelResource(CustomArmorItem customArmorItem) {
        // Check slim status at render time, not construction time
        boolean isSlim = CustomArmorRenderer.SLIM_CONTEXT.get();

        switch (customArmorItem.getMaterial().getName()) {
            case "kevlar" -> {
                switch (customArmorItem.getType()) {
                    case HELMET -> {
                        return switch (customArmorItem.getVariant()) {
                            case 0, 2, 6 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/armor/combat_helmet.geo.json");
                            case 1 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/armor/ssh68_helmet.geo.json");
                            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
                        };
                    }
                    case LEGGINGS -> {
                        return switch (customArmorItem.getVariant()) {
                            case 0, 1, 2 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/armor/kevlar_clothing.geo.json");
                            case 3, 4, 5 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/armor/iola.geo.json");
                            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
                        };
                    }
                    default -> {
                        if (isSlim) {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "geo/armor/kevlar_clothing_thin.geo.json");
                        } else {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "geo/armor/kevlar_clothing.geo.json");
                        }
                    }
                }
            }

            case "nothing" -> {
                switch (customArmorItem.getType()) {
                    case HELMET -> {
                        return switch (customArmorItem.getVariant()) {
                            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/armor/head_mount.geo.json");
                            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
                        };
                    }
                    default -> {
                        throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
                    }
                }
            }

            case "ronin" -> {
                switch (customArmorItem.getType()) {
                    case HELMET -> {
                        return switch (customArmorItem.getVariant()) {
                            case 0 -> fromNamespaceAndPath(ModernMayhemMod.ID, "geo/armor/ronin.geo.json");
                            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
                        };
                    }
                    default -> {
                        throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
                    }
                }
            }

            case "hazmat" -> {
                return fromNamespaceAndPath(ModernMayhemMod.ID, "geo/armor/hazmat_suit.geo.json");
            }

            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
        }
    }

    @Override
    public ResourceLocation getTextureResource(CustomArmorItem customArmorItem) {
        boolean isSlim = CustomArmorRenderer.SLIM_CONTEXT.get();

        switch (customArmorItem.getMaterial().getName()) {
            case "kevlar" -> {
                switch (customArmorItem.getVariant()) {
                    case 0 -> {
                        if (customArmorItem.getType() == ArmorItem.Type.HELMET) {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/black_combat_helmet.png");
                        }
                        if (isSlim) {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/black_kevlar_clothing_thin.png");
                        } else {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/black_kevlar_clothing.png");
                        }
                    }
                    case 1 -> {
                        if (customArmorItem.getType() == ArmorItem.Type.HELMET) {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/green_ssh68_helmet.png");
                        }
                        if (isSlim) {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/green_kevlar_clothing_thin.png");
                        } else {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/green_kevlar_clothing.png");
                        }
                    }
                    case 2 -> {
                        if (customArmorItem.getType() == ArmorItem.Type.HELMET) {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/tan_combat_helmet.png");
                        }
                        if (isSlim) {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/tan_kevlar_clothing_thin.png");
                        } else {
                            return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/tan_kevlar_clothing.png");
                        }
                    }
                    case 3 -> {
                        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/black_iola.png");
                    }
                    case 4 -> {
                        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/green_iola.png");
                    }
                    case 5 -> {
                        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/tan_iola.png");
                    }
                    case 6 -> {
                        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/c1300_helmet.png");
                    }
                    default -> throw new IllegalStateException("Unexpected value: No such variant with id " + customArmorItem.getVariant());
                }
            }

            case "nothing" -> {
                switch (customArmorItem.getVariant()) {
                    case 0 -> {
                        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/black_head_mount.png");
                    }
                    default -> throw new IllegalStateException("Unexpected value: No such variant with id " + customArmorItem.getVariant());
                }
            }

            case "ronin" -> {
                switch (customArmorItem.getVariant()) {
                    case 0 -> {
                        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/black_ronin.png");
                    }
                    default -> throw new IllegalStateException("Unexpected value: No such variant with id " + customArmorItem.getVariant());
                }
            }

            case "hazmat" -> {
                switch (customArmorItem.getVariant()) {
                    case 0 -> {
                        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/yellow_hazmat_suit.png");
                    }
                    case 1 -> {
                        return fromNamespaceAndPath(ModernMayhemMod.ID, "textures/armor/orange_hazmat_suit.png");
                    }
                    default -> throw new IllegalStateException("Unexpected value: No such variant with id " + customArmorItem.getVariant());
                }
            }

            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
        }
    }

    @Override
    public ResourceLocation getAnimationResource(CustomArmorItem customArmorItem) {
        return fromNamespaceAndPath(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}