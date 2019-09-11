package io.github.mpcs;

import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;

public class PacketSwitch implements Packet<ServerPlayPacketListener> {

    String UUID;

    public PacketSwitch(String uuid) {
        UUID = uuid;
    }

    @Override
    public void read(PacketByteBuf packetByteBuf) throws IOException {
        UUID = packetByteBuf.readString();
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) throws IOException {
        packetByteBuf.writeString(UUID);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        System.out.println("A1");
        ((IServerPlayNetworkHandler)((ServerPlayNetworkHandler)serverPlayPacketListener)).onPacketSwitch(this);
        //serverPlayPacketListener.onPacketSwitch(this);
        //PlayerStream.world())
        System.out.println("A2");
    }
}
