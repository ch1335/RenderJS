package com.chen1335.renderjs.mixin;

import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private static final EntityDataAccessor<CompoundTag> RENDER_JS_EXTRA_DATA = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.COMPOUND_TAG);

    @Inject(method = {"defineSynchedData"}, at = @At("RETURN"))
    private void onDefineSynchedData(CallbackInfo ci) {
        ((Entity) (Object) this).getEntityData().define(RENDER_JS_EXTRA_DATA, new CompoundTag());
    }

    @Info("设置额外可同步的生物实体信息\n Set SynchedData")
    @Unique
    public void renderJS$setRenderJsExtraData(CompoundTag compoundTag) {
        ((Entity) (Object) this).getEntityData().set(RENDER_JS_EXTRA_DATA, compoundTag);
    }

    @Info("获取从服务端同步到客户端的生物实体信息\n Read SynchedData")
    @Unique
    public CompoundTag renderJS$getRenderJsExtraData() {
        return ((Entity) (Object) this).getEntityData().get(RENDER_JS_EXTRA_DATA);
    }
}
