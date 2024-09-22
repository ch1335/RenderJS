package com.chen1335.renderjs.API;

import com.chen1335.renderjs.client.renderer.RenderJSBlockRender;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.client.model.data.ModelData;

public interface ILevelRenderHelper {
    ;
    ILevelRenderHelper levelRenderHelper = new ILevelRenderHelper() {
        public int[] color = new int[]{255, 255, 255, 255};

        public RenderType blockRenderType = null;

        public void setColor(int r, int g, int b, int a) {
            RenderSystem.setShaderColor((float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
            color = new int[]{r, g, b, a};
        }

        @Override
        public int[] getColor() {
            return color;
        }

        @Override
        public RenderType getBlockRenderType() {
            return blockRenderType;
        }

        public void restColor() {
            RenderSystem.setShaderColor(1, 1, 1, 1);
            color = new int[]{255, 255, 255, 255};
        }

        public void restBlockRenderType() {
            blockRenderType = null;
        }

        public void setBlockRenderType(RenderType renderType) {
            this.blockRenderType = renderType;
        }


        @Info("If you want to make the block semi transparent, please use this first,and set rgba")
        public void setTranslucentBlockRenderType() {
            setBlockRenderType(RenderType.translucent());
        }

    };

    default int[] getColor() {
        return levelRenderHelper.getColor();
    }


    default void setColor(int r, int g, int b, int a) {
        levelRenderHelper.setColor(r, g, b, a);
    }

    default void restColor() {
        levelRenderHelper.setColor(255, 255, 255, 255);
    }

    default void restBlockRenderType() {
        levelRenderHelper.setBlockRenderType(null);
    }

    default void setBlockRenderType(RenderType renderType) {
        levelRenderHelper.setBlockRenderType(renderType);
    }


    default RenderType getBlockRenderType() {
        return levelRenderHelper.getBlockRenderType();
    }

    @Info("If you want to make the block semi transparent, please use this first,and set rgba")
    default void setTranslucentBlockRenderType() {
        levelRenderHelper.setBlockRenderType(RenderType.translucent());
    }

    default MultiBufferSource.BufferSource getBufferSource() {
        return Minecraft.getInstance().renderBuffers().bufferSource();
    }

    default void transformerCamera(PoseStack poseStack, Camera camera) {
        Vec3 position = camera.getPosition();
        poseStack.translate(-position.x, -position.y, -position.z);
    }

    default void transformerToBlockPose(PoseStack poseStack, BlockPos blockPos) {
        poseStack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    default void renderSingleBlock(BlockState pState, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, ModelData modelData, RenderType renderType) {
        RenderJSBlockRender.renderSingleBlock(pState, pPoseStack, pBufferSource, getColor()[0], getColor()[1], getColor()[2], getColor()[3], pPackedLight, pPackedOverlay, modelData, renderType);

    }

    default void renderSingleBlock(BlockState pState, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, ModelData modelData) {
        this.renderSingleBlock(pState, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, modelData, getBlockRenderType());
    }

    default void renderSingleBlock(BlockState pState, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        this.renderSingleBlock(pState, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, ModelData.EMPTY);
    }

    default void renderSingleBlock(BlockState pState, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
        this.renderSingleBlock(pState, pPoseStack, pBufferSource, pPackedLight, OverlayTexture.NO_OVERLAY);
    }

    default void renderSingleBlock(BlockState pState, PoseStack pPoseStack, MultiBufferSource pBufferSource) {
        this.renderSingleBlock(pState, pPoseStack, pBufferSource, LightTexture.pack(15, 15));
    }

    default void renderSingleBlock(BlockState pState, PoseStack pPoseStack) {
        this.renderSingleBlock(pState, pPoseStack, getBufferSource());
    }

    default void renderHitOutline(BlockState pState, PoseStack pPoseStack, MultiBufferSource bufferSource, Entity pEntity, BlockPos pPos) {
        LevelRenderer.renderShape(pPoseStack, bufferSource.getBuffer(RenderType.lines()), pState.getShape(pEntity.level(), pPos, CollisionContext.of(pEntity)), 0, 0, 0, 0.0F, 0.0F, 0.0F, 0.4F);
    }

    default void renderHitOutline(BlockState pState, PoseStack pPoseStack, MultiBufferSource bufferSource, Entity pEntity) {
        renderHitOutline(pState, pPoseStack, bufferSource, pEntity, BlockPos.ZERO);
    }

    default void renderHitOutline(BlockState pState, PoseStack pPoseStack, MultiBufferSource bufferSource) {
        renderHitOutline(pState, pPoseStack, bufferSource, Minecraft.getInstance().cameraEntity, BlockPos.ZERO);
    }

    default void renderHitOutline(BlockState pState, PoseStack pPoseStack) {
        renderHitOutline(pState, pPoseStack, Minecraft.getInstance().renderBuffers().bufferSource());
    }

    default void renderLevelItem(ItemStack pItemStack, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        Minecraft.getInstance().getItemRenderer().renderStatic(pItemStack, ItemDisplayContext.GROUND, pCombinedLight, pCombinedOverlay, pPoseStack, pBuffer, Minecraft.getInstance().level, 0);
    }

    default void renderLevelItem(ItemStack pItemStack, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight) {
        this.renderLevelItem(pItemStack, pPoseStack, pBuffer, pCombinedLight, OverlayTexture.NO_OVERLAY);
    }

    default void renderLevelItem(ItemStack pItemStack, PoseStack pPoseStack, MultiBufferSource pBuffer) {
        this.renderLevelItem(pItemStack, pPoseStack, pBuffer, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
    }

    default void renderLevelItem(ItemStack pItemStack, PoseStack pPoseStack) {
        this.renderLevelItem(pItemStack, pPoseStack, getBufferSource(), LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
    }

    default int packLight(Level level, BlockPos blockPos) {
        return LightTexture.pack(level.getBrightness(LightLayer.BLOCK, blockPos), level.getBrightness(LightLayer.SKY, blockPos));
    }
}
