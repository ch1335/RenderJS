package com.chen1335.renderjs.painter;


import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.painter.PainterObjectProperties;
import dev.latvian.mods.kubejs.client.painter.screen.PaintScreenEventJS;
import dev.latvian.mods.kubejs.client.painter.screen.ScreenPainterObject;

public abstract class RenderJSPainterObject extends ScreenPainterObject implements IRenderJSPainterObject{

    public PaintScreenEventJS eventJS;
    public RenderJSPainter.Layer layer = RenderJSPainter.Layer.hud;
    public RenderJSPainter.Step step = RenderJSPainter.Step.belowAll;

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

    public abstract void draw(PoseStack poseStack);

    @Override
    public RenderJSPainter.Layer getLayer() {
        return layer;
    }

    @Override
    public RenderJSPainter.Step getStep() {
        return step;
    }
}
