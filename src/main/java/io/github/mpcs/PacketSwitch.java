package io.github.mpcs;

import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.util.PacketByteBuf;

public class PacketSwitch implements Packet<ServerPlayPacketListener> {
    @Override
    public void read(PacketByteBuf packetByteBuf){
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        ((IServerPlayNetworkHandler)(serverPlayPacketListener)).onPacketSwitch(this);
    }
}
