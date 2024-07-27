package com.chen1335.renderjs.kubejs.bindings;

import com.chen1335.renderjs.client.RenderJSGUI;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@Info("包含了一些GuiComponent里的static方法,主要目的是开放protect方法以及修复冲重名重参导致kjs无法使用的问题")
public class RenderJSGuiComponent extends GuiComponent {

    public static void fillGradientine1(PoseStack pPoseStack, int pX1, int pY1, int pX2, int pY2, int pColorFrom, int pColorTo, int pBlitOffset) {
        GuiComponent.fillGradient(pPoseStack, pX1, pY1, pX2, pY2, pColorFrom, pColorTo, pBlitOffset);
    }

    public static void fillGradientine2(Matrix4f pMatrix, BufferBuilder pBuilder, int pX1, int pY1, int pX2, int pY2, int pBlitOffset, int pColorA, int pColorB) {
        GuiComponent.fillGradient(pMatrix, pBuilder, pX1, pY1, pX2, pY2, pBlitOffset, pColorA, pColorB);
    }

    public static void drawCenteredString1(PoseStack pPoseStack, Font pFont, Component pText, int pX, int pY, int pColor) {
        GuiComponent.drawCenteredString(pPoseStack, pFont, pText, pX, pY, pColor);
    }

    public static void drawString1(@NotNull PoseStack pPoseStack, Font pFont, @NotNull Component pText, int pX, int pY, int pColor) {
        GuiComponent.drawString(pPoseStack, pFont, pText, pX, pY, pColor);
    }

    public static void blit1(PoseStack pPoseStack, int pX, int pY, int pBlitOffset, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureHeight, int pTextureWidth) {
        GuiComponent.blit(pPoseStack, pX, pY, pBlitOffset, pUOffset, pVOffset, pUWidth, pVHeight, pTextureHeight, pTextureWidth);
    }


}
