package net.tkg.ModernMayhem.content.def;

import java.util.List;

/**
 * A thermal palette: how heat maps to colour, plus how the world behind it is tinted.
 * <p>
 * This is the single description of a palette. It used to be split in two -- the heat gradient was
 * hardcoded in {@code thermal_composite.fsh} while the world tint lived in a parallel array in
 * {@code ThermalRenderer} -- which meant adding a palette required editing both.
 * <p>
 * Deliberately free of client-only types so it can be parsed on a dedicated server.
 *
 * @param name       shown when cycling palettes
 * @param worldColor tint applied to the world behind the thermal image
 * @param inverted   inverts the world (as "Black Hot" does)
 * @param base       colour at heat 0
 * @param stops      ordered blend targets; each blends the accumulated colour across
 *                   {@code [previous stop, at]}
 * @param linear     blend without smoothstep easing -- a straight ramp, as the greyscale palettes use
 */
public record PaletteDefinition(
        String name,
        float[] worldColor,
        boolean inverted,
        float[] base,
        List<Stop> stops,
        boolean linear
) {
    public record Stop(float at, float r, float g, float b) {}

    /**
     * Size of ModernMayhem's built-in palette set, used to wrap palette cycling.
     * <p>
     * Declared here rather than read from {@code ThermalPalettes.BUILTIN} because cycling is handled
     * on the server, where that client-only class doesn't exist. {@code ThermalPalettes} asserts the
     * two agree at startup.
     */
    public static final int BUILTIN_COUNT = 8;
}
