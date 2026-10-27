package com.chen1335.renderjs.client.renderer.state;

import dev.latvian.mods.kubejs.block.entity.KubeBlockEntity;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RenderJSBlockEntityRenderState extends BlockEntityRenderState {
    public @Nullable BlockEntity blockEntity;
    public @Nullable KubeBlockEntity blockEntityJS;
    public float partialTick;
    public Vec3 cameraPosition = Vec3.ZERO;
}
