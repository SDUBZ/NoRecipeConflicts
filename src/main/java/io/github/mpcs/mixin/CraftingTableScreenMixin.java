package io.github.mpcs.mixin;

import io.github.mpcs.ICraftingTableContainer;
import io.github.mpcs.ICraftingTableScreen;
import io.github.mpcs.NoRecipeConflictsMod;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.CraftingTableScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingTableScreen.class)
public abstract class CraftingTableScreenMixin extends AbstractContainerScreen  implements ICraftingTableScreen {

    private Identifier SLOT_TEXTURE = new Identifier("norecipeconflicts", "textures/gui/button.png");
    private TexturedButtonWidget toggleButtonWidget;
    private int conflictingItems = 0;
    private int indx;

    @Shadow
    private final RecipeBookWidget recipeBookGui  = new RecipeBookWidget();

    public CraftingTableScreenMixin(Container container_1, PlayerInventory playerInventory_1, Text text_1) {
        super(container_1, playerInventory_1, text_1);
    }

    @Inject(at = @At("HEAD"), method = "init", cancellable = true)
    private void init(CallbackInfo ci) {
        toggleButtonWidget = new TexturedButtonWidget(114,  59, 37, 17, 0, 0, 18, SLOT_TEXTURE, (buttonWidget_1) -> {
            ClientSidePacketRegistry.INSTANCE.sendToServer(NoRecipeConflictsMod.PACKET_SWITCH, new PacketByteBuf(Unpooled.buffer()));
        });
        if(this.playerInventory.player.world.isClient)
            ((ICraftingTableContainer)this.container).setScreen((CraftingTableScreen)(Object)this);

    }

    @Inject(at = @At("RETURN"), method = "drawForeground", cancellable = true)
    private void drawForeground(int int_1, int int_2, CallbackInfo ci) {
        if(conflictingItems > 1)
            toggleButtonWidget.render(int_1 - (this.width - this.containerWidth)/2 - (recipeBookGui.isOpen() ? 77 : 0), int_2 - (this.height - this.containerHeight)/2, 0);
        else
            toggleButtonWidget.render(115, 60, 0);
        TextRenderer textRenderer_1 = this.minecraft.getFontManager().getTextRenderer(MinecraftClient.DEFAULT_TEXT_RENDERER_ID);
        textRenderer_1.drawStringBounded((indx+(conflictingItems > 0 ? 1 : 0)) + "/" + conflictingItems, 129, 64, 26, 17);
    }

    @Inject(at = @At("HEAD"), method = "mouseClicked", cancellable = true)
    private void mouseClicked(double x, double y, int int_1, CallbackInfoReturnable<Boolean> cir) {
        if(conflictingItems > 1)
            toggleButtonWidget.mouseClicked(x - (this.width - this.containerWidth)/2 - (recipeBookGui.isOpen() ? 77 : 0), y - (this.height - this.containerHeight)/2, int_1);
    }

    public void setCount(int i) {
        conflictingItems = i;
    }
    public void setIndx(int i) {
        indx = i;
    }
}
