package com.chen1335.renderjs.mixin;


import com.chen1335.renderjs.client.renderer.RenderJSBlockEntityRenderer;
import com.chen1335.renderjs.mixinAPI.IBlockEntityType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(BlockEntityRenderers.class)
public class BlockEntityRenderersMixin {
    @Inject(method = {"createEntityRenderers"}, at = @At(value = "RETURN", remap = false))
    private static void onCreateEntityRenderers(BlockEntityRendererProvider.Context pContext, CallbackInfoReturnable<Map<BlockEntityType<?>, BlockEntityRenderer<?>>> cir) {
        cir.getReturnValue().forEach((blockEntityType, blockEntityRenderer) -> {
            if (blockEntityRenderer instanceof RenderJSBlockEntityRenderer) {
                ((IBlockEntityType) blockEntityType).rjs$setHasRenderJSCustomRender(true);
            }
        });
    }
}
