package com.chen1335.renderjs.painter;


import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.client.painter.PainterObjectProperties;
import dev.latvian.mods.kubejs.client.painter.screen.BoxObject;
import dev.latvian.mods.kubejs.client.painter.screen.PaintScreenEventJS;
import dev.latvian.mods.kubejs.client.painter.screen.ScreenPainterObject;
import net.minecraft.client.gui.GuiGraphics;

public abstract class RenderJSPainterObject extends BoxObject implements IRenderJSPainterObject{

    public PaintScreenEventJS eventJS;
    public RenderJSPainter.Layer layer = RenderJSPainter.Layer.hud;
    public RenderJSPainter.Step step = RenderJSPainter.Step.belowAll;

    public RenderJSPainterObject(Painter painter) {
        super(painter);
    }

    @Override
    public void draw(PaintScreenEventJS paintScreenEventJS) {
        eventJS = paintScreenEventJS;
    }

    @Override
    protected void load(PainterObjectProperties properties) {
        super.load(properties);
        layer = RenderJSPainter.Layer.valueOf(properties.getString("layer", "layer"));
        step = RenderJSPainter.Step.valueOf(properties.getString("step", "step"));
    }

    public abstract void draw(GuiGraphics guiGraphics);

    @Override
    public RenderJSPainter.Layer getLayer() {
        return layer;
    }

    @Override
    public RenderJSPainter.Step getStep() {
        return step;
    }
}
