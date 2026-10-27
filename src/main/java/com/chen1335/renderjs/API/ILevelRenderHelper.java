package com.chen1335.renderjs.API;

import com.chen1335.renderjs.client.renderer.RenderJSBlockRender;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * World space rendering helper. MC 26.1.2 renders through a {@link SubmitNodeCollector}, so the
 * context that implements this interface must provide one via {@link #getSubmitNodeCollector()}.
 * <p>
 * There is no global color/RenderType state anymore: pass a {@link RenderType} per call to control
 * depth/layering (see {@code renderSingleBlock(blockState, poseStack, renderType)}).
 */
public interface ILevelRenderHelper {
    @HideFromJS
    PoseStack getPoseStack();

    @HideFromJS
    SubmitNodeCollector getSubmitNodeCollector();

    default void transformerCamera(PoseStack poseStack, Camera camera) {
        Vec3 position = camera.position();
        poseStack.translate(-position.x, -position.y, -position.z);
    }

    default void transformerToBlockPose(PoseStack poseStack, BlockPos blockPos) {
        poseStack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    default void renderSingleBlock(BlockState blockState, PoseStack poseStack) {
        renderSingleBlock(blockState, poseStack, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    default void renderSingleBlock(BlockState blockState, PoseStack poseStack, int packedLight, int packedOverlay) {
        RenderJSBlockRender.renderSingleBlock(blockState, poseStack, getSubmitNodeCollector(), packedLight, packedOverlay);
    }

    /**
     * Renders a block with an explicit RenderType, e.g. {@code RenderJSRenderType.TOP_LAYER_TARGET}
     * to draw it through terrain (that render type uses a no-depth pipeline).
     */
    default void renderSingleBlock(BlockState blockState, PoseStack poseStack, RenderType renderType) {
        renderSingleBlock(blockState, poseStack, renderType, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    default void renderSingleBlock(BlockState blockState, PoseStack poseStack, RenderType renderType, int packedLight, int packedOverlay) {
        RenderJSBlockRender.renderSingleBlockWithType(blockState, poseStack, getSubmitNodeCollector(), renderType, packedLight, packedOverlay);
    }

    default void renderHitOutline(BlockState blockState, PoseStack poseStack, Entity entity, BlockPos blockPos) {
        RenderJSBlockRender.renderHitOutline(blockState, poseStack, getSubmitNodeCollector(), entity, blockPos);
    }

    default void renderHitOutline(BlockState blockState, PoseStack poseStack, Entity entity) {
        renderHitOutline(blockState, poseStack, entity, BlockPos.ZERO);
    }

    default void renderHitOutline(BlockState blockState, PoseStack poseStack) {
        renderHitOutline(blockState, poseStack, Minecraft.getInstance().getCameraEntity(), BlockPos.ZERO);
    }

    default void renderHitOutline(BlockState blockState, PoseStack poseStack, RenderType renderType) {
        renderHitOutline(blockState, poseStack, Minecraft.getInstance().getCameraEntity(), BlockPos.ZERO, renderType);
    }

    default void renderHitOutline(BlockState blockState, PoseStack poseStack, Entity entity, BlockPos blockPos, RenderType renderType) {
        RenderJSBlockRender.renderHitOutline(blockState, poseStack, getSubmitNodeCollector(), entity, blockPos, renderType);
    }

    default void renderLevelItem(ItemStack itemStack, PoseStack poseStack, int packedLight, int packedOverlay) {
        RenderJSBlockRender.renderLevelItem(itemStack, poseStack, getSubmitNodeCollector(), packedLight, packedOverlay);
    }

    default void renderLevelItem(ItemStack itemStack, PoseStack poseStack, int packedLight) {
        renderLevelItem(itemStack, poseStack, packedLight, OverlayTexture.NO_OVERLAY);
    }

    default void renderLevelItem(ItemStack itemStack, PoseStack poseStack) {
        renderLevelItem(itemStack, poseStack, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    default int packLight(Level level, BlockPos blockPos) {
        return LightCoordsUtil.pack(level.getBrightness(LightLayer.BLOCK, blockPos), level.getBrightness(LightLayer.SKY, blockPos));
    }
}
