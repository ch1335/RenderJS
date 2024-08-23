package com.chen1335.renderjs.mixin;

import com.chen1335.renderjs.mixinAPI.IBlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin implements IBlockEntityType {
    @Unique
    boolean rjs$hasRenderJSCustomRender = false;

    @Unique
    public boolean rjs$hasRenderJSCustomRender() {
        return rjs$hasRenderJSCustomRender;
    }

    @Unique
    public void rjs$setHasRenderJSCustomRender(boolean hasRenderJSCustomRender) {
        rjs$hasRenderJSCustomRender = hasRenderJSCustomRender;
    }
}
