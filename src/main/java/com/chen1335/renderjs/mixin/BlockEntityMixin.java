package com.chen1335.renderjs.mixin;

import com.chen1335.renderjs.mixinAPI.IBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements IForgeBlockEntity {

    @Shadow
    @Final
    private BlockEntityType<?> type;

    @Shadow public abstract BlockState getBlockState();

    @Shadow public abstract BlockPos getBlockPos();

    @Shadow @Nullable public abstract Level getLevel();

    @Unique
    @Override
    public AABB getRenderBoundingBox() {
        if (((IBlockEntityType) this.type).rjs$hasRenderJSCustomRender()) {
            return IForgeBlockEntity.INFINITE_EXTENT_AABB;
        } else {
            AABB bb = INFINITE_EXTENT_AABB;
            BlockState state = this.getBlockState();
            Block block = state.getBlock();
            BlockPos pos = this.getBlockPos();
            if (block == Blocks.ENCHANTING_TABLE)
            {
                bb = new AABB(pos, pos.offset(1, 1, 1));
            }
            else if (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST)
            {
                bb = new AABB(pos.offset(-1, 0, -1), pos.offset(2, 2, 2));
            }
            else if (block == Blocks.STRUCTURE_BLOCK)
            {
                bb = INFINITE_EXTENT_AABB;
            }
            else if (block != null && block != Blocks.BEACON)
            {
                AABB cbb = null;
                try
                {
                    VoxelShape collisionShape = state.getCollisionShape(this.getLevel(), pos);
                    if (!collisionShape.isEmpty())
                    {
                        cbb = collisionShape.bounds().move(pos);
                    }
                }
                catch (Exception e)
                {
                    cbb = new net.minecraft.world.phys.AABB(pos.offset(-1, 0, -1), pos.offset(1, 1, 1));
                }
                if (cbb != null) bb = cbb;
            }
            return bb;
        }
    }
}
