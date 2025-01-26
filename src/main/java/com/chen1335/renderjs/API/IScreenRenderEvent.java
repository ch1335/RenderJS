package com.chen1335.renderjs.API;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;

public interface IScreenRenderEvent extends IRenderJSEvent{
    @HideFromJS
    ScreenEvent.Render getEvent();


    default PoseStack getPoseStack() {
        return getEvent().getPoseStack();
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
