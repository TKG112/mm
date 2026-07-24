package net.tkg.ModernMayhem.server.network;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.content.KnownCurioSlots;
import net.tkg.ModernMayhem.server.util.PacketBase;

import java.util.HashSet;
import java.util.Set;

public class SyncCurioSlotsPacket extends PacketBase {
    private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();

    private static final int MAX_SLOT_LENGTH = 64;
    private static final int MAX_SLOTS = 256;

    private final Set<String> slots;

    public SyncCurioSlotsPacket(Set<String> slots) {
        this.slots = slots;
    }

    public SyncCurioSlotsPacket(FriendlyByteBuf buffer) {
        int count = Math.min(buffer.readVarInt(), MAX_SLOTS);
        Set<String> read = new HashSet<>();
        for (int i = 0; i < count; i++) {
            read.add(buffer.readUtf(MAX_SLOT_LENGTH));
        }
        this.slots = read;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        int count = Math.min(this.slots.size(), MAX_SLOTS);
        buffer.writeVarInt(count);
        int written = 0;
        for (String slot : this.slots) {
            if (written++ >= count) break;
            buffer.writeUtf(slot, MAX_SLOT_LENGTH);
        }
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        KnownCurioSlots.set(this.slots);
        LOGGER.info("[MM] Server supports {} curio slot(s): {}", this.slots.size(), this.slots);
        return true;
    }
}
