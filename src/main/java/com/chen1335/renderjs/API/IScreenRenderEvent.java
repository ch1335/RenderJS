package com.chen1335.renderjs.API;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.joml.Matrix3x2fStack;

public interface IScreenRenderEvent extends IRenderJSEvent {
    @HideFromJS
    ScreenEvent.Render getEvent();

    @HideFromJS
    default Matrix3x2fStack getPoseStack() {
        return getEvent().getGuiGraphics().pose();
    }

    default int getMouseX() {
        return getEvent().getMouseX();
    }

    default int getMouseY() {
        return getEvent().getMouseY();
    }

    default float getPartialTick() {
        return getEvent().getPartialTick();
    }

    default Screen getScreen() {
        return getEvent().getScreen();
    }
}
