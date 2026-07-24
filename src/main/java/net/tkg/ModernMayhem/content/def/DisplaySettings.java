package net.tkg.ModernMayhem.content.def;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.EnumMap;
import java.util.Map;

/**
 * Per-item display framing, mirroring the vanilla model {@code display} block.
 * <p>
 * A data-driven armor icon reuses the full-body worn model (showing only the slot's bones), so the
 * visible group sits where it would on the body rather than centred. Each display context (inventory,
 * hands, item frame, ...) therefore needs its own framing. Defaults are provided per armor slot and
 * can be overridden per item from the pack JSON.
 * <p>
 * Deliberately free of client-only classes so it can be parsed on a dedicated server too;
 * conversion to {@code ItemTransforms} happens client-side.
 */
public final class DisplaySettings {

    /** Rotation in degrees, translation in model pixels (1px = 1/16 block), scale as a multiplier. */
    public record Entry(Vector3f rotation, Vector3f translation, Vector3f scale) {
        public static Entry of(float rotX, float rotY, float rotZ, float transX, float transY, float transZ, float scale) {
            return new Entry(new Vector3f(rotX, rotY, rotZ), new Vector3f(transX, transY, transZ), new Vector3f(scale, scale, scale));
        }

        public static final Entry IDENTITY = of(0, 0, 0, 0, 0, 0, 1f);
    }

    private final Map<ItemDisplayContext, Entry> entries;

    public DisplaySettings(Map<ItemDisplayContext, Entry> entries) {
        this.entries = new EnumMap<>(entries);
    }

    public static DisplaySettings empty() {
        return new DisplaySettings(new EnumMap<>(ItemDisplayContext.class));
    }

    public Entry get(ItemDisplayContext context) {
        return this.entries.getOrDefault(context, Entry.IDENTITY);
    }

    public Map<ItemDisplayContext, Entry> entries() {
        return this.entries;
    }

    /** Returns a copy of this with any entries from {@code overrides} replacing ours. */
    public DisplaySettings withOverrides(@Nullable DisplaySettings overrides) {
        if (overrides == null || overrides.entries.isEmpty()) {
            return this;
        }
        EnumMap<ItemDisplayContext, Entry> merged = new EnumMap<>(this.entries);
        merged.putAll(overrides.entries);
        return new DisplaySettings(merged);
    }

    /**
     * Baseline framing per armor slot, for a standard humanoid armor model.
     * <p>
     * All four slots are measured in-game against ModernMayhem's own armor models.
     * <p>
     * The values are listed per slot rather than derived from one another: measurement showed the
     * contexts don't share a single offset (third person shifts along Z, boots need an X nudge in
     * first person, and ground doesn't track the GUI value).
     */
    public static DisplaySettings defaultFor(ArmorItem.Type slot) {
        return switch (slot) {
            // Note: the helmet's rotations differ from the other slots by 180 degrees (gui, first
            // person) and 90 degrees (third person). That reflects the orientation of the helmet
            // model these were measured against, not something inherent to helmets -- a helmet
            // authored facing the same way as the body pieces will want the body rotations instead.
            case HELMET -> build(
                    Entry.of(30f, 150f, 0f, 0f, -21.75f, 0f, 0.9f),   // gui
                    Entry.of(0f, 0f, 0f, 0f, -25.25f, 0f, 0.9f),      // fixed (item frame)
                    Entry.of(0f, 0f, 0f, 0f, -13f, 0f, 0.5f),         // ground
                    Entry.of(90f, 0f, 0f, 0f, -2f, -16.25f, 0.5f),    // third person
                    Entry.of(0f, 155f, 0f, 0f, -20f, 0f, 0.8f));      // first person
            case CHESTPLATE -> build(
                    Entry.of(30f, -30f, 0f, 0f, -14f, 0f, 0.9f),
                    Entry.of(0f, 0f, 0f, 0f, -16f, 0f, 0.9f),
                    Entry.of(0f, 0f, 0f, 0f, -7.5f, 0f, 0.5f),
                    Entry.of(90f, 90f, 0f, 0f, -1.75f, -12f, 0.5f),
                    Entry.of(0f, -25f, 0f, 0f, -16f, 0f, 0.8f));
            case LEGGINGS -> build(
                    Entry.of(30f, -30f, 0f, 0f, -6f, 0f, 0.9f),
                    Entry.of(0f, 0f, 0f, 0f, -6.75f, 0f, 0.9f),
                    Entry.of(0f, 0f, 0f, 0f, -3f, 0f, 0.5f),
                    Entry.of(90f, 90f, 0f, 0f, -2f, -6f, 0.5f),
                    Entry.of(0f, -25f, 0f, 0f, -4f, 0f, 0.8f));
            case BOOTS -> build(
                    Entry.of(30f, -30f, 0f, 0f, -1.75f, 0f, 0.9f),
                    Entry.of(0f, 0f, 0f, 0f, -1.25f, 0f, 0.9f),
                    Entry.of(0f, 0f, 0f, 0f, -1.25f, 0f, 0.5f),
                    Entry.of(90f, 90f, 0f, 0f, -2f, -1.5f, 0.5f),
                    Entry.of(0f, -25f, 0f, -1f, 0.25f, 0f, 0.8f));
        };
    }

    /**
     * Baseline framing for curios (backpacks, rigs, cosmetics). Their models are authored around their
     * own origin rather than a humanoid skeleton, so this starts from a plain centred framing; authors
     * tune per item via the {@code display} block.
     */
    public static DisplaySettings defaultForCurio() {
        return build(
                Entry.of(30f, -30f, 0f, 0f, 0f, 0f, 0.9f),        // gui
                Entry.of(0f, 0f, 0f, 0f, 0f, 0f, 0.9f),           // fixed (item frame)
                Entry.of(0f, 0f, 0f, 0f, 0f, 0f, 0.5f),           // ground
                Entry.of(90f, 90f, 0f, 0f, -1.75f, -4f, 0.5f),    // third person
                Entry.of(0f, -25f, 0f, 0f, -4f, 0f, 0.8f));       // first person
    }

    /** Both hands share their context's entry; the head slot is left untransformed. */
    private static DisplaySettings build(Entry gui, Entry fixed, Entry ground, Entry thirdPerson, Entry firstPerson) {
        EnumMap<ItemDisplayContext, Entry> map = new EnumMap<>(ItemDisplayContext.class);
        map.put(ItemDisplayContext.GUI, gui);
        map.put(ItemDisplayContext.FIXED, fixed);
        map.put(ItemDisplayContext.GROUND, ground);
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, thirdPerson);
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, thirdPerson);
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, firstPerson);
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, firstPerson);
        map.put(ItemDisplayContext.HEAD, Entry.IDENTITY);
        return new DisplaySettings(map);
    }
}
