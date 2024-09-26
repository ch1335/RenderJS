package com.chen1335.renderjs.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class RenderJSBlockEntityRenderer implements BlockEntityRenderer<BlockEntity>, RenderJSWorldRenderHelper ,BlockEntityRendererProvider<BlockEntity>{
    private Predicate<BlockEntity> shouldRenderOffScreen = blockEntity -> true;

    private int viewDistance = BlockEntityRenderer.super.getViewDistance();

    private BiPredicate<BlockEntity, Vec3> shouldRender = (blockEntity, vec3) -> true;

    private Function<BlockEntity, AABB> renderBoundingBox = BlockEntityRenderer.super::getRenderBoundingBox;

    private BiConsumer<RenderJSBlockEntityRenderer, RenderContext> customRender = (renderJSBlockEntityRenderer, renderContext1) -> {
    };
    public BlockEntityRendererProvider.Context providerContext;

    public RenderJSBlockEntityRenderer(BlockEntityRendererProvider.Context providerContext) {
        this.providerContext = providerContext;
    }

    public @NotNull RenderJSBlockEntityRenderer create(BlockEntityRendererProvider.@NotNull Context providerContext) {
        this.providerContext = providerContext;
        return this;
    }

    public static @NotNull RenderJSBlockEntityRenderer create() {
        return new RenderJSBlockEntityRenderer(null);
    }

    @Override
    public void render(@NotNull BlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        customRender.accept(this, RenderContext.getAndUpdate(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay));
    }


    public RenderJSBlockEntityRenderer setCustomRender(BiConsumer<RenderJSBlockEntityRenderer, RenderContext> customRender) {
        this.customRender = customRender;
        return this;
    }

    public RenderJSBlockEntityRenderer setShouldRenderOffScreen(Predicate<BlockEntity> predicate) {
        shouldRenderOffScreen = predicate;
        return this;
    }

    public RenderJSBlockEntityRenderer setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
        return this;
    }

    public RenderJSBlockEntityRenderer setShouldRender(BiPredicate<BlockEntity, Vec3> biPredicate) {
        this.shouldRender = biPredicate;
        return this;
    }

    @Info("Need return an AABB that controls the visible scope of this BlockEntityRenderer. Defaults to the unit cube at the given position. AABB.INFINITE can be used to declare the BER should be visible everywhere.")

    public RenderJSBlockEntityRenderer setRenderBoundingBox(Function<BlockEntity, AABB> function) {
        this.renderBoundingBox = function;
        return this;
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull BlockEntity pBlockEntity) {
        return shouldRenderOffScreen.test(pBlockEntity);
    }

    @Override
    public int getViewDistance() {
        return viewDistance;
    }

    @Override
    public boolean shouldRender(@NotNull BlockEntity pBlockEntity, @NotNull Vec3 pCameraPos) {
        return shouldRender.test(pBlockEntity, pCameraPos);
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull BlockEntity blockEntity) {
        return renderBoundingBox.apply(blockEntity);
    }

    public static class RenderContext {
        private static final RenderContext INSTANCE = new RenderContext();
        public BlockEntity blockEntity;
        public float partialTick;
        public PoseStack poseStack;
        public MultiBufferSource multiBufferSource;
        public int packedLight;
        public int packedOverlay;

        public static RenderContext getAndUpdate(@NotNull BlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
            INSTANCE.blockEntity = pBlockEntity;
            INSTANCE.partialTick = pPartialTick;
            INSTANCE.poseStack = pPoseStack;
            INSTANCE.multiBufferSource = pBufferSource;
            INSTANCE.packedLight = pPackedLight;
            INSTANCE.packedOverlay = pPackedOverlay;
            return INSTANCE;
        }
    }
}
