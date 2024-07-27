package com.chen1335.renderjs.client;

import com.chen1335.renderjs.Renderjs;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Renderjs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderJSGUI extends GuiComponent {
    public static ArrayList<Consumer<RenderContext>> RENDER_LIST = new ArrayList<>();

    public static ArrayList<Consumer<RenderContext>> READY_RENDER_LIST = new ArrayList<>();
    public ItemRenderer itemRenderer;
    private Minecraft minecraft;

    public static boolean needReload = false;

    public static RenderJSGUI getInstance() {
        return Renderjs.renderJSGUI;
    }

    @HideFromJS
    public static void reload() {
        needReload = true;
    }

    @HideFromJS
    @SubscribeEvent
    public static void RenderGuiEvent(RenderGuiEvent.Post event) {
        getInstance().render(RenderContext.getContext().update(event));
    }

    @Info("添加render\n Add Render")
    public static void addRender(Consumer<RenderContext> consumer) {
        READY_RENDER_LIST.add(consumer);
    }

    public void init() {
        minecraft = Minecraft.getInstance();
        itemRenderer = minecraft.getItemRenderer();
    }

    @HideFromJS
    private void render(RenderContext renderContext) {
        Iterator<Consumer<RenderContext>> iterator = RENDER_LIST.iterator();


        while (iterator.hasNext()) {
            if (Renderjs.CAN_RENDER) {
                iterator.next().accept(renderContext);
            }
        }

        if (needReload) {
            needReload = false;
            RENDER_LIST.clear();
            RENDER_LIST.addAll(READY_RENDER_LIST);
            READY_RENDER_LIST.clear();
        }
    }

    @Info("绘制居中字符串")
    public void drawCenteredStringJS(@NotNull PoseStack poseStack, Font font, Component component, int x, int y, int color) {
        GuiComponent.drawCenteredString(poseStack, font, component, x, y, color);
    }

    @Info("绘制有阴影字符串")
    public void drawShadowJS(PoseStack poseStack, Font font, Component component, int x, int y, int color) {
        font.drawShadow(poseStack, component, (float) x, (float) y, color);
    }

    @Info("绘制无阴影字符串\nDraw a shadowless string")
    public void drawJS(PoseStack poseStack, Font font, Component component, int x, int y, int color) {
        font.draw(poseStack, component, (float) x, (float) y, color);
    }

    @Info("rgba颜色转10进制")
    public int rgbaColor(int r, int g, int b, int a) {
        return new Color(r, g, b, a).getRGB();
    }

    @Info("绘制图片，需要与RenderSystem类配合使用,总图片大小默认256x256\nTo draw an image, it needs to be used in conjunction with the RenderSystem class, with a default total image size of 256x256")
    public void blitJS(@NotNull PoseStack poseStack, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        super.blit(poseStack, x, y, uOffset, vOffset, uWidth, vHeight);
    }

    @Info("render entity at screen")
    public void renderEntity1(int pPosX, int pPosY, int pScale, float angleX, float angleY, LivingEntity pLivingEntity) {
        InventoryScreen.renderEntityInInventoryRaw(pPosX, pPosY, pScale, angleX, angleY, pLivingEntity);
    }

    @Info("render entity at screen")
    public void renderEntity2(int pPosX, int pPosY, int pScale, float lookAtX, float lookAtY, LivingEntity pLivingEntity) {
        InventoryScreen.renderEntityInInventory(pPosX, pPosY, pScale, lookAtX, lookAtY, pLivingEntity);
    }

    @Info("获取font")
    public Font getFont() {
        return this.minecraft.font;
    }

    @Info("绘制物品\n Draw Item")
    public void renderItem1(ItemStack itemStack, int x, int y) {
        itemRenderer.renderAndDecorateItem(itemStack, x, y);
    }

    @Info("绘制物品\n Draw Item")
    public void renderItem2(ItemStack itemStack, PoseStack poseStack, int x, int y) {
        BakedModel bakedModel = itemRenderer.getModel(itemStack, null, null, 0);
        itemRenderer.textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(x, y, 150);
        posestack.translate(8.0D, 8.0D, 0.0D);
        posestack.scale(1.0F, -1.0F, 1.0F);
        posestack.scale(16.0F, 16.0F, 16.0F);
        posestack.mulPoseMatrix(poseStack.last().pose());
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !bakedModel.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }

        itemRenderer.render(itemStack, ItemTransforms.TransformType.GUI, false, posestack1, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
        multibuffersource$buffersource.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) {
            Lighting.setupFor3DItems();
        }
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    public static class RenderContext {
        private static final RenderContext context = new RenderContext();
        public Window window;
        public PoseStack poseStack;
        public float partialTick;

        public static RenderContext getContext() {
            return context;
        }

        @HideFromJS
        public RenderContext update(RenderGuiEvent.Post event) {
            this.window = event.getWindow();
            this.poseStack = event.getPoseStack();
            this.partialTick = event.getPartialTick();
            return this;
        }
    }

    @Override
    public void hLine(@NotNull PoseStack pPoseStack, int pMinX, int pMaxX, int pY, int pColor){
        super.hLine(pPoseStack, pMinX, pMaxX, pY, pColor);
    }

    @Override
    public void vLine(@NotNull PoseStack pPoseStack, int pX, int pMinY, int pMaxY, int pColor){
        super.vLine(pPoseStack, pX, pMinY, pMaxY, pColor);
    }
}
