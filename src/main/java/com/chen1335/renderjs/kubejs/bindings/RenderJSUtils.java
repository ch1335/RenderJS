package com.chen1335.renderjs.kubejs.bindings;

import com.chen1335.renderjs.ProbeSupport.ParamInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

@Info("主要给RenderJSItemDecorator和gui使用")
public class RenderJSUtils {
    @Info("绘制材质(可中心旋转，中心缩放)")
    public static void blit(@ParamInfo(argName = "guiGraphics")GuiGraphics guiGraphics,
                            @ParamInfo(argName = "pAtlasLocation")ResourceLocation pAtlasLocation,
                            @ParamInfo(argName = "x")double x,
                            @ParamInfo(argName = "y")double y,
                            @ParamInfo(argName = "scaleX")float scaleX,
                            @ParamInfo(argName = "scaleY")float scaleY,
                            @ParamInfo(argName = "rotate")float rotate,
                            @ParamInfo(argName = "uOffset")int uOffset,
                            @ParamInfo(argName = "vOffset")int vOffset,
                            @ParamInfo(argName = "uWidth")int uWidth,
                            @ParamInfo(argName = "vHeight")int vHeight,
                            @ParamInfo(argName = "textureWidth")int textureWidth,
                            @ParamInfo(argName = "textureHeight")int textureHeight) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotate));
        poseStack.scale(scaleX, scaleY, 1);
        poseStack.translate((double) -uWidth / 2, -(double) vHeight / 2, 0);
        guiGraphics.blit(pAtlasLocation, 0, 0, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
        poseStack.popPose();
    }

    @Info(value = "纯色填充")
    public static void fill(@ParamInfo(argName = "guiGraphics") GuiGraphics guiGraphics,
                            @ParamInfo(argName = "minX") int pMinX,
                            @ParamInfo(argName = "minY") int pMinY,
                            @ParamInfo(argName = "maxX") int pMaxX,
                            @ParamInfo(argName = "maxY") int pMaxY,
                            @ParamInfo(argName = "r") int pRed,
                            @ParamInfo(argName = "g") int pGreen,
                            @ParamInfo(argName = "b") int pBlue,
                            @ParamInfo(argName = "a") int pAlpha) {
        guiGraphics.fill(pMinX, pMinY, pMaxX, pMaxY, new Color(pRed, pGreen, pBlue, pAlpha).getRGB());
    }
}
