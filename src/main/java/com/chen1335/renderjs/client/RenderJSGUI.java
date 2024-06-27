package com.chen1335.renderjs.client;

import com.chen1335.renderjs.ProbeSupport.ParamInfo;
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
    public void drawCenteredStringJS(@ParamInfo(argName = "font") Font font,
                                     @ParamInfo(argName = "component") Component component,
                                     @ParamInfo(argName = "x") int x,
                                     @ParamInfo(argName = "y") int y,
                                     @ParamInfo(argName = "color") int color) {
        this.drawCenteredString(font, component, x, y, color);
    }

    @Info("绘制字符串")
    public void drawStringJS(@ParamInfo(argName = "font") Font font,
                             @ParamInfo(argName = "component") Component component,
                             @ParamInfo(argName = "x") int x,
                             @ParamInfo(argName = "y") int y,
                             @ParamInfo(argName = "color") int color,
                             @ParamInfo(argName = "dropShadow") boolean dropShadow) {
        this.drawString(font, component, x, y, color, dropShadow);
    }

    @Info(value = "绘制字符串")
    public void drawInBatchJS(@ParamInfo(argName = "pText") Component pText,
                              @ParamInfo(argName = "pX") float pX,
                              @ParamInfo(argName = "pY") float pY,
                              @ParamInfo(argName = "pColor") int pColor,
                              @ParamInfo(argName = "pDropShadow") boolean pDropShadow,
                              @ParamInfo(argName = "pMatrix") Matrix4f pMatrix,
                              @ParamInfo(argName = "pBuffer") MultiBufferSource pBuffer,
                              @ParamInfo(argName = "pDisplayMode") Font.DisplayMode pDisplayMode,
                              @ParamInfo(argName = "pBackgroundColor") int pBackgroundColor,
                              @ParamInfo(argName = "pPackedLightCoords") int pPackedLightCoords) {
        this.getFont().drawInBatch(pText, pX, pY, pColor, pDropShadow, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords);
    }

    @Info("rgba颜色转10进制")
    public int rgbaColor(@ParamInfo(argName = "r") int r,
                         @ParamInfo(argName = "g") int g,
                         @ParamInfo(argName = "b") int b,
                         @ParamInfo(argName = "a") int a) {
        return new Color(r, g, b, a).getRGB();
    }

    @Info("绘制图片,总图片大小默认256x256")
    public void blitJS(@ParamInfo(argName = "pAtlasLocation")ResourceLocation pAtlasLocation,
                       @ParamInfo(argName = "x")int x,
                       @ParamInfo(argName = "y")int y,
                       @ParamInfo(argName = "uOffset")int uOffset,
                       @ParamInfo(argName = "vOffset")int vOffset,
                       @ParamInfo(argName = "uWidth")int uWidth,
                       @ParamInfo(argName = "vHeight")int vHeight) {
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
