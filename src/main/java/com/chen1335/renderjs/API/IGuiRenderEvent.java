package com.chen1335.renderjs.API;

import com.mojang.blaze3d.platform.Window;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

@Info("HUD render event context. Provides window information and partial tick for HUD callbacks.")
public interface IGuiRenderEvent extends IRenderJSEvent {
    GuiGraphicsExtractor getGuiGraphics();

    @Info("Returns the game window.")
    default Window getWindow() {
        return Minecraft.getInstance().getWindow();
    }

    @Info("Returns the partial tick of the current frame.")
    float getPartialTick();
}