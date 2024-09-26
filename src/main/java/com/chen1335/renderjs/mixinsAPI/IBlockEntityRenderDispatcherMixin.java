package com.chen1335.renderjs.mixinsAPI;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IBlockEntityRenderDispatcherMixin {
    <T extends BlockEntity> BlockEntityRenderer<T> rjs$getBlockEntityRenderByType(BlockEntityType<T> type );
}
