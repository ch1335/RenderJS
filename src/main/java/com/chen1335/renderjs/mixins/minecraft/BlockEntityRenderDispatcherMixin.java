package com.chen1335.renderjs.mixins.minecraft;

import com.chen1335.renderjs.kubejs.bindings.event.RenderJSBlockEntityRenderReloadEvent;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSClientEvents;
import com.chen1335.renderjs.mixinsAPI.IBlockEntityRenderDispatcherMixin;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin implements IBlockEntityRenderDispatcherMixin {
    @Shadow
    private Map<BlockEntityType<?>, BlockEntityRenderer<?>> renderers;

    @Info("for get BlockEntityRender By Type")
    @Unique
    public <E extends BlockEntity> BlockEntityRenderer<E> rjs$getBlockEntityRenderByType(BlockEntityType<E> type ){
        return (BlockEntityRenderer<E>) this.renderers.get(type);
    }

    @Inject(method = "onResourceManagerReload(Lnet/minecraft/server/packs/resources/ResourceManager;)V",at = @At("RETURN"))
    public void onResourceManagerReload(ResourceManager pResourceManager, CallbackInfo info) {
        RenderJSClientEvents.RENDERJS_BLOCK_ENTITY_RENDER_RELOAD_EVENT.post(new RenderJSBlockEntityRenderReloadEvent());
    }
}
