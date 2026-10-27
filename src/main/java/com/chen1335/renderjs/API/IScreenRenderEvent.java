package com.chen1335.renderjs.API;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.joml.Matrix3x2fStack;

@Info("Screen render event context. Provides mouse position, partial tick and the current screen.")
public interface IScreenRenderEvent extends IRenderJSEvent {
    @HideFromJS
    ScreenEvent.Render getEvent();

    default Matrix3x2fStack getPoseStack() {
        return getEvent().getGuiGraphics().pose();
    }

    @Info("Returns the horizontal position of the mouse, in GUI pixels.")
    default int getMouseX() {
        return getEvent().getMouseX();
    }

    @Info("Returns the vertical position of the mouse, in GUI pixels.")
    default int getMouseY() {
        return getEvent().getMouseY();
    }

    @Info("Returns the partial tick of the current frame.")
    default float getPartialTick() {
        return getEvent().getPartialTick();
    }

    @Info("Returns the screen currently being rendered.")
    default Screen getScreen() {
        return getEvent().getScreen();
    }
}
