package com.chen1335.renderjs.mixin.core;

import com.chen1335.renderjs.Renderjs;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScriptManager.class)
public class KubeJSClientMixin {
    @Shadow(remap = false)
    @Final
    public ScriptType scriptType;

    @Inject(method = {"reload"}, at = {@At("RETURN")}, remap = false)
    private void reload(CallbackInfo ci) {
        if (this.scriptType == ScriptType.CLIENT) {
            Renderjs.reloadRenders();
        }
    }
}
