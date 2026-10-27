package com.chen1335.renderjs.API;

import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

@Info("Screen render event context. Provides mouse position, partial tick and the current screen.")
public interface IScreenRenderEvent extends IRenderJSEvent {
    GuiGraphicsExtractor getGuiGraphics();

    @Info("Returns the horizontal position of the mouse, in GUI pixels.")
    int getMouseX();

    @Info("Returns the vertical position of the mouse, in GUI pixels.")
    int getMouseY();

    @Info("Returns the partial tick of the current frame.")
    float getPartialTick();

    @Info("Returns the screen currently being rendered.")
    Screen getScreen();
}