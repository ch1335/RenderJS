package com.chen1335.renderjs.mixin;

import com.chen1335.renderjs.RenderJS;
import dev.latvian.mods.kubejs.client.KubeJSClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KubeJSClient.class)
public class KubeJSClientMixin {
    @Inject(method = {"init"}, at = {@At("HEAD")}, remap = false)
    private void init(CallbackInfo ci) {
        RenderJS.clientInit();
    }

}
