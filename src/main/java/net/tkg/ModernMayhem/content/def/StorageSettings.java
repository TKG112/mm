package net.tkg.ModernMayhem.content.def;

/**
 * Inventory configuration for a data-driven curio (backpack, rig, ...).
 * <p>
 * A curio with no storage block simply has none -- {@link #NONE} yields a zero-size inventory, which
 * ModernMayhem treats as "not a container" (no GUI opens).
 */
public record StorageSettings(int rows, int columns, boolean suppliesAmmo) {
    public static final StorageSettings NONE = new StorageSettings(0, 0, false);

    public int size() {
        return rows * columns;
    }
}
