package net.tkg.ModernMayhem.server.registry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.network.*;
import net.tkg.ModernMayhem.server.util.PacketBase;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

/*
    * This class is an adapted version of the Create mod packets registry/system
 */
public enum PacketsRegistryMM {
    // Client to Server
    SWITCH_NVG_STATUS(SwitchNVGStatusPacket.class, SwitchNVGStatusPacket::new, PLAY_TO_SERVER),
    NVG_TUBE_GAIN_UP(NVGTubeGainUpPacket.class, NVGTubeGainUpPacket::new, PLAY_TO_SERVER),
    NVG_TUBE_GAIN_DOWN(NVGTubeGainDownPacket.class, NVGTubeGainDownPacket::new, PLAY_TO_SERVER),
    OPEN_BACK_BACKPACK_FROM_CONTROL(OpenBackpackKeyPacket.class, OpenBackpackKeyPacket::new, PLAY_TO_SERVER),
    OPEN_CHEST_BACKPACK_FROM_CONTROL(OpenRigKeyPacket.class, OpenRigKeyPacket::new, PLAY_TO_SERVER),;


    // Server to Client
    // There is no Server to Client packets at the moment

    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(ModernMayhemMod.ID, "main");
    private static SimpleChannel channel;
    public static final int NETWORK_VERSION = 1;
    public static final String NETWORK_VERSION_STRING = String.valueOf(NETWORK_VERSION);

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void init() {
        channel = NetworkRegistry.ChannelBuilder
                .named(CHANNEL_NAME)
                .networkProtocolVersion(() -> NETWORK_VERSION_STRING)
                .clientAcceptedVersions(NETWORK_VERSION_STRING::equals)
                .serverAcceptedVersions(NETWORK_VERSION_STRING::equals)
                .simpleChannel();

        for (PacketsRegistryMM packet : values()) {
            packet.packetType.register();
        }
    }

    private PacketType<?> packetType;

    <T extends PacketBase> PacketsRegistryMM(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
        packetType = new PacketType<>(type, factory, direction);
    }

    public static SimpleChannel getChannel() {
        return channel;
    }

    private static class PacketType<T extends PacketBase> {
        private static int index = 0;

        private BiConsumer<T, FriendlyByteBuf> encoder;
        private Function<FriendlyByteBuf, T> decoder;
        private BiConsumer<T, Supplier<NetworkEvent.Context>> handler;
        private Class<T> type;
        private NetworkDirection direction;

        private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            encoder = T::write;
            decoder = factory;
            handler = (packet, contextSupplier) -> {
                NetworkEvent.Context context = contextSupplier.get();
                if (packet.handle(context)) {
                    context.setPacketHandled(true);
                }
            };
            this.type = type;
            this.direction = direction;
        }

        private void register() {
            getChannel().messageBuilder(type, index++, direction)
                    .encoder(encoder)
                    .decoder(decoder)
                    .consumerNetworkThread(handler)
                    .add();
        }
    }
}
