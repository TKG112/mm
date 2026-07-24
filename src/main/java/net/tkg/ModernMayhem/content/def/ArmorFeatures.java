package net.tkg.ModernMayhem.content.def;

/**
 * Optional per-item capability flags declared inline in an armor definition's {@code "features"} block.
 * <p>
 * These are a <b>convenience that adds to</b>, never replaces, ModernMayhem's item tags
 * ({@code mm:has_head_mount}, {@code mm:has_visor_mount}). The tags remain the authoritative,
 * open mechanism so that helmets from <i>other mods</i> can still be made compatible via an ordinary
 * datapack. A capability counts as present if the tag says so <b>or</b> the definition does.
 */
public record ArmorFeatures(boolean headMount, boolean visorMount) {
    public static final ArmorFeatures NONE = new ArmorFeatures(false, false);
}
