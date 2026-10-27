package com.chen1335.renderjs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import java.util.List;

/**
 * Engine bridge for the 26.1.2 submit based renderer.
 * <p>
 * Everything is submitted into a {@link SubmitNodeCollector} instead of being written into a
 * {@code MultiBufferSource} immediately. The {@code ...WithType} variants allow rendering a block model
 * with a custom {@link RenderType}, which is how depth/layering is controlled in 26.1.2.
 */
public class RenderJSBlockRender {
    private static BlockModelResolver blockModelResolver;
    private static ItemModelResolver itemModelResolver;

    public static BlockModelResolver getBlockModelResolver() {
        if (blockModelResolver == null) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.getModelManager() == null) {
                return null;
            }
            blockModelResolver = new BlockModelResolver(minecraft.getModelManager());
        }
        return blockModelResolver;
    }

    public static ItemModelResolver getItemModelResolver() {
        if (itemModelResolver == null) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.getModelManager() == null) {
                return null;
            }
            itemModelResolver = new ItemModelResolver(minecraft.getModelManager());
        }
        return itemModelResolver;
    }

    private static BlockModel getBlockModel(BlockState blockState) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getModelManager() == null) {
            return null;
        }
        return minecraft.getModelManager().getBlockModelSet().get(blockState);
    }

    public static void renderSingleBlock(BlockState blockState, PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int packedOverlay) {
        BlockModelResolver resolver = getBlockModelResolver();
        if (resolver == null || collector == null) {
            return;
        }
        BlockModelRenderState renderState = new BlockModelRenderState();
        resolver.update(renderState, blockState, BlockDisplayContext.create());
        renderState.submitMultiLayer(poseStack, collector, packedLight, packedOverlay, 0);
    }

    /**
     * Renders a block model with an explicit {@link RenderType} (for example a no-depth render type so the
     * block is visible through terrain). Falls back to the default block render type for special models
     * (chests, banners, ...) which cannot be expressed as plain model parts.
     */
    public static void renderSingleBlockWithType(BlockState blockState, PoseStack poseStack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {
        if (renderType == null || collector == null) {
            renderSingleBlock(blockState, poseStack, collector, packedLight, packedOverlay);
            return;
        }
        BlockModel model = getBlockModel(blockState);
        if (model == null) {
            renderSingleBlock(blockState, poseStack, collector, packedLight, packedOverlay);
            return;
        }

        CapturingBlockModelRenderState captured = new CapturingBlockModelRenderState();
        model.update(captured, blockState, BlockDisplayContext.create(), 42L);
        List<BlockStateModelPart> parts = captured.capturedParts;
        if (parts == null || parts.isEmpty()) {
            // special models (chest/banner/...) have no model parts, fall back to the default path
            renderSingleBlock(blockState, poseStack, collector, packedLight, packedOverlay);
            return;
        }

        int[] tints = captured.tintLayers().toArray(BlockModelRenderState.EMPTY_TINTS);
        collector.submitBlockModel(poseStack, renderType, parts, tints, packedLight, packedOverlay, 0);
    }

    /**
     * Renders a block model tinted by a single ARGB color. Unlike {@code tintLayers} (which only affects
     * quads that declare a tint index) this multiplies the color into every quad, giving a real whole-model
     * tint. Special models (chest/banner/...) have no plain model parts and fall back to the normal path.
     */
    public static void renderSingleBlockWithColor(BlockState blockState, PoseStack poseStack, SubmitNodeCollector collector, RenderType renderType, int argbColor, int packedLight, int packedOverlay) {
        if (collector == null) {
            return;
        }
        BlockModel model = getBlockModel(blockState);
        if (model == null) {
            renderSingleBlock(blockState, poseStack, collector, packedLight, packedOverlay);
            return;
        }

        CapturingBlockModelRenderState captured = new CapturingBlockModelRenderState();
        model.update(captured, blockState, BlockDisplayContext.create(), 42L);
        List<BlockStateModelPart> parts = captured.capturedParts;
        if (parts == null || parts.isEmpty()) {
            renderSingleBlock(blockState, poseStack, collector, packedLight, packedOverlay);
            return;
        }

        int[] tintLayers = captured.tintLayers().toArray(BlockModelRenderState.EMPTY_TINTS);
        RenderType type = renderType != null ? renderType : defaultBlockSheet(blockState);
        collector.submitCustomGeometry(poseStack, type, (pose, buffer) -> {
            QuadInstance instance = new QuadInstance();
            instance.setLightCoords(packedLight);
            instance.setOverlayCoords(packedOverlay);
            for (BlockStateModelPart part : parts) {
                for (Direction direction : Direction.values()) {
                    putColoredQuads(part.getQuads(direction), pose, buffer, instance, tintLayers, argbColor);
                }
                putColoredQuads(part.getQuads(null), pose, buffer, instance, tintLayers, argbColor);
            }
        });
    }

    private static void putColoredQuads(List<BakedQuad> quads, PoseStack.Pose pose, VertexConsumer buffer, QuadInstance instance, int[] tintLayers, int argbColor) {
        for (BakedQuad quad : quads) {
            int tintIndex = quad.materialInfo().tintIndex();
            int baseColor = tintIndex != -1 && tintIndex < tintLayers.length ? tintLayers[tintIndex] : -1;
            instance.setColor(ARGB.multiply(baseColor, argbColor));
            buffer.putBakedQuad(pose, quad, instance);
        }
    }

    private static RenderType defaultBlockSheet(BlockState blockState) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getModelManager() != null) {
            boolean translucent = minecraft.getModelManager().getBlockStateModelSet()
                    .get(blockState)
                    .hasMaterialFlag(BlockAndTintGetter.EMPTY, BlockPos.ZERO, blockState, 1);
            return translucent ? Sheets.translucentBlockSheet() : Sheets.cutoutBlockSheet();
        }
        return Sheets.cutoutBlockSheet();
    }

    public static void renderLevelItem(ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int packedOverlay) {
        ItemModelResolver resolver = getItemModelResolver();
        if (resolver == null || collector == null || itemStack.isEmpty()) {
            return;
        }
        ItemStackRenderState renderState = new ItemStackRenderState();
        resolver.updateForTopItem(renderState, itemStack, ItemDisplayContext.GROUND, Minecraft.getInstance().level, null, 0);
        renderState.submit(poseStack, collector, packedLight, packedOverlay, 0);
    }

    public static void renderHitOutline(BlockState blockState, PoseStack poseStack, SubmitNodeCollector collector, Entity entity, BlockPos blockPos) {
        renderHitOutline(blockState, poseStack, collector, entity, blockPos, RenderTypes.lines());
    }

    public static void renderHitOutline(BlockState blockState, PoseStack poseStack, SubmitNodeCollector collector, Entity entity, BlockPos blockPos, RenderType renderType) {
        renderHitOutline(blockState, poseStack, collector, entity, blockPos, renderType, 0x66FFFFFF);
    }

    public static void renderHitOutline(BlockState blockState, PoseStack poseStack, SubmitNodeCollector collector, Entity entity, BlockPos blockPos, RenderType renderType, int argbColor) {
        if (collector == null || entity == null) {
            return;
        }
        if (renderType == null) {
            renderType = RenderTypes.lines();
        }
        VoxelShape shape = blockState.getShape(entity.level(), blockPos, CollisionContext.of(entity));
        int color = argbColor;
        float width = 1.0F;
        RenderType finalRenderType = renderType;
        collector.submitCustomGeometry(poseStack, finalRenderType, (pose, buffer) -> shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            Vector3f normal = new Vector3f((float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1)).normalize();
            buffer.addVertex(pose, (float) x1, (float) y1, (float) z1).setColor(color).setNormal(pose, normal).setLineWidth(width);
            buffer.addVertex(pose, (float) x2, (float) y2, (float) z2).setColor(color).setNormal(pose, normal).setLineWidth(width);
        }));
    }

    /**
     * Captures the model part list produced by {@link BlockModelRenderState#setupModel(Matrix4fc, boolean)}
     * so it can be submitted with a custom render type.
     */
    private static final class CapturingBlockModelRenderState extends BlockModelRenderState {
        private List<BlockStateModelPart> capturedParts;

        @Override
        public List<BlockStateModelPart> setupModel(Matrix4fc transformation, boolean hasTranslucency) {
            this.capturedParts = super.setupModel(transformation, hasTranslucency);
            return this.capturedParts;
        }
    }
}
