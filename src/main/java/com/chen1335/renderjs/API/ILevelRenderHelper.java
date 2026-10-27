package com.chen1335.renderjs.API;

import com.chen1335.renderjs.client.renderer.RenderJSBlockRender;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * World space rendering helper. MC 26.1.2 renders through a {@link SubmitNodeCollector}, so the
 * context that implements this interface must provide one via {@link #getSubmitNodeCollector()}.
 * <p>
 * Overloads are arranged so that every method name has a unique parameter count (2, 3, 4, ...),
 * because KubeJS/Rhino cannot reliably pick between overloads with the same arity.
 */
@Info("Drawing helpers for world, entity and block entity rendering. Only usable from a render event callback.")
public interface ILevelRenderHelper {
    PoseStack getPoseStack();

    SubmitNodeCollector getSubmitNodeCollector();

    @Info(value = "Translates the pose stack into camera relative space.",
            params = {
                    @Param(name = "poseStack", value = "Pose stack to transform."),
                    @Param(name = "camera", value = "Camera whose position is subtracted.")
            })
    default void transformerCamera(PoseStack poseStack, Camera camera) {
        Vec3 position = camera.position();
        poseStack.translate(-position.x, -position.y, -position.z);
    }

    @Info(value = "Translates the pose stack to the given block position.",
            params = {
                    @Param(name = "poseStack", value = "Pose stack to transform."),
                    @Param(name = "blockPos", value = "Block position to translate to.")
            })
    default void transformerToBlockPose(PoseStack poseStack, BlockPos blockPos) {
        poseStack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Info(value = "Renders a block model without tinting.",
            params = {
                    @Param(name = "blockState", value = "Block state to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the model.")
            })
    default void renderSingleBlock(BlockState blockState, PoseStack poseStack) {
        renderSingleBlock(blockState, poseStack, -1, null, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    @Info(value = "Renders a block model tinted by the given ARGB color.",
            params = {
                    @Param(name = "blockState", value = "Block state to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the model."),
                    @Param(name = "argbColor", value = "Tint color in 0xAARRGGBB format. Use -1 for no tint.")
            })
    default void renderSingleBlock(BlockState blockState, PoseStack poseStack, long argbColor) {
        renderSingleBlock(blockState, poseStack, argbColor, null, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    @Info(value = "Renders a tinted block model with an explicit render type.",
            params = {
                    @Param(name = "blockState", value = "Block state to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the model."),
                    @Param(name = "argbColor", value = "Tint color in 0xAARRGGBB format. Use -1 for no tint."),
                    @Param(name = "renderType", value = "Render type controlling depth and blending. May be null to use the default block render type.")
            })
    default void renderSingleBlock(BlockState blockState, PoseStack poseStack, long argbColor, @Nullable RenderType renderType) {
        renderSingleBlock(blockState, poseStack, argbColor, renderType, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    @Info(value = "Renders a tinted block model with an explicit render type and packed light.",
            params = {
                    @Param(name = "blockState", value = "Block state to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the model."),
                    @Param(name = "argbColor", value = "Tint color in 0xAARRGGBB format. Use -1 for no tint."),
                    @Param(name = "renderType", value = "Render type controlling depth and blending. May be null to use the default block render type."),
                    @Param(name = "packedLight", value = "Packed block and sky light coordinates.")
            })
    default void renderSingleBlock(BlockState blockState, PoseStack poseStack, long argbColor, @Nullable RenderType renderType, int packedLight) {
        renderSingleBlock(blockState, poseStack, argbColor, renderType, packedLight, OverlayTexture.NO_OVERLAY);
    }

    @Info(value = "Renders a tinted block model with explicit render type, packed light and packed overlay.",
            params = {
                    @Param(name = "blockState", value = "Block state to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the model."),
                    @Param(name = "argbColor", value = "Tint color in 0xAARRGGBB format. Use -1 for no tint."),
                    @Param(name = "renderType", value = "Render type controlling depth and blending. May be null to use the default block render type."),
                    @Param(name = "packedLight", value = "Packed block and sky light coordinates."),
                    @Param(name = "packedOverlay", value = "Packed overlay texture coordinates.")
            })
    default void renderSingleBlock(BlockState blockState, PoseStack poseStack, long argbColor, @Nullable RenderType renderType, int packedLight, int packedOverlay) {
        RenderJSBlockRender.renderSingleBlockWithColor(blockState, poseStack, getSubmitNodeCollector(), renderType, (int) argbColor, packedLight, packedOverlay);
    }

    @Info(value = "Renders a block model with an explicit render type but no tinting.",
            params = {
                    @Param(name = "blockState", value = "Block state to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the model."),
                    @Param(name = "renderType", value = "Render type controlling depth and blending.")
            })
    default void renderSingleBlockWithType(BlockState blockState, PoseStack poseStack, @Nullable RenderType renderType) {
        renderSingleBlockWithType(blockState, poseStack, renderType, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    @Info(value = "Renders an untinted block model with an explicit render type and packed light.",
            params = {
                    @Param(name = "blockState", value = "Block state to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the model."),
                    @Param(name = "renderType", value = "Render type controlling depth and blending."),
                    @Param(name = "packedLight", value = "Packed block and sky light coordinates.")
            })
    default void renderSingleBlockWithType(BlockState blockState, PoseStack poseStack, @Nullable RenderType renderType, int packedLight) {
        renderSingleBlockWithType(blockState, poseStack, renderType, packedLight, OverlayTexture.NO_OVERLAY);
    }

    @Info(value = "Renders an untinted block model with explicit render type, packed light and packed overlay.",
            params = {
                    @Param(name = "blockState", value = "Block state to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the model."),
                    @Param(name = "renderType", value = "Render type controlling depth and blending."),
                    @Param(name = "packedLight", value = "Packed block and sky light coordinates."),
                    @Param(name = "packedOverlay", value = "Packed overlay texture coordinates.")
            })
    default void renderSingleBlockWithType(BlockState blockState, PoseStack poseStack, @Nullable RenderType renderType, int packedLight, int packedOverlay) {
        RenderJSBlockRender.renderSingleBlockWithType(blockState, poseStack, getSubmitNodeCollector(), renderType, packedLight, packedOverlay);
    }

    @Info(value = "Draws the outline of a block with the default color.",
            params = {
                    @Param(name = "blockState", value = "Block state whose shape is drawn."),
                    @Param(name = "poseStack", value = "Pose stack that positions the outline.")
            })
    default void renderHitOutline(BlockState blockState, PoseStack poseStack) {
        renderHitOutline(blockState, poseStack, 0x66FFFFFF, null);
    }

    @Info(value = "Draws the outline of a block with the given ARGB color.",
            params = {
                    @Param(name = "blockState", value = "Block state whose shape is drawn."),
                    @Param(name = "poseStack", value = "Pose stack that positions the outline."),
                    @Param(name = "argbColor", value = "Outline color in 0xAARRGGBB format.")
            })
    default void renderHitOutline(BlockState blockState, PoseStack poseStack, long argbColor) {
        renderHitOutline(blockState, poseStack, argbColor, null);
    }

    @Info(value = "Draws the outline of a block with an explicit color and render type.",
            params = {
                    @Param(name = "blockState", value = "Block state whose shape is drawn."),
                    @Param(name = "poseStack", value = "Pose stack that positions the outline."),
                    @Param(name = "argbColor", value = "Outline color in 0xAARRGGBB format."),
                    @Param(name = "renderType", value = "Render type controlling depth and blending. May be null to use the default line render type.")
            })
    default void renderHitOutline(BlockState blockState, PoseStack poseStack, long argbColor, @Nullable RenderType renderType) {
        RenderJSBlockRender.renderHitOutline(blockState, poseStack, getSubmitNodeCollector(), Minecraft.getInstance().getCameraEntity(), BlockPos.ZERO, renderType, (int) argbColor);
    }

    @Info(value = "Draws the outline of a block with the default color and an explicit render type.",
            params = {
                    @Param(name = "blockState", value = "Block state whose shape is drawn."),
                    @Param(name = "poseStack", value = "Pose stack that positions the outline."),
                    @Param(name = "renderType", value = "Render type controlling depth and blending. May be null to use the default line render type.")
            })
    default void renderHitOutlineWithType(BlockState blockState, PoseStack poseStack, @Nullable RenderType renderType) {
        RenderJSBlockRender.renderHitOutline(blockState, poseStack, getSubmitNodeCollector(), Minecraft.getInstance().getCameraEntity(), BlockPos.ZERO, renderType, 0x66FFFFFF);
    }

    @Info(value = "Renders an item stack in the world using the GROUND display context.",
            params = {
                    @Param(name = "itemStack", value = "Item stack to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the item.")
            })
    default void renderLevelItem(ItemStack itemStack, PoseStack poseStack) {
        renderLevelItem(itemStack, poseStack, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    @Info(value = "Renders an item stack in the world with an explicit packed light.",
            params = {
                    @Param(name = "itemStack", value = "Item stack to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the item."),
                    @Param(name = "packedLight", value = "Packed block and sky light coordinates.")
            })
    default void renderLevelItem(ItemStack itemStack, PoseStack poseStack, int packedLight) {
        renderLevelItem(itemStack, poseStack, packedLight, OverlayTexture.NO_OVERLAY);
    }

    @Info(value = "Renders an item stack in the world with explicit packed light and packed overlay.",
            params = {
                    @Param(name = "itemStack", value = "Item stack to render."),
                    @Param(name = "poseStack", value = "Pose stack that positions the item."),
                    @Param(name = "packedLight", value = "Packed block and sky light coordinates."),
                    @Param(name = "packedOverlay", value = "Packed overlay texture coordinates.")
            })
    default void renderLevelItem(ItemStack itemStack, PoseStack poseStack, int packedLight, int packedOverlay) {
        RenderJSBlockRender.renderLevelItem(itemStack, poseStack, getSubmitNodeCollector(), packedLight, packedOverlay);
    }

    @Info(value = "Packs the block and sky light levels of the given position into packed light coordinates.",
            params = {
                    @Param(name = "level", value = "Level to query light from."),
                    @Param(name = "blockPos", value = "Position to query light at.")
            })
    default int packLight(Level level, BlockPos blockPos) {
        return LightCoordsUtil.pack(level.getBrightness(LightLayer.BLOCK, blockPos), level.getBrightness(LightLayer.SKY, blockPos));
    }
}