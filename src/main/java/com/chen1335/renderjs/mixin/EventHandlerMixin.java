package com.chen1335.renderjs.mixin;

import com.chen1335.renderjs.API.IRenderJSEvent;
import dev.latvian.mods.kubejs.event.EventExceptionHandler;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventHandlerContainer;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EventHandler.class,remap = false)
public class EventHandlerMixin {
    @Inject(method = "postToHandlers", at = @At("HEAD"), cancellable = true)
    private void postToHandlers(ScriptType type, EventHandlerContainer[] containers, EventJS event, EventExceptionHandler exh, CallbackInfo ci) {
        if (containers == null && event instanceof IRenderJSEvent) {
            ci.cancel();
        }
    }
}
