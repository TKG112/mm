package net.tkg.ModernMayhem.content.def;

import net.minecraft.resources.ResourceLocation;

/**
 * A mob effect applied to the wearer while a piece is equipped.
 * <p>
 * The effect is looked up by id when it is applied rather than at parse time: the registry isn't
 * populated while content packs are being scanned, and an id belonging to a mod that isn't installed
 * should be skipped quietly rather than break the whole pack.
 *
 * @param effect         effect id, e.g. {@code minecraft:night_vision}
 * @param amplifier      0 = level I, 1 = level II, ...
 * @param requiresFullSet when true the effect only applies while every armor slot is filled with a
 *                        piece of the same {@code material} -- the "wear the whole ghillie suit"
 *                        case. When false the single piece is enough.
 * @param showParticles  whether the effect's ambient particles are shown
 * @param showIcon       whether the effect appears in the inventory HUD
 */
public record WornEffect(
        ResourceLocation effect,
        int amplifier,
        boolean requiresFullSet,
        boolean showParticles,
        boolean showIcon
) {
}
