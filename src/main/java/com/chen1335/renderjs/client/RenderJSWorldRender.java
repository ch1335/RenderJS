package com.chen1335.renderjs.client;

import com.chen1335.renderjs.Renderjs;
import com.chen1335.renderjs.client.renderer.ModRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Renderjs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderJSWorldRender {
    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final RenderJSWorldRender instance = new RenderJSWorldRender();
    public static ArrayList<Consumer<RenderContext>> RENDER_LIST = new ArrayList<>();

    @HideFromJS
    public static RenderJSWorldRender getInstance() {
        return instance;
    }

    @SubscribeEvent
    public static void RenderLevelStageEvent(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            Iterator<Consumer<RenderContext>> iterator = RENDER_LIST.iterator();
            while (iterator.hasNext()) {
                iterator.next().accept(RenderContext.getInstance().setParam(event, getInstance().getRenderBuffers().bufferSource()));
            }
        }
    }

    public static void clearRender() {
        RENDER_LIST.clear();
    }

    public static void renderShape(PoseStack pPoseStack, VertexConsumer pConsumer, VoxelShape pShape, double pX, double pY, double pZ, float pRed, float pGreen, float pBlue, float pAlpha) {
        PoseStack.Pose posestack$pose = pPoseStack.last();
        pShape.forAllEdges((p_234280_, p_234281_, p_234282_, p_234283_, p_234284_, p_234285_) -> {
            float f = (float) (p_234283_ - p_234280_);
            float f1 = (float) (p_234284_ - p_234281_);
            float f2 = (float) (p_234285_ - p_234282_);
            float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
            f /= f3;
            f1 /= f3;
            f2 /= f3;
            pConsumer.vertex(posestack$pose.pose(), (float) (p_234280_ + pX), (float) (p_234281_ + pY), (float) (p_234282_ + pZ)).color(pRed, pGreen, pBlue, pAlpha).normal(posestack$pose.normal(), f, f1, f2).endVertex();
            pConsumer.vertex(posestack$pose.pose(), (float) (p_234283_ + pX), (float) (p_234284_ + pY), (float) (p_234285_ + pZ)).color(pRed, pGreen, pBlue, pAlpha).normal(posestack$pose.normal(), f, f1, f2).endVertex();
        });
    }

    public void addWorldRender(Consumer<RenderContext> consumer) {
        RENDER_LIST.add(consumer);
    }

    @Info("RenderType使用这个可以使渲染出来的方块穿透地形")
    public RenderType getOutlineTargetRenderType() {
        return ModRenderType.CUTOUT_OUTLINE_TARGET;
    }

    @Info("绘制方块(BlockPos blockPos, BlockState blockState, int BlockLight, int SkyLight,@Nullable RenderType renderType)")
    public void renderBlock1(BlockPos blockPos, BlockState blockState, int BlockLight, int SkyLight, @Nullable RenderType renderType) {
        PoseStack poseStack = RenderContext.instance.poseStack;
        Vec3 playerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        poseStack.pushPose();
        poseStack.translate(blockPos.getX() - playerPos.x, blockPos.getY() - playerPos.y, blockPos.getZ() - playerPos.z);
        minecraft.getBlockRenderer().renderSingleBlock(blockState, poseStack, getRenderBuffers().bufferSource(), LightTexture.pack(BlockLight, SkyLight), OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.ModelData.EMPTY, renderType);
        poseStack.popPose();
    }

    @Info("(PoseStack poseStack, BlockState blockState, int BlockLight, int SkyLight, @Nullable RenderType renderType)")
    public void renderBlock2(PoseStack poseStack, BlockState blockState, int BlockLight, int SkyLight, @Nullable RenderType renderType) {
        minecraft.getBlockRenderer().renderSingleBlock(blockState, poseStack, getRenderBuffers().bufferSource(), LightTexture.pack(BlockLight, SkyLight), OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.ModelData.EMPTY, renderType);
    }

    @Info("绘制方块边框线(BlockPos blockPos, BlockState blockState,@Nullable RenderType renderType)")
    public void renderBlockOutLine1(BlockPos blockPos, BlockState blockState, @Nullable RenderType renderType) {
        if (!blockState.isAir()) {
            Vec3 playerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            PoseStack poseStack = RenderContext.instance.poseStack;
            VertexConsumer vertexconsumer = this.getRenderBuffers().bufferSource().getBuffer(renderType == null ? RenderType.lines() : renderType);
            poseStack.pushPose();
            getInstance().renderHitOutline(poseStack, vertexconsumer, Minecraft.getInstance().gameRenderer.getMainCamera().getEntity(), playerPos.x, playerPos.y, playerPos.z, blockPos, blockState);
            poseStack.popPose();
        }
    }

    @Info("(PoseStack poseStack, BlockPos blockPos, BlockState blockState, @Nullable RenderType renderType)")
    public void renderBlockOutLine2(PoseStack poseStack, BlockPos blockPos, BlockState blockState, @Nullable RenderType renderType) {
        if (!blockState.isAir()) {
            Vec3 playerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            VertexConsumer vertexconsumer = this.getRenderBuffers().bufferSource().getBuffer(renderType == null ? RenderType.lines() : renderType);
            getInstance().renderHitOutline(poseStack, vertexconsumer, Minecraft.getInstance().gameRenderer.getMainCamera().getEntity(), playerPos.x, playerPos.y, playerPos.z, blockPos, blockState);
        }
    }

    public BakedModel getBlockModel(BlockState blockState) {
        return minecraft.getBlockRenderer().getBlockModel(blockState);
    }

    public BlockColors getBlockColors() {
        return minecraft.getBlockColors();
    }

    public RenderBuffers getRenderBuffers() {
        return minecraft.renderBuffers();
    }

    public ModelBlockRenderer getModelRenderer() {
        return minecraft.getBlockRenderer().getModelRenderer();
    }

    public void renderHitOutline(PoseStack pPoseStack, VertexConsumer pConsumer, Entity pEntity, double pCamX, double pCamY, double pCamZ, BlockPos pPos, BlockState pState) {
        renderShape(pPoseStack, pConsumer, pState.getShape(pEntity.level, pPos, CollisionContext.of(pEntity)), (double) pPos.getX() - pCamX, (double) pPos.getY() - pCamY, (double) pPos.getZ() - pCamZ, 0.0F, 0.0F, 0.0F, 0.4F);
    }

    public static class RenderContext {
        private static final RenderContext instance = new RenderContext();
        public MultiBufferSource.BufferSource bufferSource;
        public RenderJSWorldRender worldRender = RenderJSWorldRender.getInstance();
        public Camera camera;
        public LevelRenderer levelRenderer;
        public PoseStack poseStack;
        public Matrix4f projectionMatrix;
        public int renderTick;
        public float partialTick;
        public Frustum frustum;

        public static RenderContext getInstance() {
            return instance;
        }

        public RenderContext setParam(RenderLevelStageEvent event, MultiBufferSource.BufferSource bufferSource) {
            this.levelRenderer = event.getLevelRenderer();
            this.poseStack = event.getPoseStack();
            this.projectionMatrix = event.getProjectionMatrix();
            this.renderTick = event.getRenderTick();
            this.partialTick = event.getPartialTick();
            this.camera = event.getCamera();
            this.frustum = event.getFrustum();
            this.bufferSource = bufferSource;
            return this;
        }
    }
}
