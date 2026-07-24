package net.tkg.ModernMayhem.content.def;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.Nullable;

/**
 * A fully-parsed armor definition from a content pack.
 * <p>
 * The {@link #id()} is derived from the definition file's path
 * ({@code data/<namespace>/modernmayhem/armor/<name>.json} -> {@code <namespace>:<name>}),
 * which guarantees the item namespace matches the pack's {@code assets/<namespace>/} folder.
 */
public record ArmorDefinition(
        ResourceLocation id,
        ArmorItem.Type slot,
        String material,
        ResourceLocation model,
        ResourceLocation texture,
        ResourceLocation animation,
        @Nullable ResourceLocation slimModel,
        @Nullable ResourceLocation slimTexture,
        @Nullable ResourceLocation icon,
        ArmorStats stats,
        ArmorFeatures features,
        /** Full-screen texture drawn while this is worn on the head in first person, e.g. a visor HUD. */
        @Nullable ResourceLocation screenOverlay,
        /** Extra descriptive lines shown in the item tooltip. */
        java.util.List<TooltipLine> tooltip,
        /** Mob effects granted while this piece is worn. */
        java.util.List<WornEffect> effects,
        /**
         * Whether to hide the player skin's outer layer ("second layer") under this piece.
         * <p>
         * Lets a model sit flush against the body instead of being inflated to clear the overlay,
         * which is what otherwise causes z-fighting between the two.
         */
        boolean hidesSkinOverlay
) {
}
