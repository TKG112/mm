package net.tkg.ModernMayhem.client.models.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.armor.CustomArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class CustomArmorModel extends GeoModel<CustomArmorItem> {
    @Override
    public ResourceLocation getModelResource(CustomArmorItem customArmorItem) {
        switch (customArmorItem.getMaterial().getName()) { // Using a switch in case we add more armor types in the future
            case "kevlar" -> {

                 switch (customArmorItem.getType()) {
                    case HELMET -> {
                        return switch (customArmorItem.getVariant()) {
                            case 0, 2 -> new ResourceLocation(ModernMayhemMod.ID, "geo/armor/combat_helmet.geo.json");
                            case 1 -> new ResourceLocation(ModernMayhemMod.ID, "geo/armor/ssh68_helmet.geo.json");
                            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
                        };
                    }
                     default -> {
                         return new ResourceLocation(ModernMayhemMod.ID, "geo/armor/kevlar_clothing.geo.json");
                     }
                 }
            }

            case "nothing" -> {

                switch (customArmorItem.getType()) {
                    case HELMET -> {
                        return switch (customArmorItem.getVariant()) {
                            case 0 -> new ResourceLocation(ModernMayhemMod.ID, "geo/armor/head_mount.geo.json");
                            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
                        };
                    }
                    default -> {
                        return new ResourceLocation(ModernMayhemMod.ID, "geo/armor/kevlar_clothing.geo.json");
                    }
                }
            }

            case "ronin" -> {

                switch (customArmorItem.getType()) {
                    case HELMET -> {
                        return switch (customArmorItem.getVariant()) {
                            case 0 -> new ResourceLocation(ModernMayhemMod.ID, "geo/armor/ronin.geo.json");
                            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
                        };
                    }
                    default -> {
                        return new ResourceLocation(ModernMayhemMod.ID, "geo/armor/kevlar_clothing.geo.json");
                    }
                }
            }

            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
        }
    }

    @Override
    public ResourceLocation getTextureResource(CustomArmorItem customArmorItem) {
        switch (customArmorItem.getMaterial().getName()) { // Using a switch in case we add more armor types in the future
            case "kevlar" -> {
                switch (customArmorItem.getVariant()) {
                    case 0 -> {
                        if (customArmorItem.getType() == ArmorItem.Type.HELMET) {
                            return new ResourceLocation(ModernMayhemMod.ID, "textures/armor/black_combat_helmet.png");
                        }
                        return new ResourceLocation(ModernMayhemMod.ID, "textures/armor/black_kevlar_clothing.png");
                    }
                    case 1 -> {
                        if (customArmorItem.getType() == ArmorItem.Type.HELMET) {
                            return new ResourceLocation(ModernMayhemMod.ID, "textures/armor/green_ssh68_helmet.png");
                        }
                        return new ResourceLocation(ModernMayhemMod.ID, "textures/armor/green_kevlar_clothing.png");
                    }
                    case 2 -> {
                        if (customArmorItem.getType() == ArmorItem.Type.HELMET) {
                            return new ResourceLocation(ModernMayhemMod.ID, "textures/armor/tan_combat_helmet.png");
                        }
                        return new ResourceLocation(ModernMayhemMod.ID, "textures/armor/tan_kevlar_clothing.png");
                    }
                    default -> throw new IllegalStateException("Unexpected value: No such variant with id " + customArmorItem.getVariant());
                }
            }

            case "nothing" -> {
                switch (customArmorItem.getVariant()) {
                    case 0 -> {
                        if (customArmorItem.getType() == ArmorItem.Type.HELMET) {
                            return new ResourceLocation(ModernMayhemMod.ID, "textures/armor/black_head_mount.png");
                        }
                        return new ResourceLocation(ModernMayhemMod.ID, "textures/armor/black_kevlar_clothing.png"); //No armor, but gonna keep this
                    }
                    default -> throw new IllegalStateException("Unexpected value: No such variant with id " + customArmorItem.getVariant());
                }
            }

            case "ronin" -> {
                switch (customArmorItem.getVariant()) {
                    case 0 -> {
                        if (customArmorItem.getType() == ArmorItem.Type.HELMET) {
                            return new ResourceLocation(ModernMayhemMod.ID, "textures/armor/black_ronin.png");
                        }
                        return new ResourceLocation(ModernMayhemMod.ID, "textures/armor/black_kevlar_clothing.png"); //No armor, but gonna keep this
                    }
                    default -> throw new IllegalStateException("Unexpected value: No such variant with id " + customArmorItem.getVariant());
                }
            }

            default -> throw new IllegalStateException("Unexpected value: no such armor type as " + customArmorItem.getMaterial().getName());
        }
    }

    @Override
    public ResourceLocation getAnimationResource(CustomArmorItem customArmorItem) {
        return new ResourceLocation(ModernMayhemMod.ID, "animation/empty.animation.json");
    }
}
