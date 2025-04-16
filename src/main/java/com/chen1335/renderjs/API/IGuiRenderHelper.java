package com.chen1335.renderjs.API;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public interface IGuiRenderHelper {
    @HideFromJS
    IGuiRenderHelper guiRenderHelper = new IGuiRenderHelper() {
        GuiGraphics guiGraphics;

        @Override
        public GuiGraphics getGuiGraphics() {
            if (guiGraphics == null) {
                guiGraphics = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
            }
            return guiGraphics;
        }
    };

    GuiGraphics getGuiGraphics();


    default void drawTexture(ResourceLocation resourceLocation, int x, int y, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight, int blitOffset) {
        getGuiGraphics().blit(resourceLocation, x, y, blitOffset, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
    }

    default void drawTexture(ResourceLocation resourceLocation, int x, int y, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight) {
        this.drawTexture(resourceLocation, x, y, textureWidth, textureHeight, uOffset, vOffset, uWidth, vHeight, 0);
    }

    default void drawTexture(ResourceLocation resourceLocation, int x, int y, int textureWidth, int textureHeight) {
        this.drawTexture(resourceLocation, x, y, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight);
    }

    default void drawString(Component component, int x, int y, int r, int g, int b, int a) {
        getGuiGraphics().drawString(Minecraft.getInstance().font, component, x, y, new Color(r, g, b, a).getRGB(), false);
    }

    default void drawString(Component component, int x, int y, int r, int g, int b, int a, boolean shadow) {
        getGuiGraphics().drawString(Minecraft.getInstance().font, component, x, y, new Color(r, g, b, a).getRGB(), shadow);
    }

    default void drawString(Component component, int x, int y, int color) {
        getGuiGraphics().drawString(Minecraft.getInstance().font, component, x, y, color, false);
    }

    default void drawShadowString(Component component, int x, int y, int r, int g, int b, int a) {
        getGuiGraphics().drawString(Minecraft.getInstance().font, component, x, y, new Color(r, g, b, a).getRGB(), true);
    }

    default void drawShadowString(Component component, int x, int y, int color) {
        getGuiGraphics().drawString(Minecraft.getInstance().font, component, x, y, color, true);
    }

    @Info("don's use this in level render, use renderLevelItem")
    default void renderGuiItem(ItemStack itemStack, int x, int y) {
        getGuiGraphics().renderItem(itemStack, x, y);
    }


    default void hLine(int pMinX, int pMaxX, int pY, int pColor) {
        if (pMaxX < pMinX) {
            int i = pMinX;
            pMinX = pMaxX;
            pMaxX = i;
        }
        getGuiGraphics().fill(pMinX, pY, pMaxX + 1, pY + 1, pColor);
    }

    default void hLine(int pMinX, int pMaxX, int pY, int r, int g, int b, int a) {
        if (pMaxX < pMinX) {
            int i = pMinX;
            pMinX = pMaxX;
            pMaxX = i;
        }
        getGuiGraphics().fill(pMinX, pY, pMaxX + 1, pY + 1, new Color(r, g, b, a).getRGB());
    }

    default void vLine(PoseStack pPoseStack, int pX, int pMinY, int pMaxY, int pColor) {
        if (pMaxY < pMinY) {
            int i = pMinY;
            pMinY = pMaxY;
            pMaxY = i;
        }
        getGuiGraphics().fill(pX, pMinY + 1, pX + 1, pMaxY, pColor);
    }

    default void vLine(int pX, int pMinY, int pMaxY, int r, int g, int b, int a) {
        if (pMaxY < pMinY) {
            int i = pMinY;
            pMinY = pMaxY;
            pMaxY = i;
        }
        getGuiGraphics().fill(pX, pMinY + 1, pX + 1, pMaxY, new Color(r, g, b, a).getRGB());
    }

    default void fill(int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor) {
        getGuiGraphics().fill(pMinX, pMinY, pMaxX, pMaxY, pColor);
    }

    default void fill(int pMinX, int pMinY, int pMaxX, int pMaxY, int r, int g, int b, int a) {
        getGuiGraphics().fill(pMinX, pMinY, pMaxX, pMaxY, new Color(r, g, b, a).getRGB());
    }
}
