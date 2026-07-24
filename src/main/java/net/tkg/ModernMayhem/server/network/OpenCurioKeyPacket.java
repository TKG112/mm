package net.tkg.ModernMayhem.server.network;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tkg.ModernMayhem.server.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.PacketBase;

public class OpenCurioKeyPacket extends PacketBase {
    private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();

    private final String slot;

    private static final int MAX_SLOT_LENGTH = 64;

    public OpenCurioKeyPacket(String slot) {
        this.slot = slot;
    }

    public OpenCurioKeyPacket(FriendlyByteBuf buffer) {
        this.slot = buffer.readUtf(MAX_SLOT_LENGTH);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.slot, MAX_SLOT_LENGTH);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        Player player = context.getSender();
        if (player == null) {
            return true;
        }
        if (!CuriosUtil.hasSlot(player, this.slot)) {
            LOGGER.debug("[MM] {} asked to open unknown curio slot '{}' -- ignoring",
                    player.getGameProfile().getName(), this.slot);
            return true;
        }
        ItemStack curio = CuriosUtil.getCurioInSlot(player, this.slot);
        if (curio.getItem() instanceof GenericBackpackItem backpack) {
            backpack.OpenGUIFromCuriosInventory(player, curio);
        }
        return true;
    }
}
