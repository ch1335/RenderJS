package com.chen1335.renderjs.kubejs.bindings;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.lighting.SkyLightEngine;

@Info("主要给RenderJSItemDecorator和gui使用")
public class RenderJSUtils {
    @Info("绘制材质(可中心旋转，中心缩放)\nDraw texture (rotatable and scalable)")
    public static void blit(double x, double y, float scaleX, float scaleY, float rotate, int uOffset, int vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotate));
        poseStack.scale(scaleX, scaleY, 1);
        poseStack.translate((double) -uWidth / 2, -(double) vHeight / 2, 0);
        GuiComponent.blit(poseStack, 0, 0, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
        poseStack.popPose();
    }

    @Info("纯色填充\nSolid color filling")
    public static void fillRect(double pX, double pY, int pWidth, int pHeight, int pRed, int pGreen, int pBlue, int pAlpha) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder pRenderer = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        pRenderer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        pRenderer.vertex(pX, pY, 0.0D).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pRenderer.vertex(pX, pY + pHeight, 0.0D).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pRenderer.vertex(pX + pWidth, pY + pHeight, 0.0D).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pRenderer.vertex(pX + pWidth, pY, 0.0D).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        BufferUploader.drawWithShader(pRenderer.end());
    }

    public static int packTextureLight(int positionLight, int skyLight){
        return LightTexture.pack(positionLight, skyLight);
    }
}
