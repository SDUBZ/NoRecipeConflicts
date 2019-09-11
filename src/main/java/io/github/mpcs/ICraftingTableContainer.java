package io.github.mpcs;

import net.minecraft.client.gui.screen.ingame.CraftingTableScreen;

public interface ICraftingTableContainer {
    int getSize();
    void setIndex(int i);
    void move();
    void setScreen(CraftingTableScreen cts);
    CraftingTableScreen getScreen();
}
