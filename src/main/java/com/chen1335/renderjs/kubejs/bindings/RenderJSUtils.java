package com.chen1335.renderjs.kubejs.bindings;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class RenderJSUtils {


    @Info("Draw texture (rotatable and scalable)")
    public static void blit(GuiGraphics guiGraphics,
                            ResourceLocation pAtlasLocation,
                            double x,
                            double y,
                            float scaleX,
                            float scaleY,
                            float rotate,
                            int uOffset,
                            int vOffset,
                            int uWidth,
                            int vHeight,
                            int textureWidth,
                            int textureHeight) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotate));
        poseStack.scale(scaleX, scaleY, 1);
        poseStack.translate((double) -uWidth / 2, -(double) vHeight / 2, 0);
        guiGraphics.blit(pAtlasLocation, 0, 0, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
        poseStack.popPose();
    }


    public static void fill(GuiGraphics guiGraphics,
                            int pMinX,
                            int pMinY,
                            int pMaxX,
                            int pMaxY,
                            int pRed,
                            int pGreen,
                            int pBlue,
                            int pAlpha) {
        guiGraphics.fill(pMinX, pMinY, pMaxX, pMaxY, new Color(pRed, pGreen, pBlue, pAlpha).getRGB());
    }
}
