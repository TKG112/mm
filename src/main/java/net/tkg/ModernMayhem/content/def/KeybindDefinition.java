package net.tkg.ModernMayhem.content.def;

import net.minecraft.resources.ResourceLocation;

/**
 * A key binding declared by a content pack, which opens the storage of whatever curio the player has
 * equipped in {@link #opensSlot()}.
 * <p>
 * Lets a pack that invents its own Curios slot also give it a dedicated open key, rebindable in the
 * vanilla Controls screen like any other.
 * <p>
 * {@link #defaultKey()} is kept as the raw string (e.g. {@code key.keyboard.k}) rather than a resolved
 * key code, because resolving it needs client-only classes -- the client does that at registration.
 */
public record KeybindDefinition(ResourceLocation id, String opensSlot, String defaultKey) {

    /** Translation key for the Controls screen: {@code key.<namespace>.<name>}. */
    public String translationKey() {
        return "key." + id.getNamespace() + "." + id.getPath();
    }
}
