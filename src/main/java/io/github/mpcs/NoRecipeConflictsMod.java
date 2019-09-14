package io.github.mpcs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class NoRecipeConflictsMod implements ModInitializer, ClientModInitializer {

	public static final Identifier PACKET_SWITCH = new Identifier("norecipeconflicts", "switch");
	public static final Identifier PACKET_DATA = new Identifier("norecipeconflicts", "data");

	@Override
	public void onInitialize() {
        ServerSidePacketRegistry.INSTANCE.register(PACKET_SWITCH, (packetContext, packetByteBuf) -> {
			((ICraftingTableContainer)packetContext.getPlayer().container).move();
        });
	}

	@Override
	public void onInitializeClient() {
		ClientSidePacketRegistry.INSTANCE.register(PACKET_DATA, (packetContext, packetByteBuf) -> {
			int data = packetByteBuf.readInt();
			int indx = packetByteBuf.readInt();
			((ICraftingTableScreen)(((ICraftingTableContainer) MinecraftClient.getInstance().player.container).getScreen())).setCount(data);
			((ICraftingTableScreen)(((ICraftingTableContainer)MinecraftClient.getInstance().player.container).getScreen())).setIndx(indx);
		});
	}
}
