package com.chen1335.renderjs.client.renderer;

import com.chen1335.renderjs.API.ILevelPoseHelper;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.client.renderer.state.RenderJSBlockEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.block.entity.KubeBlockEntity;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Script driven block entity renderer for the 26.1.2 two stage renderer API.
 */
public class RenderJSBlockEntityRenderer implements BlockEntityRenderer<BlockEntity, RenderJSBlockEntityRenderState> {
    public final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    public final BlockModelResolver blockModelResolver;
    public final ItemModelResolver itemModelResolver;
    public final EntityRenderDispatcher entityRenderer;
    public final EntityModelSet modelSet;
    public final Font font;
    public final SpriteGetter sprites;
    public final PlayerSkinRenderCache playerSkinRenderCache;

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
        this.blockEntityRenderDispatcher = context.blockEntityRenderDispatcher();
        this.blockModelResolver = context.blockModelResolver();
        this.itemModelResolver = context.itemModelResolver();
        this.entityRenderer = context.entityRenderer();
        this.modelSet = context.entityModelSet();
        this.font = context.font();
        this.sprites = context.sprites();
        this.playerSkinRenderCache = context.playerSkinRenderCache();
    }

    @Override
    public RenderJSBlockEntityRenderState createRenderState() {
        return new RenderJSBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(BlockEntity blockEntity, RenderJSBlockEntityRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);
        state.blockEntity = blockEntity;
        state.blockEntityJS = blockEntity instanceof KubeBlockEntity kube ? kube : null;
        state.partialTick = partialTick;
        state.cameraPosition = cameraPosition;
    }

    @HideFromJS
    @Override
    public void submit(RenderJSBlockEntityRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        customRender.accept(this, Context.context.update(this, state, poseStack, submitNodeCollector, camera));
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return false;
    }

    @Override
    public int getViewDistance() {
        return distance;
    }

    @Override
    public boolean shouldRender(@NotNull BlockEntity blockEntity, @NotNull Vec3 cameraPos) {
        return shouldRenderPredicate.test(blockEntity, cameraPos);
    }

    public boolean defaultShouldRender(BlockEntity blockEntity, Vec3 cameraPos) {
        return Vec3.atCenterOf(blockEntity.getBlockPos()).closerThan(cameraPos, this.getViewDistance());
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

    /**
     * Copies the script callbacks from a freshly rebuilt renderer into the registered one, used after a
     * script reload because block entity renderers can only be registered once.
     */
    public void copyFrom(RenderJSBlockEntityRenderer other) {
        this.customRender = other.customRender;
        this.shouldRenderPredicate = other.shouldRenderPredicate;
        this.shouldRenderOffScreen = other.shouldRenderOffScreen;
        this.distance = other.distance;
        this.renderBoundingBox = other.renderBoundingBox;
    }

    public static class Context implements ILevelRenderHelper, ILevelPoseHelper {
        public static final Context context = new Context();

        public RenderJSBlockEntityRenderer renderer;
        public BlockEntity blockEntity;
        public KubeBlockEntity blockEntityJS = null;
        public float partialTick;
        public PoseStack poseStack;
        public SubmitNodeCollector submitNodeCollector;
        public CameraRenderState camera;
        public int packedLight;
        public int packedOverlay;

        @HideFromJS
        public Context update(RenderJSBlockEntityRenderer renderer, RenderJSBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
            this.renderer = renderer;
            this.blockEntity = state.blockEntity;
            this.blockEntityJS = state.blockEntityJS;
            this.partialTick = state.partialTick;
            this.poseStack = poseStack;
            this.submitNodeCollector = submitNodeCollector;
            this.camera = camera;
            this.packedLight = state.lightCoords;
            this.packedOverlay = net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;
            return this;
        }

        @Override
        public PoseStack getPoseStack() {
            return poseStack;
        }

        @Override
        public SubmitNodeCollector getSubmitNodeCollector() {
            return submitNodeCollector;
        }

        @Info("The block entity that is currently rendered")
        public BlockEntity getBlockEntity() {
            return blockEntity;
        }

        @Info("The KubeJS block entity that is currently rendered, or null")
        public KubeBlockEntity getBlockEntityJS() {
            return blockEntityJS;
        }

        public int getPackedLight() {
            return packedLight;
        }

        public int getPackedOverlay() {
            return packedOverlay;
        }
    }
}
