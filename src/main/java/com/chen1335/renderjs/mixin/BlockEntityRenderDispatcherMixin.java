package com.chen1335.renderjs.mixin;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
    @Shadow private Map<BlockEntityType<?>, BlockEntityRenderer<?>> renderers;

    @Unique
    public  BlockEntityRenderer<BlockEntity> rjs$getRendererByType(BlockEntityType<BlockEntity> blockEntityType){
        return (BlockEntityRenderer<BlockEntity>) renderers.get(blockEntityType);
    }
}
