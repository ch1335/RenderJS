package com.chen1335.renderjs.mixin;

import com.chen1335.renderjs.RenderJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(dev.latvian.mods.kubejs.script.ScriptManager.class)
public class ScriptManagerMixin {
    @Shadow(remap = false)
    @Final
    public ScriptType scriptType;

    @Inject(method = {"reload"}, at = {@At("RETURN")}, remap = false)
    private void reload(CallbackInfo ci) {
        if (this.scriptType == ScriptType.CLIENT) {
            if (RenderJS.CLIENT_INIT) {
                RenderJS.LOGGER.info("RenderJSReload!");
                RenderJS.reloadRenders();
            }
        }
    }

    @Inject(method = {"unload"}, at = {@At("HEAD")}, remap = false)
    private void unload(CallbackInfo ci) {
        if (this.scriptType == ScriptType.CLIENT) {
            RenderJS.CAN_RENDER = false;
        }
    }

    @Inject(method = {"load"}, at = {@At("RETURN")}, remap = false)
    private void load(CallbackInfo ci) {
        if (this.scriptType == ScriptType.CLIENT) {
            RenderJS.CAN_RENDER = true;
        }
    }
}
