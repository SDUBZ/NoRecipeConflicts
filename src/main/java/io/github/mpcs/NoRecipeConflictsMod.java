package io.github.mpcs;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NoRecipeConflictsMod implements ModInitializer, ClientModInitializer {

	protected static Item SMALL_BACKPACK;
	public static final Identifier PACKET_SWITCH = new Identifier("norecipeconflicts", "switch");
	public static final Identifier PACKET_DATA = new Identifier("norecipeconflicts", "data");

	@Override
	public void onInitialize() {
		SMALL_BACKPACK = new Item(new Item.Settings().group(ItemGroup.TOOLS));
        ServerSidePacketRegistry.INSTANCE.register(PACKET_SWITCH, (packetContext, packetByteBuf) -> {
			((ICraftingTableContainer)packetContext.getPlayer().container).move();
        });

		Registry.register(Registry.ITEM, new Identifier("norecipeconflicts", "small_backpack"), SMALL_BACKPACK);
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
