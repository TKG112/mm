package net.tkg.ModernMayhem.content.def;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * An arbitrary attribute modifier granted by a curio while it is equipped.
 * <p>
 * The armour-ish stats (protection, toughness, knockback) live in {@link ArmorStats} because they
 * scale with durability. This is the escape hatch for everything else -- movement speed, attack
 * damage, ModernMayhem's own {@code mm:safe_fall_distance} that the kneepads grant, or an attribute
 * added by some other mod entirely. These are applied flat, without durability scaling, because an
 * item granting them need not be damageable at all.
 * <p>
 * The attribute is looked up by id when the modifiers are requested rather than at parse time: the
 * attribute registry isn't populated yet while content packs are being scanned, and an id belonging
 * to a mod that isn't installed should be skipped quietly rather than break the whole pack.
 */
public record AttributeBonus(
        ResourceLocation attribute,
        double amount,
        AttributeModifier.Operation operation,
        /** Stable name for the modifier; also what shows up in the item tooltip. */
        String name
) {
}
