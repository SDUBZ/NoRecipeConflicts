package io.github.mpcs.mixin;

import io.github.mpcs.ICraftingTableContainer;
import io.github.mpcs.IServerPlayNetworkHandler;
import io.github.mpcs.PacketSwitch;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin implements IServerPlayNetworkHandler {

    @Shadow
    public ServerPlayerEntity player;

    @Override
    public void onPacketSwitch(PacketSwitch packetSwitch) {
        ((ICraftingTableContainer)player.container).move();
        System.out.println(player.getDisplayName());
    }
}
