package io.github.mpcs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;

public class PacketData implements Packet<ClientPlayPacketListener> {

    public int data, indx;

    public PacketData(int mess, int index) {
        data = mess;
        indx = index;
    }

    @Override
    public void read(PacketByteBuf packetByteBuf) throws IOException {
        data = packetByteBuf.readInt();
        indx = packetByteBuf.readInt();
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) throws IOException {
        packetByteBuf.writeInt(data);
        packetByteBuf.writeInt(indx);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        System.out.println("aa " + data);
        ((ICraftingTableScreen)(((ICraftingTableContainer)MinecraftClient.getInstance().player.container).getScreen())).setCount(data);
        ((ICraftingTableScreen)(((ICraftingTableContainer)MinecraftClient.getInstance().player.container).getScreen())).setIndx(indx);
    }
}
