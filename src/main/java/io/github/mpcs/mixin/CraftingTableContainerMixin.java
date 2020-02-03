package io.github.mpcs.mixin;

import io.github.mpcs.ICraftingTableContainer;
import io.github.mpcs.NoRecipeConflictsMod;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.gui.screen.ingame.CraftingTableScreen;
import net.minecraft.container.ContainerType;
import net.minecraft.container.CraftingContainer;
import net.minecraft.container.CraftingTableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ContainerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(CraftingTableContainer.class)
public abstract class CraftingTableContainerMixin extends CraftingContainer implements ICraftingTableContainer {

	private int index = 0;
	private int sizz = 0;
	private CraftingTableScreen craftingTableScreen;
	@Shadow
	private CraftingResultInventory resultInv;
	@Shadow
	private PlayerEntity player;
	@Shadow
	private CraftingInventory craftingInv;

	public CraftingTableContainerMixin(ContainerType containerType_1, int int_1) {
		super(containerType_1, int_1);
	}

	@Override
	public void onContentChanged(Inventory inventory_1) {
		newUpdateResult(this.syncId, this.player.getEntityWorld(), this.player, this.craftingInv, this.resultInv);
	}


	private void newUpdateResult(int int_1, World world_1, PlayerEntity playerEntity_1, CraftingInventory craftingInventory_1, CraftingResultInventory craftingResultInventory_1) {
		if (!world_1.isClient) {
			ServerPlayerEntity serverPlayerEntity_1 = (ServerPlayerEntity)playerEntity_1;
			ItemStack itemStack_1 = ItemStack.EMPTY;
			List<CraftingRecipe> items = world_1.getServer().getRecipeManager().getAllMatches(RecipeType.CRAFTING, craftingInventory_1, world_1);
			this.sizz = items.size();
			if (items.size() > 0) {
				CraftingRecipe craftingRecipe_1 = (CraftingRecipe)items.get(index);
				if (craftingResultInventory_1.shouldCraftRecipe(world_1, serverPlayerEntity_1, craftingRecipe_1)) {
					itemStack_1 = craftingRecipe_1.craft(craftingInventory_1);
				}
			}
			craftingResultInventory_1.setInvStack(0, itemStack_1);
			serverPlayerEntity_1.networkHandler.sendPacket(new ContainerSlotUpdateS2CPacket(int_1, 0, itemStack_1));
            PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
            byteBuf.writeInt(sizz);
            byteBuf.writeInt(index);
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(serverPlayerEntity_1, NoRecipeConflictsMod.PACKET_DATA, byteBuf);
		}
	}

	public void move() {
		index++;
		if(index >= sizz)
			index = 0;
		newUpdateResult(this.syncId, this.player.getEntityWorld(), this.player, this.craftingInv, this.resultInv);
	}

	public void setScreen(CraftingTableScreen cts) {
		craftingTableScreen = cts;
	}

	public CraftingTableScreen getScreen() {
		return craftingTableScreen;
	}

}
