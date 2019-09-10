package io.github.mpcs.mixin;

import io.github.mpcs.ICraftingTableContainer;
import io.github.mpcs.ICraftingTableScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.CraftingTableScreen;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.container.Container;
import net.minecraft.container.CraftingTableContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingTableScreen.class)
public abstract class CraftingTableScreenMixin extends AbstractContainerScreen  implements ICraftingTableScreen {

    private Identifier SLOT_TEXTURE = new Identifier("rc", "textures/gui/button.png");
    private ToggleButtonWidget toggleButtonWidget;
    private int conflictingItems = 0;
    private int counter = 0;
    public CraftingTableScreenMixin(Container container_1, PlayerInventory playerInventory_1, Text text_1) {
        super(container_1, playerInventory_1, text_1);
    }

    @Inject(at = @At("HEAD"), method = "init", cancellable = true)
    private void init(CallbackInfo ci) {
        toggleButtonWidget = new ToggleButtonWidget((this.width-this.containerWidth)/2 +119, (this.height-this.containerHeight)/2 +59, 26, 17, false);
        toggleButtonWidget.setTextureUV(0, 0, 27, 18, SLOT_TEXTURE);
        if(this.playerInventory.player.world.isClient)
            ((ICraftingTableContainer)this.container).setScreen((CraftingTableScreen)(Object)this);
    }

    @Inject(at = @At("RETURN"), method = "render", cancellable = true)
    private void render(int int_1, int int_2, float float_1, CallbackInfo ci) {
        toggleButtonWidget.render(int_1, int_2, float_1);
        TextRenderer textRenderer_1 = this.minecraft.getFontManager().getTextRenderer(MinecraftClient.DEFAULT_TEXT_RENDERER_ID);
        //conflictingItems = ((ICraftingTableContainer)((CraftingTableContainer)this.getContainer())).getSize();
        textRenderer_1.drawStringBounded(Integer.toString(conflictingItems), (this.width-this.containerWidth)/2 + 134, (this.height-this.containerHeight)/2 +64, 26, 17);
    }

    @Inject(at = @At("HEAD"), method = "mouseClicked", cancellable = true)
    private void mouseClicked(double x, double y, int int_1, CallbackInfoReturnable<Boolean> cir) {
        if(x > toggleButtonWidget.x && x < toggleButtonWidget.x + 26)
            if(y > toggleButtonWidget.y && y < toggleButtonWidget.y + 17) {
                System.out.println("Guzik KLIK");
                System.out.println("LUST SIZE: " + ((ICraftingTableContainer)this.container).getSize());
                counter++;
                if(counter > 20)
                    counter = 0;
                ((ICraftingTableContainer)this.container).setIndex(counter);
            }
    }

    public void setCount(int i) {
        conflictingItems = i;
    }
}
