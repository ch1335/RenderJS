package com.chen1335.renderjs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.client.renderer.LevelRenderer.DIRECTIONS;

public class RenderJSBlockRender {
    public static void renderSingleBlock(BlockState pState, PoseStack pPoseStack, MultiBufferSource pBufferSource, int r, int g, int b, int a, int pPackedLight, int pPackedOverlay, net.minecraftforge.client.model.data.ModelData modelData, net.minecraft.client.renderer.RenderType renderType) {
        RenderShape rendershape = pState.getRenderShape();
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        if (rendershape != RenderShape.INVISIBLE) {
            switch (rendershape) {
                case MODEL:
                    BakedModel bakedmodel = blockRenderDispatcher.getBlockModel(pState);
                    for (net.minecraft.client.renderer.RenderType rt : bakedmodel.getRenderTypes(pState, RandomSource.create(42), modelData))
                        renderModel(pPoseStack.last(), pBufferSource.getBuffer(renderType != null ? renderType : net.minecraftforge.client.RenderTypeHelper.getEntityRenderType(rt, false)), pState, bakedmodel, (float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255, pPackedLight, pPackedOverlay, modelData, rt);
                    break;
                case ENTITYBLOCK_ANIMATED:
                    ItemStack stack = new ItemStack(pState.getBlock());
                    net.minecraftforge.client.extensions.common.IClientItemExtensions.of(stack).getCustomRenderer().renderByItem(stack, ItemTransforms.TransformType.NONE, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
            }
        }
    }

    public static void renderModel(PoseStack.Pose pPose, VertexConsumer pConsumer, @Nullable BlockState pState, BakedModel pModel, float r, float g, float b, float a, int pPackedLight, int pPackedOverlay, net.minecraftforge.client.model.data.ModelData modelData, net.minecraft.client.renderer.RenderType renderType) {
        RandomSource randomsource = RandomSource.create();
        long i = 42L;

        for (Direction direction : DIRECTIONS) {
            randomsource.setSeed(42L);
            renderQuadList(pPose, pConsumer, r, g, b, a, pModel.getQuads(pState, direction, randomsource, modelData, renderType), pPackedLight, pPackedOverlay);
        }

        randomsource.setSeed(42L);
        renderQuadList(pPose, pConsumer, r, g, b, a, pModel.getQuads(pState, null, randomsource, modelData, renderType), pPackedLight, pPackedOverlay);
    }

    private static void renderQuadList(PoseStack.Pose pPose, VertexConsumer pConsumer, float r, float g, float b, float a, List<BakedQuad> pQuads, int pPackedLight, int pPackedOverlay) {
        for (BakedQuad bakedquad : pQuads) {
            pConsumer.putBulkData(pPose, bakedquad, new float[]{1.0F, 1.0F, 1.0F, 1.0F}, r, g, b, a, new int[]{pPackedLight, pPackedLight, pPackedLight, pPackedLight}, pPackedOverlay, false);
        }
    }
}
