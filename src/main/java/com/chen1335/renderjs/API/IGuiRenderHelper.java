package com.chen1335.renderjs.API;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public interface IGuiRenderHelper{
    @HideFromJS
    IGuiRenderHelper guiRenderHelper = new IGuiRenderHelper() {
        PoseStack poseStack = new PoseStack();

        public void setPoseStack(PoseStack poseStack) {
            this.poseStack = poseStack;
        }

        @Override
        public PoseStack getPoseStack() {
            return poseStack;
        }
    };


    PoseStack getPoseStack();


    default void drawTexture(ResourceLocation resourceLocation, int x, int y, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight, int blitOffset) {
        RenderSystem.setShaderTexture(0, resourceLocation);
        GuiComponent.blit(getPoseStack(), x, y, blitOffset, uOffset, vOffset, uWidth, vHeight, textureHeight, textureWidth);
        RenderSystem.disableTexture();
    }

    default void drawTexture(ResourceLocation resourceLocation, int x, int y, int textureWidth, int textureHeight, int uOffset, int vOffset, int uWidth, int vHeight) {
        this.drawTexture(resourceLocation, x, y, textureWidth, textureHeight, uOffset, vOffset, uWidth, vHeight, 0);
    }

    default void drawTexture(ResourceLocation resourceLocation, int x, int y, int textureWidth, int textureHeight) {
        this.drawTexture(resourceLocation, x, y, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight);
    }

    default void drawString(Component component, float x, float y, int r, int g, int b, int a) {
        Minecraft.getInstance().font.draw(getPoseStack(), component, x, y, new Color(r, g, b, a).getRGB());
    }

    default void drawString(Component component, float x, float y, int color) {
        Minecraft.getInstance().font.draw(getPoseStack(), component, x, y, color);
    }

    default void drawShadowString(Component component, float x, float y, int r, int g, int b, int a) {
        Minecraft.getInstance().font.drawShadow(getPoseStack(), component, x, y, new Color(r, g, b, a).getRGB());
    }

    default void drawShadowString(Component component, float x, float y, int color) {
        Minecraft.getInstance().font.drawShadow(getPoseStack(), component, x, y, color);
    }

    @Info("don's use this in level render, use renderLevelItem")
    default void renderGuiItem(ItemStack itemStack, int x, int y) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel bakedModel = itemRenderer.getModel(itemStack, null, null, 0);
        itemRenderer.textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(x, y, 150);
        posestack.translate(8.0D, 8.0D, 0.0D);
        posestack.scale(1.0F, -1.0F, 1.0F);
        posestack.scale(16.0F, 16.0F, 16.0F);
        posestack.mulPoseMatrix(getPoseStack().last().pose());
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


    default void hLine(int pMinX, int pMaxX, int pY, int pColor) {
        if (pMaxX < pMinX) {
            int i = pMinX;
            pMinX = pMaxX;
            pMaxX = i;
        }
        GuiComponent.fill(getPoseStack(), pMinX, pY, pMaxX + 1, pY + 1, pColor);
    }

    default void hLine(int pMinX, int pMaxX, int pY, int r, int g, int b, int a) {
        if (pMaxX < pMinX) {
            int i = pMinX;
            pMinX = pMaxX;
            pMaxX = i;
        }
        GuiComponent.fill(getPoseStack(), pMinX, pY, pMaxX + 1, pY + 1, new Color(r, g, b, a).getRGB());
    }

    default void vLine(int pX, int pMinY, int pMaxY, int pColor) {
        if (pMaxY < pMinY) {
            int i = pMinY;
            pMinY = pMaxY;
            pMaxY = i;
        }
        GuiComponent.fill(getPoseStack(), pX, pMinY + 1, pX + 1, pMaxY, pColor);
    }

    default void vLine(int pX, int pMinY, int pMaxY, int r, int g, int b, int a) {
        if (pMaxY < pMinY) {
            int i = pMinY;
            pMinY = pMaxY;
            pMaxY = i;
        }
        GuiComponent.fill(getPoseStack(), pX, pMinY + 1, pX + 1, pMaxY, new Color(r, g, b, a).getRGB());
    }

    default void fill(int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor) {
        GuiComponent.fill(getPoseStack(), pMinX, pMinY, pMaxX, pMaxY, pColor);
    }

    default void fill( int pMinX, int pMinY, int pMaxX, int pMaxY, int r, int g, int b, int a) {
        GuiComponent.fill(getPoseStack(), pMinX, pMinY, pMaxX, pMaxY, new Color(r, g, b, a).getRGB());
    }
}
