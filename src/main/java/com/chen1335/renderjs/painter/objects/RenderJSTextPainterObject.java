package com.chen1335.renderjs.painter.objects;

import com.chen1335.renderjs.painter.IRenderJSPainterObject;
import com.chen1335.renderjs.painter.RenderJSPainter;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.client.painter.PainterObjectProperties;
import dev.latvian.mods.kubejs.client.painter.screen.PaintScreenEventJS;
import dev.latvian.mods.kubejs.client.painter.screen.TextObject;

public class RenderJSTextPainterObject extends TextObject implements IRenderJSPainterObject {
    public PaintScreenEventJS eventJS;
    public RenderJSPainter.Layer layer = RenderJSPainter.Layer.hud;
    public RenderJSPainter.Step step = RenderJSPainter.Step.belowAll;

    @Override
    public void preDraw(PaintScreenEventJS event) {

    }

    @Override
    public void draw(PaintScreenEventJS paintScreenEventJS) {
        eventJS = paintScreenEventJS;
    }

    public RenderJSTextPainterObject(Painter painter) {
        super(painter);
    }

    @Override
    protected void load(PainterObjectProperties properties) {
        super.load(properties);
        layer = RenderJSPainter.Layer.valueOf(properties.getString("layer", "layer"));
        step = RenderJSPainter.Step.valueOf(properties.getString("step", "step"));
    }

    @Override
    public void draw(PoseStack poseStack) {
        if (eventJS == null) {
            return;
        }
        super.preDraw(eventJS);
        super.draw(eventJS);
    }

    @Override
    public RenderJSPainter.Layer getLayer() {
        return layer;
    }

    @Override
    public RenderJSPainter.Step getStep() {
        return step;
    }
}
