package com.chen1335.renderjs.client.renderer;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.block.entity.KubeBlockEntity;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;


public class RenderJSBlockEntityRenderer implements BlockEntityRenderer<BlockEntity>{
    public final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    public final BlockRenderDispatcher blockRenderDispatcher;
    public final ItemRenderer itemRenderer;
    public final EntityRenderDispatcher entityRenderer;
    public final EntityModelSet modelSet;
    public final Font font;
    public AABB renderBoundingBox = null;


    private int distance = 64;

    public BiPredicate<BlockEntity, Vec3> shouldRenderPredicate = this::defaultShouldRender;
    public BiConsumer<RenderJSBlockEntityRenderer, Context> customRender = (renderer, context) -> {
    };

    public Predicate<BlockEntity> shouldRenderOffScreen = blockEntity -> false;

    public static RenderJSBlockEntityRenderer create(BlockEntityRendererProvider.Context context) {
        return new RenderJSBlockEntityRenderer(context);
    }

    public RenderJSBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        blockEntityRenderDispatcher = context.getBlockEntityRenderDispatcher();
        blockRenderDispatcher = context.getBlockRenderDispatcher();
        itemRenderer = context.getItemRenderer();
        entityRenderer = context.getEntityRenderer();
        modelSet = context.getModelSet();
        font = context.getFont();
    }

    @HideFromJS
    @Override
    public void render(@NotNull BlockEntity blockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        customRender.accept(this, Context.context.update(blockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay));
    }


    @Override
    public boolean shouldRenderOffScreen(@NotNull BlockEntity blockEntity) {
        return shouldRenderOffScreen.test(blockEntity);
    }

    @Override
    public int getViewDistance() {
        return distance;
    }

    @Override
    public boolean shouldRender(@NotNull BlockEntity pBlockEntity, @NotNull Vec3 pCameraPos) {
        return shouldRenderPredicate.test(pBlockEntity, pCameraPos);
    }

    public boolean defaultShouldRender(BlockEntity pBlockEntity, Vec3 pCameraPos) {
        return Vec3.atCenterOf(pBlockEntity.getBlockPos()).closerThan(pCameraPos, this.getViewDistance());
    }

    public RenderJSBlockEntityRenderer setCustomRender(BiConsumer<RenderJSBlockEntityRenderer, Context> consumer) {
        customRender = consumer;
        return this;
    }

    public RenderJSBlockEntityRenderer setViewDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public RenderJSBlockEntityRenderer setShouldRender(BiPredicate<BlockEntity, Vec3> predicate) {
        shouldRenderPredicate = predicate;
        return this;
    }

    public RenderJSBlockEntityRenderer setShouldRenderOffScreen(Predicate<BlockEntity> predicate) {
        shouldRenderOffScreen = predicate;
        return this;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull BlockEntity blockEntity) {
        if (renderBoundingBox == null) {
            return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
        }
        return renderBoundingBox;
    }

    public void setInfiniteRenderBoundingBox() {
        this.renderBoundingBox = AABB.INFINITE;
    }

    public void setRenderBoundingBox(AABB renderBoundingBox) {
        this.renderBoundingBox = renderBoundingBox;
    }


    public static class Context implements ILevelRenderHelper, IGuiRenderHelper, IRenderJSPoseStackHelper {
        public static final Context context = new Context();

        private GuiGraphics guiGraphics = null;

        public BlockEntity blockEntity;

        public KubeBlockEntity blockEntityJS = null;
        public float partialTick;

        public PoseStack poseStack;

        public MultiBufferSource bufferSource;

        public int packedLight;

        public int packedOverlay;

        @HideFromJS
        public Context update(@NotNull BlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
            if (pBlockEntity instanceof KubeBlockEntity) blockEntityJS = (KubeBlockEntity) pBlockEntity;
            getGuiGraphics().pose = pPoseStack;
            blockEntity = pBlockEntity;
            partialTick = pPartialTick;
            poseStack = pPoseStack;
            bufferSource = pBufferSource;
            packedLight = pPackedLight;
            packedOverlay = pPackedOverlay;
            return this;
        }

        @Override
        public GuiGraphics getGuiGraphics() {
            if (guiGraphics == null) {
                guiGraphics = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
            }
            return guiGraphics;
        }


        @Override
        public PoseStack getPoseStack() {
            return poseStack;
        }
    }
}
