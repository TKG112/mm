package net.tkg.ModernMayhem.content;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;
import net.tkg.ModernMayhem.server.network.SyncCurioSlotsPacket;
import net.tkg.ModernMayhem.server.registry.PacketsRegistryMM;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashSet;
import java.util.Set;

/**
 * Sends the Curios-slot handshake to each joining player.
 * <p>
 * Content packs are scanned locally on both sides, so a client can hold a pack the server doesn't.
 * Telling the client which slots actually exist server-side lets pack-declared keybinds stay quiet
 * instead of sending packets that would only be discarded.
 */
public final class CurioSlotSync {

    private CurioSlotSync() {}

    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        Set<String> slots = new HashSet<>();
        CuriosApi.getCuriosInventory(player).ifPresent(inventory -> slots.addAll(inventory.getCurios().keySet()));
        if (slots.isEmpty()) {
            return;
        }
        PacketsRegistryMM.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new SyncCurioSlotsPacket(slots));
    }
}
