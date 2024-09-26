package com.chen1335.renderjs.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public interface RenderJSWorldRenderHelper {

    RenderJSLevelRender renderJSLevelRender = new RenderJSLevelRender();

    default RenderJSLevelRender getLevelRenderHelper() {
        return renderJSLevelRender;
    }

    class RenderJSLevelRender {
        public void renderSingleBlock(BlockState pState, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay, net.neoforged.neoforge.client.model.data.ModelData modelData, net.minecraft.client.renderer.RenderType renderType) {
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(pState, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, modelData, renderType);
        }

        public void renderItem(ItemStack pStack,
                               ItemDisplayContext pDisplayContext,
                               int pCombinedLight,
                               int pCombinedOverlay,
                               PoseStack pPoseStack,
                               MultiBufferSource pBufferSource,
                               @Nullable Level pLevel,
                               int pSeed) {
            Minecraft.getInstance().getItemRenderer().renderStatic(pStack, pDisplayContext, pCombinedLight, pCombinedOverlay, pPoseStack, pBufferSource, pLevel, pSeed);
        }

        public int packLight(int pBlockLight, int pSkyLight){
            return LightTexture.pack(pBlockLight, pSkyLight);
        }
    }
}
