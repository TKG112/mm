package net.tkg.ModernMayhem.content.def;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A fully-parsed curio definition from a content pack -- backpacks, chest rigs, and purely
 * cosmetic curios all share this shape.
 * <p>
 * The {@link #id()} is derived from the definition file's path
 * ({@code data/<namespace>/modernmayhem/curio/<name>.json} -> {@code <namespace>:<name>}).
 * <p>
 * {@link #slot()} is the Curios slot identifier ({@code back}, {@code body}, ...). ModernMayhem
 * generates the required {@code curios:<slot>} item tag entry automatically, so authors don't have to
 * hand-write a tag file for their own items.
 * <p>
 * {@link #attachesTo()} is a <em>rendering</em> concern, distinct from the Curios slot: it selects
 * which set of GeckoLib armor bones is drawn, and therefore which part of the player the model rides
 * along with. A belt in a custom {@code belt} slot still attaches to {@link EquipmentSlot#CHEST}
 * (torso bones), while a thigh holster would attach to {@link EquipmentSlot#LEGS}. Defaults are
 * derived from the Curios slot and can be overridden per item.
 */
public record CurioDefinition(
        ResourceLocation id,
        String slot,
        EquipmentSlot attachesTo,
        ResourceLocation model,
        ResourceLocation texture,
        ResourceLocation animation,
        @Nullable ResourceLocation icon,
        StorageSettings storage,
        ArmorStats stats,
        /** Extra, non-durability-scaled attribute modifiers granted while equipped. */
        List<AttributeBonus> attributes,
        /**
         * Item tag of helmets this curio refuses to be worn with. A gas mask can't seal against a
         * full-face helmet, for example. When {@code null} the curio has no such restriction.
         */
        @Nullable ResourceLocation conflictsWithHelmets,
        /**
         * Tag of mob effects the wearer is immune to while this is equipped -- a gas mask filtering
         * out airborne nasties. Null means it grants no immunity.
         */
        @Nullable ResourceLocation immuneToEffects,
        /**
         * Whether partially-transparent texels are blended rather than rounded to fully solid or
         * fully invisible. Wanted by tinted lenses and other see-through gear; costlier than the
         * default, so it stays opt-in.
         */
        boolean transparent,
        /** Extra descriptive lines shown in the item tooltip. */
        java.util.List<TooltipLine> tooltip
) {
    /** True when this curio holds items and should open an inventory GUI. */
    public boolean hasStorage() {
        return storage.size() > 0;
    }
}
