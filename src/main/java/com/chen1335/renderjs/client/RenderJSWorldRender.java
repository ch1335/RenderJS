package com.chen1335.renderjs.client;

import com.chen1335.renderjs.Renderjs;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import dev.latvian.mods.kubejs.typings.Info;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Renderjs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderJSWorldRender {

    private static final Minecraft minecraft = Minecraft.getInstance();
    public RenderBuffers renderBuffers;
    public static boolean isReload = false;
    private static final RenderJSWorldRender instance = new RenderJSWorldRender();

    public static RenderJSWorldRender getInstance() {
        return instance;
    }

    public static ArrayList<Consumer<RenderContext>> RENDER_LIST = new ArrayList<>();

    public Camera camera;

    public Vec3 cameraPosition;
    public double cameraX;
    public double cameraY;
    public double cameraZ;

    public void init() {
        this.renderBuffers = minecraft.renderBuffers();
    }

    @SubscribeEvent
    public static void RenderLevelStageEvent(RenderLevelStageEvent event) {
        instance.render(RenderContext.getInstance().setParam(event, getInstance().renderBuffers.bufferSource()));
    }

    public void render(RenderContext renderContext) {
        this.camera = renderContext.camera;
        this.cameraPosition = renderContext.camera.getPosition();
        this.cameraX = cameraPosition.x;
        this.cameraY = cameraPosition.y;
        this.cameraZ = cameraPosition.z;


        RenderLevelStageEvent.Stage stage = renderContext.stage;
        if (stage == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {

            BlockState blockState = Blocks.STONE.defaultBlockState();
            RenderSystem.disableDepthTest();
            this.renderBlock1(new BlockPos(0, 0, 0), blockState, 15, 15);
            RenderSystem.enableDepthTest();
        }
        if (stage == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
        }
    }


    public void addWorldRender() {

    }


    public void test(BlockPos blockPos){
        PoseStack poseStack = RenderContext.instance.poseStack;
        poseStack.pushPose();
        poseStack.translate(blockPos.getX() - cameraX, blockPos.getY() - cameraY, blockPos.getZ() - cameraZ);

        poseStack.popPose();
    }
    @Info("绘制方块")
    public void renderBlock1(BlockPos blockPos, BlockState blockState, int BlockLight, int SkyLight) {
        PoseStack poseStack = RenderContext.instance.poseStack;
        poseStack.pushPose();
        poseStack.translate(blockPos.getX() - cameraX, blockPos.getY() - cameraY, blockPos.getZ() - cameraZ);
        minecraft.getBlockRenderer().renderSingleBlock(blockState, poseStack, getRenderBuffers().bufferSource(), LightTexture.pack(BlockLight, SkyLight), OverlayTexture.NO_OVERLAY,net.minecraftforge.client.model.data.ModelData.EMPTY,null);
        poseStack.popPose();
    }
    public void renderBlock2(PoseStack poseStack, BlockState blockState, int BlockLight, int SkyLight) {
        minecraft.getBlockRenderer().renderSingleBlock(blockState, poseStack, getRenderBuffers().bufferSource(), LightTexture.pack(BlockLight, SkyLight), OverlayTexture.NO_OVERLAY,net.minecraftforge.client.model.data.ModelData.EMPTY,null);
    }
    @Info("绘制方块边框线")
    public void renderBlockOutLine1(BlockPos blockPos, BlockState blockState) {
        if (!blockState.isAir()) {
            PoseStack poseStack = RenderContext.instance.poseStack;
            VertexConsumer vertexconsumer = this.getRenderBuffers().bufferSource().getBuffer(RenderType.lines());
            poseStack.pushPose();
            getInstance().renderHitOutline(poseStack, vertexconsumer, this.camera.getEntity(), cameraX, cameraY, cameraZ, blockPos, blockState);
            poseStack.popPose();
        }
    }
    public void renderBlockOutLine2(PoseStack poseStack,BlockPos blockPos, BlockState blockState) {
        if (!blockState.isAir()) {
            VertexConsumer vertexconsumer = this.getRenderBuffers().bufferSource().getBuffer(RenderType.lines());
            getInstance().renderHitOutline(poseStack, vertexconsumer, this.camera.getEntity(), cameraX, cameraY, cameraZ, blockPos, blockState);
        }
    }
    public BakedModel getBlockModel(BlockState blockState){
        return minecraft.getBlockRenderer().getBlockModel(blockState);
    }
    public BlockColors getBlockColors() {
        return minecraft.getBlockColors();
    }

    public RenderBuffers getRenderBuffers(){
        return minecraft.renderBuffers();
    }
    public ModelBlockRenderer getModelRenderer(){
        return minecraft.getBlockRenderer().getModelRenderer();
    }
    public void renderHitOutline(PoseStack pPoseStack, VertexConsumer pConsumer, Entity pEntity, double pCamX, double pCamY, double pCamZ, BlockPos pPos, BlockState pState) {
        renderShape(pPoseStack, pConsumer, pState.getShape(pEntity.level, pPos, CollisionContext.of(pEntity)), (double) pPos.getX() - pCamX, (double) pPos.getY() - pCamY, (double) pPos.getZ() - pCamZ, 0.0F, 0.0F, 0.0F, 0.4F);
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

    public static class RenderContext {
        private static final RenderContext instance = new RenderContext();
        public RenderLevelStageEvent.Stage stage;
        public MultiBufferSource.BufferSource bufferSource;

        public static RenderContext getInstance() {
            return instance;
        }

        public Camera camera;
        public LevelRenderer levelRenderer;
        public PoseStack poseStack;
        public Matrix4f projectionMatrix;
        public int renderTick;
        public float partialTick;
        public Frustum frustum;


        public RenderContext setParam(RenderLevelStageEvent event, MultiBufferSource.BufferSource bufferSource) {
            this.levelRenderer = event.getLevelRenderer();
            this.poseStack = event.getPoseStack();
            this.projectionMatrix = event.getProjectionMatrix();
            this.renderTick = event.getRenderTick();
            this.partialTick = event.getPartialTick();
            this.camera = event.getCamera();
            this.frustum = event.getFrustum();
            this.stage = event.getStage();
            this.bufferSource = bufferSource;
            return this;
        }
    }
}
