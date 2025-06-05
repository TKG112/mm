package net.tkg.ModernMayhem.server.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public abstract class PacketBase {

    public abstract void write(FriendlyByteBuf buffer);

    public abstract boolean handle(NetworkEvent.Context context);

    public static boolean isCtS(NetworkEvent.Context context) { // Is Client to Server
        return context.getDirection() == NetworkDirection.PLAY_TO_SERVER && context.getSender() != null;
    }

    public static boolean isStC(NetworkEvent.Context context) { // Is Server to Client
        return context.getDirection() == NetworkDirection.PLAY_TO_CLIENT && context.getSender() != null;
    }
}
