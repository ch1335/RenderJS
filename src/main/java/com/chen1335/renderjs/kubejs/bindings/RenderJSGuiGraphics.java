package com.chen1335.renderjs.kubejs.bindings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class RenderJSGuiGraphics extends GuiGraphics {
    public RenderJSGuiGraphics(Minecraft pMinecraft, MultiBufferSource.BufferSource pBufferSource) {
        super(pMinecraft, pBufferSource);
    }

    public void drawCenteredString(Font pFont, Component pText, int pX, int pY, int pColor) {
        FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
        this.drawString(pFont, formattedcharsequence, pX - pFont.width(formattedcharsequence) / 2, pY, pColor);
    }
}
