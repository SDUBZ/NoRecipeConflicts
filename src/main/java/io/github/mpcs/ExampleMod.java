package io.github.mpcs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ExampleMod implements ModInitializer, ClientModInitializer {

	protected static Item SMALL_BACKPACK;
	public static final Identifier PACKET_COUNT = new Identifier("rc", "count");
	@Override
	public void onInitialize() {
		SMALL_BACKPACK = new Item(new Item.Settings().group(ItemGroup.TOOLS));
		ServerSidePacketRegistry.INSTANCE.register(PACKET_COUNT, ((packetContext, packetByteBuf) -> {
			System.out.println("PACKET ARRIVED SERVER DATA: " + packetByteBuf.readInt());
		}));


		Registry.register(Registry.ITEM, new Identifier("rc", "small_backpack"), SMALL_BACKPACK);
		System.out.println("Hello Fabric world!");
	}

	@Override
	public void onInitializeClient() {
		ClientSidePacketRegistry.INSTANCE.register(PACKET_COUNT, ((packetContext, packetByteBuf) -> {
			System.out.println("PACKET ARRIVED CLIENT DATA: " + packetByteBuf.readInt());
		}));

	}
}
