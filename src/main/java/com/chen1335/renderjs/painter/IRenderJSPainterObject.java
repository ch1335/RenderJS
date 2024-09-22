package com.chen1335.renderjs.painter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

public interface IRenderJSPainterObject {
    void draw(GuiGraphics guiGraphics);

    RenderJSPainter.Layer getLayer();
    RenderJSPainter.Step getStep();

    boolean visible();
}
