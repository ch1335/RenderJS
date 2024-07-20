package com.chen1335.renderjs.client;

import com.chen1335.renderjs.Renderjs;
import com.chen1335.renderjs.client.renderer.ModRenderType;
import com.chen1335.renderjs.kubejs.bindings.RenderJSUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
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
    public static ArrayList<Consumer<RenderContext>> RENDER_LIST = new ArrayList<>();

    public static ArrayList<Consumer<RenderContext>> READY_RENDER_LIST = new ArrayList<>();

    private final PoseStack EmptyPoseStack = new PoseStack();

    public static boolean needReload = false;

    private Minecraft minecraft;

    @HideFromJS
    public static RenderJSWorldRender getInstance() {
        return Renderjs.worldRenderInstance;
    }

    @SubscribeEvent
    public static void RenderLevelStageEvent(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            RenderSystem.disableDepthTest();
            Iterator<Consumer<RenderContext>> consumerIterator = RENDER_LIST.iterator();
            while (consumerIterator.hasNext()) {
                if (Renderjs.CAN_RENDER) {
                    consumerIterator.next().accept(RenderContext.getContext().update(event, getInstance().getRenderBuffers().bufferSource()));
                }
            }

            if (needReload) {
                needReload=false;
                RENDER_LIST.clear();
                RENDER_LIST.addAll(READY_RENDER_LIST);
                READY_RENDER_LIST.clear();
            }
            RenderSystem.enableDepthTest();
        }
    }

    @HideFromJS
    public static void reload() {
       needReload=true;
    }

    public static void clearOld() {
        READY_RENDER_LIST.clear();
    }

    public void init() {
        minecraft = Minecraft.getInstance();
    }

    public void addWorldRender(Consumer<RenderContext> consumer) {
        READY_RENDER_LIST.add(consumer);
    }

    @Info("RenderType使用这个可以使渲染出来的方块穿透地形(BlockOutLine请使用getTopLayerLineType())\nRenderType uses this to make the rendered blocks penetrate the terrain (BlockOutLine, please use getTopLayerLineType())")
    public RenderType getTopLayerType() {
        return ModRenderType.TOP_LAYER_TARGET;
    }

    @Info("BlockOutLine的RenderType使用这个可以使渲染出来的方块穿透地形\nThe RenderType of BlockOutLine can be used to make the rendered blocks penetrate the terrain")
    public RenderType getTopLayerLineType() {
        return ModRenderType.TOP_LAYER_LINE_TARGET;
    }

    @Info(value = "绘制方块", params = {@Param(name = "renderType", value = "@Nullable")})
    public void renderBlock1(BlockPos blockPos, BlockState blockState, int BlockLight, int SkyLight, @Nullable RenderType renderType) {
        MultiBufferSource.BufferSource bufferSource = getRenderBuffers().bufferSource();
        PoseStack poseStack = RenderContext.getContext().poseStack;
        Vec3 playerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        poseStack.pushPose();
        poseStack.translate(blockPos.getX() - playerPos.x, blockPos.getY() - playerPos.y, blockPos.getZ() - playerPos.z);
        minecraft.getBlockRenderer().renderSingleBlock(blockState, poseStack, bufferSource, LightTexture.pack(BlockLight, SkyLight), OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.ModelData.EMPTY, renderType);
        poseStack.popPose();
    }

    @Info(value = "绘制方块", params = {@Param(name = "renderType", value = "@Nullable")})
    public void renderBlock2(PoseStack poseStack, BlockState blockState, int BlockLight, int SkyLight, @Nullable RenderType renderType) {
        minecraft.getBlockRenderer().renderSingleBlock(blockState, poseStack, getRenderBuffers().bufferSource(), LightTexture.pack(BlockLight, SkyLight), OverlayTexture.NO_OVERLAY, net.minecraftforge.client.model.data.ModelData.EMPTY, renderType);
    }

    @Info(value = "绘制方块边框线", params = {@Param(name = "renderType", value = "@Nullable")})
    public void renderBlockOutLine1(BlockPos blockPos, BlockState blockState, @Nullable RenderType renderType) {
        if (!blockState.isAir()) {
            MultiBufferSource.BufferSource bufferSource = getRenderBuffers().bufferSource();
            Vec3 playerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            PoseStack poseStack = RenderContext.getContext().poseStack;
            RenderType renderType1 = renderType == null ? RenderType.lines() : renderType;
            VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType1);
            poseStack.pushPose();
            getInstance().renderHitOutline(poseStack, vertexconsumer, Minecraft.getInstance().gameRenderer.getMainCamera().getEntity(), playerPos.x, playerPos.y, playerPos.z, blockPos, blockState);
            poseStack.popPose();
            bufferSource.endBatch(renderType1);
        }
    }


    @Info(value = "绘制方块边框线", params = {@Param(name = "renderType", value = "@Nullable")})
    public void renderBlockOutLine2(PoseStack poseStack, BlockPos blockPos, BlockState blockState, @Nullable RenderType renderType) {
        if (!blockState.isAir()) {
            MultiBufferSource.BufferSource bufferSource = getRenderBuffers().bufferSource();
            Vec3 playerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            RenderType renderType1 = renderType == null ? RenderType.lines() : renderType;
            VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType1);
            getInstance().renderHitOutline(poseStack, vertexconsumer, Minecraft.getInstance().gameRenderer.getMainCamera().getEntity(), playerPos.x, playerPos.y, playerPos.z, blockPos, blockState);
        }
    }


    @Info("渲染物品\nRender item in word")
    public void renderItem(PoseStack poseStack, ItemStack itemStack, int PositionLight, int SkyLight) {
        MultiBufferSource.BufferSource bufferSource = getRenderBuffers().bufferSource();
        minecraft.getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.GROUND, LightTexture.pack(PositionLight, SkyLight), OverlayTexture.NO_OVERLAY, poseStack, bufferSource, minecraft.player.getId());
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
        LevelRenderer.renderShape(pPoseStack, pConsumer, pState.getShape(pEntity.level, pPos, CollisionContext.of(pEntity)), (double) pPos.getX() - pCamX, (double) pPos.getY() - pCamY, (double) pPos.getZ() - pCamZ, 0.0F, 0.0F, 0.0F, 0.4F);
    }

    public static class RenderContext {
        private static final RenderContext context = new RenderContext();
        public MultiBufferSource.BufferSource bufferSource;
        public RenderJSWorldRender worldRender = RenderJSWorldRender.getInstance();
        public Camera camera;
        public LevelRenderer levelRenderer;
        public PoseStack poseStack;
        public Matrix4f projectionMatrix;
        public int renderTick;
        public float partialTick;
        public Frustum frustum;

        public static RenderContext getContext() {
            return context;
        }

        public RenderContext update(RenderLevelStageEvent event, MultiBufferSource.BufferSource bufferSource) {
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
