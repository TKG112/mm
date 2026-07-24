package net.tkg.ModernMayhem.content.def;

import net.minecraft.resources.ResourceLocation;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem.GoggleType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A fully-parsed goggle definition from a content pack -- night vision, thermal or visor.
 * <p>
 * Goggles carry two independent sets of visuals: the {@link Visuals} seen on the player's head, and a
 * separate first-person set drawn right in front of the camera. Each set may also have a variant used
 * when a COTI is attached.
 */
public record GogglesDefinition(
        ResourceLocation id,
        GoggleType goggleType,
        Visuals worn,
        Visuals firstPerson,
        @Nullable ResourceLocation activationSound,
        @Nullable ResourceLocation deactivationSound,
        GoggleFeatures features,
        ArmorStats stats,
        int defaultGain,
        List<GainStep> gainSteps,
        List<PaletteDefinition> palettes,
        @Nullable ResourceLocation postChain,
        /** Extra descriptive lines shown in the item tooltip. */
        java.util.List<TooltipLine> tooltip
) {

    /**
     * One set of model/texture/animation, plus the optional COTI variant of each. A null COTI entry
     * falls back to the base one, so a goggle that doesn't change appearance with a COTI needs no
     * extra configuration.
     */
    public record Visuals(
            ResourceLocation model,
            ResourceLocation texture,
            ResourceLocation animation,
            @Nullable ResourceLocation cotiModel,
            @Nullable ResourceLocation cotiTexture,
            @Nullable ResourceLocation cotiAnimation
    ) {
        public ResourceLocation model(boolean hasCoti) {
            return hasCoti && cotiModel != null ? cotiModel : model;
        }

        public ResourceLocation texture(boolean hasCoti) {
            return hasCoti && cotiTexture != null ? cotiTexture : texture;
        }

        public ResourceLocation animation(boolean hasCoti) {
            return hasCoti && cotiAnimation != null ? cotiAnimation : animation;
        }
    }

    /** Capability flags; {@code rainbowPhosphor} drives the animated phosphor colour cycle. */
    public record GoggleFeatures(
            boolean canHoldCoti,
            boolean autoGain,
            boolean autoGating,
            boolean rainbowPhosphor
    ) {
        public static final GoggleFeatures NONE = new GoggleFeatures(false, false, false, false);
    }
}
