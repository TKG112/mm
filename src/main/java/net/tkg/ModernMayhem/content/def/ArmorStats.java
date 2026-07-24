package net.tkg.ModernMayhem.content.def;

/**
 * Combat stats for a data-driven armor piece, parsed straight from the pack JSON.
 * JSON is authoritative in phase 1 (no per-item Forge config override).
 */
public record ArmorStats(double protection, double toughness, double knockback, int durability) {
    public static final ArmorStats EMPTY = new ArmorStats(0, 0, 0, 400);
}
