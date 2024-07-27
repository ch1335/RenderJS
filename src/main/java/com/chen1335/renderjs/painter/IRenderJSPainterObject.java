package com.chen1335.renderjs.painter;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IRenderJSPainterObject {
    void draw(PoseStack poseStack);

    RenderJSPainter.Layer getLayer();
    RenderJSPainter.Step getStep();
}
