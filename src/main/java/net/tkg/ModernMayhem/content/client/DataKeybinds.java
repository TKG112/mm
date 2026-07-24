package net.tkg.ModernMayhem.content.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.tkg.ModernMayhem.content.DataDrivenContent;
import net.tkg.ModernMayhem.content.KnownCurioSlots;
import net.tkg.ModernMayhem.content.def.KeybindDefinition;
import net.tkg.ModernMayhem.server.network.OpenCurioKeyPacket;
import net.tkg.ModernMayhem.server.registry.KeyMappingRegistryMM;
import net.tkg.ModernMayhem.server.registry.PacketsRegistryMM;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns a content pack's keybind definitions into real, rebindable {@link KeyMapping}s.
 * <p>
 * Registration happens at {@link RegisterKeyMappingsEvent}, which fires after packs are scanned during
 * mod construction -- so pack-declared keys can be registered like any built-in one, and appear in the
 * vanilla Controls screen under ModernMayhem's category.
 */
public final class DataKeybinds {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final List<KeyMapping> REGISTERED = new ArrayList<>();

    private DataKeybinds() {}

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        for (KeybindDefinition def : DataDrivenContent.keybindDefinitions()) {
            KeyMapping mapping = build(def);
            if (mapping != null) {
                event.register(mapping);
                REGISTERED.add(mapping);
                LOGGER.info("[MM] Registered data-driven keybind '{}' (opens slot: {})", def.id(), def.opensSlot());
            }
        }
    }

    /**
     * Whether pressing this key should say anything to the server at all.
     * <p>
     * The login handshake is authoritative when we have it: a key from a content pack the server
     * doesn't have simply does nothing, instead of sending packets the server would discard. If the
     * handshake hasn't arrived we fall back to the synced Curios inventory rather than blocking
     * everything, so a delayed packet never breaks a legitimate key.
     */
    private static boolean canOpen(Player player, String slot) {
        if (!KnownCurioSlots.unknown() && !KnownCurioSlots.serverHas(slot)) {
            return false;
        }
        return CuriosUtil.hasSlot(player, slot) && CuriosUtil.hasCurioInSlot(player, slot);
    }

    /** Drop the cached server slots so one session's data never leaks into the next. */
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        KnownCurioSlots.clear();
    }

    private static KeyMapping build(KeybindDefinition def) {
        int keyCode;
        try {
            keyCode = InputConstants.getKey(def.defaultKey()).getValue();
        } catch (Exception e) {
            LOGGER.error("[MM] Keybind '{}' has an unusable default_key '{}' -- defaulting to unbound",
                    def.id(), def.defaultKey());
            keyCode = InputConstants.UNKNOWN.getValue();
        }

        String slot = def.opensSlot();
        return new KeyMapping(def.translationKey(), keyCode, KeyMappingRegistryMM.CATEGORY) {
            private boolean wasDown = false;

            @Override
            public void setDown(boolean isDown) {
                super.setDown(isDown);
                if (wasDown != isDown && isDown) {
                    Player player = Minecraft.getInstance().player;
                    if (player != null && canOpen(player, slot)) {
                        PacketsRegistryMM.getChannel().sendToServer(new OpenCurioKeyPacket(slot));
                    }
                }
                wasDown = isDown;
            }
        };
    }
}
