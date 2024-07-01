package com.chen1335.renderjs.client;

import com.chen1335.renderjs.Renderjs;
import com.mojang.blaze3d.platform.Window;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Renderjs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderJSGUI extends GuiGraphics {

    @HideFromJS
    public static RenderJSGUI instance;
    @HideFromJS
    public static ArrayList<Consumer<RenderJSGUI.renderContext>> renderList = new ArrayList<>();
    private final Minecraft minecraft;

    public RenderJSGUI(Minecraft minecraft) {
        super(minecraft, minecraft.renderBuffers().bufferSource());
        this.minecraft = minecraft;
    }

    @HideFromJS
    public static RenderJSGUI getInstance() {
        return instance;
    }

    public static void clearRender() {
        renderList.clear();
    }

    @HideFromJS
    @SubscribeEvent
    public static void RenderGuiEvent(RenderGuiEvent.Post event) {
        instance.render(renderContext.instance.setContext(event));
    }

    @HideFromJS
    public void addRender(Consumer<RenderJSGUI.renderContext> consumer) {
        renderList.add(consumer);
    }

    @HideFromJS
    private void render(renderContext renderContext) {
        Iterator<Consumer<RenderJSGUI.renderContext>> iterator = renderList.iterator();
        while (iterator.hasNext()) {
            iterator.next().accept(renderContext);
        }
    }

    @Info("绘制居中字符串")
    public void drawCenteredStringJS(Font font,
                                     Component component,
                                     int x,
                                     int y,
                                     int color) {
        this.drawCenteredString(font, component, x, y, color);
    }

    @Info("绘制字符串")
    public void drawStringJS(Font font,
                             Component component,
                             int x,
                             int y,
                             int color,
                             boolean dropShadow) {
        this.drawString(font, component, x, y, color, dropShadow);
    }

    @Info(value = "绘制字符串")
    public void drawInBatchJS(Component pText,
                              float pX,
                              float pY,
                              int pColor,
                              boolean pDropShadow,
                              Matrix4f pMatrix,
                              MultiBufferSource pBuffer,
                              Font.DisplayMode pDisplayMode,
                              int pBackgroundColor,
                              int pPackedLightCoords) {
        this.getFont().drawInBatch(pText, pX, pY, pColor, pDropShadow, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords);
    }

    @Info("rgba颜色转10进制")
    public int rgbaColor(int r,
                         int g,
                         int b,
                         int a) {
        return new Color(r, g, b, a).getRGB();
    }

    @Info("绘制图片,总图片大小默认256x256")
    public void blitJS(ResourceLocation pAtlasLocation,
                       int x,
                       int y,
                       int uOffset,
                       int vOffset,
                       int uWidth,
                       int vHeight) {
        super.blit(pAtlasLocation, x, y, uOffset, vOffset, uWidth, vHeight);
    }

    @Info("获取font")
    public Font getFont() {
        return this.minecraft.font;
    }

    public static class renderContext {
        public static renderContext instance = new renderContext();
        public Window window;
        public GuiGraphics guiGraphics;
        public float partialTick;

        @HideFromJS
        public renderContext setContext(RenderGuiEvent.Post event) {
            this.window = event.getWindow();
            this.guiGraphics = event.getGuiGraphics();
            this.partialTick = event.getPartialTick();
            return this;
        }
    }
}
