package com.chen1335.renderjs.API;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

import java.awt.Color;
import java.util.function.Consumer;

/**
 * GUI drawing helper built on the 26.1.2 {@link GuiGraphicsExtractor} render state extractor.
 * <p>
 * Colors are {@code long} on purpose: KubeJS script literals such as {@code 0xFF00FF00} are
 * double values above {@code Integer.MAX_VALUE} and would otherwise fail Rhino's int conversion.
 * <p>
 * Scripts draw with a private copy of the GUI matrix stack, so every drawing call temporarily applies
 * that copy to the extractor before submitting geometry and restores the extractor afterwards.
 */
@Info("Drawing helpers for HUD and Screen rendering. Only usable from a GUI render event callback.")
public interface IGuiRenderHelper {
    GuiGraphicsExtractor getGuiGraphics();

    Matrix3x2fStack getPoseStack();

    /**
     * Runs the given drawing action on the extractor with the script controlled matrix applied.
     */
    default void withPose(Consumer<GuiGraphicsExtractor> action) {
        GuiGraphicsExtractor guiGraphics = getGuiGraphics();
        Matrix3x2fStack enginePose = guiGraphics.pose();
        enginePose.pushMatrix();
        enginePose.set(getPoseStack());
        try {
            action.accept(guiGraphics);
        } finally {
            enginePose.popMatrix();
        }
    }

    @Info(value = "Draws a texture stretched to the specified size.",
            params = {
                    @Param(name = "texture", value = "Identifier of the texture to draw."),
                    @Param(name = "x", value = "Left edge of the destination rectangle, in GUI pixels."),
                    @Param(name = "y", value = "Top edge of the destination rectangle, in GUI pixels."),
                    @Param(name = "width", value = "Destination width, in GUI pixels."),
                    @Param(name = "height", value = "Destination height, in GUI pixels."),
                    @Param(name = "blitOffset", value = "Legacy depth offset. Ignored in 26.1.2 and kept for compatibility.")
            })
    default void drawTextureWithSize(Identifier texture, int x, int y, int width, int height, int blitOffset) {
        withPose(guiGraphics -> guiGraphics.blit(texture, x, y, x + width, y + height, 0.0F, 1.0F, 0.0F, 1.0F));
    }

    @Info(value = "Draws a texture stretched to the specified size.",
            params = {
                    @Param(name = "texture", value = "Identifier of the texture to draw."),
                    @Param(name = "x", value = "Left edge of the destination rectangle, in GUI pixels."),
                    @Param(name = "y", value = "Top edge of the destination rectangle, in GUI pixels."),
                    @Param(name = "width", value = "Destination width, in GUI pixels."),
                    @Param(name = "height", value = "Destination height, in GUI pixels.")
            })
    default void drawTextureWithSize(Identifier texture, int x, int y, int width, int height) {
        drawTextureWithSize(texture, x, y, width, height, 0);
    }

