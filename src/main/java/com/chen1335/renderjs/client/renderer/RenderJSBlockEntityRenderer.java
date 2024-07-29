package com.chen1335.renderjs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;


public class RenderJSBlockEntityRenderer implements BlockEntityRenderer<BlockEntityJS> {
    public final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    public final BlockRenderDispatcher blockRenderDispatcher;
    public final ItemRenderer itemRenderer;
    public final EntityRenderDispatcher entityRenderer;
    public final EntityModelSet modelSet;
    public final Font font;

    public static RenderJSBlockEntityRenderer create(BlockEntityRendererProvider.Context context) {
        return new RenderJSBlockEntityRenderer(context);
    }

    private int distance = 64;

    private BiPredicate<BlockEntityJS, Vec3> shouldRenderPredicate = this::defaultShouldRender;
    private BiConsumer<RenderJSBlockEntityRenderer, Context> customRender = (renderer, context) -> {
    };

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
    public void render(@NotNull BlockEntityJS pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        customRender.accept(this, Context.context.update(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay));
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull BlockEntityJS pBlockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(pBlockEntity);
    }

    @Override
    public int getViewDistance() {
        return distance;
    }

    @Override
    public boolean shouldRender(@NotNull BlockEntityJS pBlockEntity, @NotNull Vec3 pCameraPos) {
        return shouldRenderPredicate.test(pBlockEntity, pCameraPos);
    }

    public boolean defaultShouldRender(BlockEntityJS pBlockEntity, Vec3 pCameraPos) {
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


    public RenderJSBlockEntityRenderer setShouldRender(BiPredicate<BlockEntityJS, Vec3> predicate) {
        shouldRenderPredicate = predicate;
        return this;
    }

    public static class Context {
        public static final Context context = new Context();

        public BlockEntityJS blockEntityJS;
        public float partialTick;

        public PoseStack poseStack;

        public MultiBufferSource bufferSource;

        public int packedLight;

        public int packedOverlay;

        @HideFromJS
        public Context update(@NotNull BlockEntityJS pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
            blockEntityJS = pBlockEntity;
            partialTick = pPartialTick;
            poseStack = pPoseStack;
            bufferSource = pBufferSource;
            packedLight = pPackedLight;
            packedOverlay = pPackedOverlay;
            return this;
        }
    }
}
