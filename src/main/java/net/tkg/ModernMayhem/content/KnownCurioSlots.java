package net.tkg.ModernMayhem.content;

import java.util.Set;

/**
 * Client-side cache of the Curios slots the connected server actually has, populated by the login
 * handshake ({@code SyncCurioSlotsPacket}).
 * <p>
 * Lets pack-declared keybinds stay quiet on a server that doesn't have the pack, instead of sending
 * packets it would only discard.
 * <p>
 * Deliberately holds no client-only types so it is safe to touch from shared packet code.
 */
public final class KnownCurioSlots {
    private static volatile Set<String> slots = Set.of();

    private KnownCurioSlots() {}

    public static void set(Set<String> serverSlots) {
        slots = Set.copyOf(serverSlots);
    }

    /** Called on disconnect so one server's slots never leak into the next session. */
    public static void clear() {
        slots = Set.of();
    }

    /**
     * True when the handshake hasn't arrived (or carried nothing). Callers should fall back to their
     * other guards rather than blocking everything -- this keeps things working if the packet is
     * delayed or a server predates the handshake.
     */
    public static boolean unknown() {
        return slots.isEmpty();
    }

    public static boolean serverHas(String slot) {
        return slots.contains(slot);
    }
}
