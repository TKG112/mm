package net.tkg.ModernMayhem.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public abstract class PacketBase {

    public abstract void write(FriendlyByteBuf buffer);

    public abstract boolean handle(NetworkEvent.Context context);

}
