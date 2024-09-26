package com.chen1335.renderjs.kubejs.bindings.event;

import com.chen1335.renderjs.client.RenderJSBlockEntityRenderer;
import com.chen1335.renderjs.mixinsAPI.IBlockEntityRenderDispatcherMixin;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class RenderJSBlockEntityRenderReloadEvent implements ClientKubeEvent {

    public <T extends BlockEntity> BlockEntityRenderer<T> getBlockEntityRender(BlockEntityType<T> type){
       return  ((IBlockEntityRenderDispatcherMixin) Minecraft.getInstance().getBlockEntityRenderDispatcher()).rjs$getBlockEntityRenderByType(type);
    }

    public RenderJSBlockEntityRenderer getRenderJSBlockEntityRender(BlockEntityType<BlockEntity> type){
        return (RenderJSBlockEntityRenderer) getBlockEntityRender(type);
    }
}