    @Info(value = "Draws a sub-region of a texture.",
            params = {
                    @Param(name = "texture", value = "Identifier of the texture to draw."),
                    @Param(name = "x", value = "Left edge of the destination rectangle, in GUI pixels."),
                    @Param(name = "y", value = "Top edge of the destination rectangle, in GUI pixels."),
                    @Param(name = "textureWidth", value = "Full width of the texture, used to convert UV coordinates."),
                    @Param(name = "textureHeight", value = "Full height of the texture, used to convert UV coordinates."),
                    @Param(name = "uOffset", value = "Horizontal source offset, in texture pixels."),
                    @Param(name = "vOffset", value = "Vertical source offset, in texture pixels."),
                    @Param(name = "uWidth", value = "Width of the source region, in texture pixels."),
                    @Param(name = "vHeight", value = "Height of the source region, in texture pixels."),
                    @Param(name = "blitOffset", value = "Legacy depth offset. Ignored in 26.1.2 and kept for compatibility.")
            })
    default void drawTexture(Identifier texture, int x, int y, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight, int blitOffset) {
        withPose(guiGraphics -> guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, uOffset, vOffset, uWidth, vHeight, uWidth, vHeight, textureWidth, textureHeight));
    }

    @Info(value = "Draws a sub-region of a texture.",
            params = {
                    @Param(name = "texture", value = "Identifier of the texture to draw."),
                    @Param(name = "x", value = "Left edge of the destination rectangle, in GUI pixels."),
                    @Param(name = "y", value = "Top edge of the destination rectangle, in GUI pixels."),
                    @Param(name = "textureWidth", value = "Full width of the texture, used to convert UV coordinates."),
                    @Param(name = "textureHeight", value = "Full height of the texture, used to convert UV coordinates."),
                    @Param(name = "uOffset", value = "Horizontal source offset, in texture pixels."),
                    @Param(name = "vOffset", value = "Vertical source offset, in texture pixels."),
                    @Param(name = "uWidth", value = "Width of the source region, in texture pixels."),
                    @Param(name = "vHeight", value = "Height of the source region, in texture pixels.")
            })
    default void drawTexture(Identifier texture, int x, int y, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight) {
        drawTexture(texture, x, y, textureWidth, textureHeight, uOffset, vOffset, uWidth, vHeight, 0);
    }

    @Info(value = "Draws a texture at its native size.",
            params = {
                    @Param(name = "texture", value = "Identifier of the texture to draw."),
                    @Param(name = "x", value = "Left edge of the destination rectangle, in GUI pixels."),
                    @Param(name = "y", value = "Top edge of the destination rectangle, in GUI pixels."),
                    @Param(name = "textureWidth", value = "Width of the texture, in pixels."),
                    @Param(name = "textureHeight", value = "Height of the texture, in pixels.")
            })
    default void drawTexture(Identifier texture, int x, int y, int textureWidth, int textureHeight) {
        drawTexture(texture, x, y, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight);
    }

    @Info(value = "Draws text without a drop shadow, with an explicit RGBA color.",
            params = {
                    @Param(name = "component", value = "Text component to draw."),
                    @Param(name = "x", value = "Left edge of the text, in GUI pixels."),
                    @Param(name = "y", value = "Top edge of the text, in GUI pixels."),
                    @Param(name = "r", value = "Red channel, 0 to 255."),
                    @Param(name = "g", value = "Green channel, 0 to 255."),
                    @Param(name = "b", value = "Blue channel, 0 to 255."),
                    @Param(name = "a", value = "Alpha channel, 0 to 255.")
            })
    default void drawString(Component component, int x, int y, int r, int g, int b, int a) {
        int color = new Color(r, g, b, a).getRGB();
        withPose(guiGraphics -> guiGraphics.text(Minecraft.getInstance().font, component, x, y, color, false));
    }

    @Info(value = "Draws text without a drop shadow.",
            params = {
                    @Param(name = "component", value = "Text component to draw."),
                    @Param(name = "x", value = "Left edge of the text, in GUI pixels."),
                    @Param(name = "y", value = "Top edge of the text, in GUI pixels."),
                    @Param(name = "color", value = "ARGB color in 0xAARRGGBB format.")
            })
    default void drawString(Component component, int x, int y, long color) {
        withPose(guiGraphics -> guiGraphics.text(Minecraft.getInstance().font, component, x, y, (int) color, false));
    }

    @Info(value = "Draws text with a drop shadow, with an explicit RGBA color.",
            params = {
                    @Param(name = "component", value = "Text component to draw."),
                    @Param(name = "x", value = "Left edge of the text, in GUI pixels."),
                    @Param(name = "y", value = "Top edge of the text, in GUI pixels."),
                    @Param(name = "r", value = "Red channel, 0 to 255."),
                    @Param(name = "g", value = "Green channel, 0 to 255."),
                    @Param(name = "b", value = "Blue channel, 0 to 255."),
                    @Param(name = "a", value = "Alpha channel, 0 to 255.")
            })
    default void drawShadowString(Component component, int x, int y, int r, int g, int b, int a) {
        int color = new Color(r, g, b, a).getRGB();
        withPose(guiGraphics -> guiGraphics.text(Minecraft.getInstance().font, component, x, y, color, true));
    }

    @Info(value = "Draws text with a drop shadow.",
            params = {
                    @Param(name = "component", value = "Text component to draw."),
                    @Param(name = "x", value = "Left edge of the text, in GUI pixels."),
                    @Param(name = "y", value = "Top edge of the text, in GUI pixels."),
                    @Param(name = "color", value = "ARGB color in 0xAARRGGBB format.")
            })
    default void drawShadowString(Component component, int x, int y, long color) {
        withPose(guiGraphics -> guiGraphics.text(Minecraft.getInstance().font, component, x, y, (int) color, true));
    }

    @Info(value = "Draws an item stack icon at the specified position.",
            params = {
                    @Param(name = "itemStack", value = "Item stack to draw."),
                    @Param(name = "x", value = "Left edge of the icon, in GUI pixels."),
                    @Param(name = "y", value = "Top edge of the icon, in GUI pixels.")
            })
    default void renderGuiItem(ItemStack itemStack, int x, int y) {
        withPose(guiGraphics -> guiGraphics.item(itemStack, x, y));
    }

    @Info(value = "Draws a horizontal line.",
            params = {
                    @Param(name = "minX", value = "Left edge of the line, inclusive."),
                    @Param(name = "maxX", value = "Right edge of the line, exclusive."),
                    @Param(name = "y", value = "Vertical position of the line."),
                    @Param(name = "color", value = "ARGB color in 0xAARRGGBB format.")
            })
    default void hLine(int minX, int maxX, int y, long color) {
        int fromX = Math.min(minX, maxX);
        int toX = Math.max(minX, maxX);
        int lineY = y;
        withPose(guiGraphics -> guiGraphics.fill(fromX, lineY, toX + 1, lineY + 1, (int) color));
    }

    @Info(value = "Draws a horizontal line, with an explicit RGBA color.",
            params = {
                    @Param(name = "minX", value = "Left edge of the line, inclusive."),
                    @Param(name = "maxX", value = "Right edge of the line, exclusive."),
                    @Param(name = "y", value = "Vertical position of the line."),
                    @Param(name = "r", value = "Red channel, 0 to 255."),
                    @Param(name = "g", value = "Green channel, 0 to 255."),
                    @Param(name = "b", value = "Blue channel, 0 to 255."),
                    @Param(name = "a", value = "Alpha channel, 0 to 255.")
            })
    default void hLine(int minX, int maxX, int y, int r, int g, int b, int a) {
        hLine(minX, maxX, y, new Color(r, g, b, a).getRGB());
    }

    @Info(value = "Draws a vertical line.",
            params = {
                    @Param(name = "x", value = "Horizontal position of the line."),
                    @Param(name = "minY", value = "Top edge of the line, inclusive."),
                    @Param(name = "maxY", value = "Bottom edge of the line, exclusive."),
                    @Param(name = "color", value = "ARGB color in 0xAARRGGBB format.")
            })
    default void vLine(int x, int minY, int maxY, long color) {
        if (maxY < minY) {
            int i = minY;
            minY = maxY;
            maxY = i;
        }
        int lineX = x;
        int fromY = minY;
        int toY = maxY;
        withPose(guiGraphics -> guiGraphics.fill(lineX, fromY + 1, lineX + 1, toY, (int) color));
    }

    @Info(value = "Draws a vertical line, with an explicit RGBA color.",
            params = {
                    @Param(name = "x", value = "Horizontal position of the line."),
                    @Param(name = "minY", value = "Top edge of the line, inclusive."),
                    @Param(name = "maxY", value = "Bottom edge of the line, exclusive."),
                    @Param(name = "r", value = "Red channel, 0 to 255."),
                    @Param(name = "g", value = "Green channel, 0 to 255."),
                    @Param(name = "b", value = "Blue channel, 0 to 255."),
                    @Param(name = "a", value = "Alpha channel, 0 to 255.")
            })
    default void vLine(int x, int minY, int maxY, int r, int g, int b, int a) {
        vLine(x, minY, maxY, new Color(r, g, b, a).getRGB());
    }

    @Info(value = "Draws a filled rectangle.",
            params = {
                    @Param(name = "minX", value = "Left edge of the rectangle."),
                    @Param(name = "minY", value = "Top edge of the rectangle."),
                    @Param(name = "maxX", value = "Right edge of the rectangle."),
                    @Param(name = "maxY", value = "Bottom edge of the rectangle."),
                    @Param(name = "color", value = "ARGB color in 0xAARRGGBB format.")
            })
    default void fill(int minX, int minY, int maxX, int maxY, long color) {
        withPose(guiGraphics -> guiGraphics.fill(minX, minY, maxX, maxY, (int) color));
    }

    @Info(value = "Draws a filled rectangle, with an explicit RGBA color.",
            params = {
                    @Param(name = "minX", value = "Left edge of the rectangle."),
                    @Param(name = "minY", value = "Top edge of the rectangle."),
                    @Param(name = "maxX", value = "Right edge of the rectangle."),
                    @Param(name = "maxY", value = "Bottom edge of the rectangle."),
                    @Param(name = "r", value = "Red channel, 0 to 255."),
                    @Param(name = "g", value = "Green channel, 0 to 255."),
                    @Param(name = "b", value = "Blue channel, 0 to 255."),
                    @Param(name = "a", value = "Alpha channel, 0 to 255.")
            })
    default void fill(int minX, int minY, int maxX, int maxY, int r, int g, int b, int a) {
        fill(minX, minY, maxX, maxY, new Color(r, g, b, a).getRGB());
    }
}
