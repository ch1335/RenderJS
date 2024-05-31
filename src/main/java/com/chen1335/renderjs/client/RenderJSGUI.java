package com.chen1335.renderjs.client;

import com.chen1335.renderjs.Renderjs;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Renderjs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderJSGUI extends GuiComponent {

    @HideFromJS
    public static Boolean isReload = false;

    public static RenderJSGUI instance = new RenderJSGUI(Minecraft.getInstance());

    @Info("获取实例")
    public static RenderJSGUI getInstance() {
        return instance;
    }

    public static ArrayList<Consumer<RenderJSGUI.renderContext>> renderList = new ArrayList<>();
    private final Minecraft minecraft;

    public RenderJSGUI(Minecraft minecraft) {
        this.minecraft = minecraft;
    }
    @Info("添加render")
    public void addRender(Consumer<RenderJSGUI.renderContext> consumer){
        if (isReload) {
            RenderJSGUI.renderList.clear();
            isReload=false;
        }
        renderList.add(consumer);
    }

    @HideFromJS
    @SubscribeEvent
    public static void RenderGuiEvent(RenderGuiEvent.Post event) {
        instance.render(renderContext.instance.setContext(event));
    }

    @HideFromJS
    private void render(renderContext renderContext) {
        renderList.forEach(consumer -> consumer.accept(renderContext));
    }

    @Info("绘制居中字符串")
    public void drawCenteredStringJS(@NotNull PoseStack poseStack, Font font, Component component, int x, int y, int color) {
        GuiComponent.drawCenteredString(poseStack, font, component, x, y, color);
    }
    @Info("绘制有阴影字符串")
    public void drawShadow(PoseStack poseStack, Font font, Component component, int x, int y, int color) {
        font.drawShadow(poseStack, component, (float) x, (float) y, color);
    }
    @Info("绘制无阴影字符串")
    public void draw(PoseStack poseStack, Font font, Component component, int x, int y, int color) {
        font.draw(poseStack, component, (float) x, (float) y, color);
    }
    @Info("rgba颜色转10进制")
    public int rgbaColor(int r, int g, int b, int a) {
        int i = r << 16;
        i = i + g << 8;
        i = i + b;
        return i | (a << 24 & -16777216);
    }

    @Info("绘制图片，需要与RenderSystem类配合使用")
    @Override
    public void blit(@NotNull PoseStack poseStack, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        super.blit(poseStack, x, y, uOffset, vOffset, uWidth, vHeight);
    }

    @Info("获取font")
    public Font getFont() {
        return this.minecraft.font;
    }

    public static class renderContext {
        public static renderContext instance = new renderContext();
        public Window window;
        public PoseStack poseStack;
        public float partialTick;

        @HideFromJS
        public renderContext setContext(RenderGuiEvent.Post event) {
            this.window = event.getWindow();
            this.poseStack = event.getPoseStack();
            this.partialTick = event.getPartialTick();
            return this;
        }
    }
}
