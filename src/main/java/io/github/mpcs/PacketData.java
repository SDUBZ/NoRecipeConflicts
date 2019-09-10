package io.github.mpcs;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.ContainerProvider;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;

public class PacketData implements Packet<ClientPlayPacketListener> {

    public int data;

    public PacketData(int mess) {
        data = mess;
    }

    @Override
    public void read(PacketByteBuf packetByteBuf) throws IOException {
        data = packetByteBuf.readInt();
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) throws IOException {
        packetByteBuf.writeInt(data);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        System.out.println("aa " + data);
        ((ICraftingTableScreen)(((ICraftingTableContainer)MinecraftClient.getInstance().player.container).getScreen())).setCount(data);
    }
}
