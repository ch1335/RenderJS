package com.chen1335.renderjs.client;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

@Info("this interface is just try to fix some commonly used functions args name in GuiGraphics")
public interface RenderJSGuiRenderHelper {
    GuiGraphicsPainter guiGraphicsPainter = new GuiGraphicsPainter();

    default GuiGraphicsPainter getGuiRenderHelper() {
        return guiGraphicsPainter;
    }

    class GuiGraphicsPainter {
        private GuiGraphics guiGraphics;

        public GuiGraphicsPainter update(GuiGraphics guiGraphics) {
            this.guiGraphics = guiGraphics;
            return this;
        }

        @Info("Draws a horizontal line from minX to maxX at the specified y-coordinate with the given color.\npMinX – the x-coordinate of the start point. pMaxX – the x-coordinate of the end point. pY – the y-coordinate of the line. pColor – the color of the line.")
        public void hLine(int pMinX, int pMaxX, int pY, int pColor) {
            guiGraphics.hLine(RenderType.gui(), pMinX, pMaxX, pY, pColor);
        }

        @Info("Draws a vertical line from minY to maxY at the specified x-coordinate with the given color.\npX – the x-coordinate of the line. pMinY – the y-coordinate of the start point. pMaxY – the y-coordinate of the end point. pColor – the color of the line.")
        public void vLine(int pX, int pMinY, int pMaxY, int pColor) {
            guiGraphics.vLine(RenderType.gui(), pX, pMinY, pMaxY, pColor);
        }

        @Info("Fills a rectangle with the specified color and z-level using the given coordinates as the boundaries.\npMinX – the minimum x-coordinate of the rectangle. pMinY – the minimum y-coordinate of the rectangle. pMaxX – the maximum x-coordinate of the rectangle. pMaxY – the maximum y-coordinate of the rectangle. pZ – the z-level of the rectangle. pColor – the color to fill the rectangle with.")
        public void fill(int pMinX, int pMinY, int pMaxX, int pMaxY, int pZ, int pColor) {
            guiGraphics.fill(RenderType.gui(), pMinX, pMinY, pMaxX, pMaxY, pZ, pColor);
        }

        @Info("Fills a rectangle with a gradient color from colorFrom to colorTo at the specified z-level using the given coordinates as the boundaries.\npX1 – the x-coordinate of the first corner of the rectangle. pY1 – the y-coordinate of the first corner of the rectangle. pX2 – the x-coordinate of the second corner of the rectangle. pY2 – the y-coordinate of the second corner of the rectangle. pZ – the z-level of the rectangle. pColorFrom – the starting color of the gradient. pColorTo – the ending color of the gradient.")
        public void fillGradient(int pX1, int pY1, int pX2, int pY2, int pZ, int pColorFrom, int pColorTo) {
            guiGraphics.fillGradient(RenderType.gui(), pX1, pY1, pX2, pY2, pColorFrom, pColorTo, pZ);
        }

        @Info("Draws a centered string at the specified coordinates using the given font, text component, and color.")
        public void drawCenteredString(Font pFont, Component pText, int pX, int pY, int pColor) {
            guiGraphics.drawCenteredString(pFont, pText, pX, pY, pColor);
        }

        @Info("Draws a component's visual order text at the specified coordinates using the given font, text component, color, and drop shadow.")
        public int drawString(Font pFont, Component pText, int pX, int pY, int pColor, boolean pDropShadow) {
            return guiGraphics.drawString(pFont, pText, pX, pY, pColor, pDropShadow);
        }

        @Info("Draws a formatted text with word wrapping at the specified coordinates using the given font, text, line width, and color.")
        public void drawWordWrap(Font pFont, FormattedText pText, int pX, int pY, int pLineWidth, int pColor) {
            guiGraphics.drawWordWrap(pFont, pText, pX, pY, pLineWidth, pColor);
        }

        public int drawStringWithBackdrop(Font pFont, Component pText, int pX, int pY, int pXOffset, int pColor) {
            return guiGraphics.drawStringWithBackdrop(pFont, pText, pX, pY, pXOffset, pColor);
        }

        @Info("Blits a portion of the specified texture atlas sprite onto the screen at the given coordinates.")
        public void blitSprite(ResourceLocation pSprite, int pX, int pY, int pBlitOffset, int pWidth, int pHeight) {
            guiGraphics.blitSprite(pSprite, pX, pY, pBlitOffset, pWidth, pHeight);
        }

        @Info("Blits a portion of the texture specified by the atlas location onto the screen at the given coordinates with a blit offset and texture coordinates.")
        public void blit(
                ResourceLocation pAtlasLocation,
                int pX,
                int pY,
                int pBlitOffset,
                float pUOffset,
                float pVOffset,
                int pUWidth,
                int pVHeight,
                int pTextureWidth,
                int pTextureHeight
        ) {
            guiGraphics.blit(pAtlasLocation, pX, pY, pBlitOffset, pUOffset, pVOffset, pUWidth, pVHeight, pTextureWidth, pTextureHeight);
        }

        public void renderItem(ItemStack pStack, int pX, int pY) {
            guiGraphics.renderItem(pStack, pX, pY);
        }

        public void renderFakeItem(ItemStack pStack, int pX, int pY) {
            guiGraphics.renderFakeItem(pStack, pX, pY);
        }

        public void renderItemDecorations(Font pFont, ItemStack pStack, int pX, int pY, @Nullable String pText) {
            guiGraphics.renderItemDecorations(pFont, pStack, pX, pY, pText);
        }

        public void renderTooltip(Font pFont, ItemStack pStack, int pMouseX, int pMouseY) {
            guiGraphics.renderTooltip(pFont, pStack, pMouseX, pMouseY);
        }
    }
}

