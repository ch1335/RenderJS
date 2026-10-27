package com.chen1335.renderjs.API;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.awt.Color;

/**
 * GUI drawing helper built on the 26.1.2 {@link GuiGraphicsExtractor} render state extractor.
 * <p>
 * Colors are {@code long} on purpose: KubeJS script literals such as {@code 0xFF00FF00} are
 * double values above {@code Integer.MAX_VALUE} and would otherwise fail Rhino's int conversion.
 */
public interface IGuiRenderHelper {
    @HideFromJS
    GuiGraphicsExtractor getGuiGraphics();

    @Info("Draw an image at a fixed size (Automatic stretching)")
    default void drawTextureWithSize(Identifier texture, int x, int y, int width, int height, int blitOffset) {
        getGuiGraphics().blit(texture, x, y, x + width, y + height, 0.0F, 1.0F, 0.0F, 1.0F);
    }

    @Info("Draw an image at a fixed size (Automatic stretching)")
    default void drawTextureWithSize(Identifier texture, int x, int y, int width, int height) {
        drawTextureWithSize(texture, x, y, width, height, 0);
    }

    default void drawTexture(Identifier texture, int x, int y, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight, int blitOffset) {
        getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, texture, x, y, uOffset, vOffset, uWidth, vHeight, uWidth, vHeight, textureWidth, textureHeight);
    }

    default void drawTexture(Identifier texture, int x, int y, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight) {
        drawTexture(texture, x, y, textureWidth, textureHeight, uOffset, vOffset, uWidth, vHeight, 0);
    }

    default void drawTexture(Identifier texture, int x, int y, int textureWidth, int textureHeight) {
        drawTexture(texture, x, y, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight);
    }

    default void drawString(Component component, int x, int y, int r, int g, int b, int a) {
        getGuiGraphics().text(Minecraft.getInstance().font, component, x, y, new Color(r, g, b, a).getRGB(), false);
    }

    default void drawString(Component component, int x, int y, long color) {
        getGuiGraphics().text(Minecraft.getInstance().font, component, x, y, (int) color, false);
    }

    default void drawShadowString(Component component, int x, int y, int r, int g, int b, int a) {
        getGuiGraphics().text(Minecraft.getInstance().font, component, x, y, new Color(r, g, b, a).getRGB(), true);
    }

    default void drawShadowString(Component component, int x, int y, long color) {
        getGuiGraphics().text(Minecraft.getInstance().font, component, x, y, (int) color, true);
    }

    @Info("Draw an item stack inside the GUI")
    default void renderGuiItem(ItemStack itemStack, int x, int y) {
        getGuiGraphics().item(itemStack, x, y);
    }

    default void hLine(int minX, int maxX, int y, long color) {
        if (maxX < minX) {
            int i = minX;
            minX = maxX;
            maxX = i;
        }
        getGuiGraphics().fill(minX, y, maxX + 1, y + 1, (int) color);
    }

    default void hLine(int minX, int maxX, int y, int r, int g, int b, int a) {
        hLine(minX, maxX, y, new Color(r, g, b, a).getRGB());
    }

    default void vLine(int x, int minY, int maxY, long color) {
        if (maxY < minY) {
            int i = minY;
            minY = maxY;
            maxY = i;
        }
        getGuiGraphics().fill(x, minY + 1, x + 1, maxY, (int) color);
    }

    default void vLine(int x, int minY, int maxY, int r, int g, int b, int a) {
        vLine(x, minY, maxY, new Color(r, g, b, a).getRGB());
    }

    default void fill(int minX, int minY, int maxX, int maxY, long color) {
        getGuiGraphics().fill(minX, minY, maxX, maxY, (int) color);
    }

    default void fill(int minX, int minY, int maxX, int maxY, int r, int g, int b, int a) {
        fill(minX, minY, maxX, maxY, new Color(r, g, b, a).getRGB());
    }
}
