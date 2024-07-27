package com.chen1335.renderjs.mixin;

import com.chen1335.renderjs.painter.objects.RenderJSItemPainterObject;
import com.chen1335.renderjs.painter.objects.RenderJSTextPainterObject;
import com.chen1335.renderjs.painter.objects.RenderJSTexturePainterObject;
import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.client.painter.PainterFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Painter.class,remap = false)
public abstract class PainterMixin {
    @Shadow public abstract void registerObject(String name, PainterFactory supplier);

    @Inject(method = "registerBuiltinObjects",at = @At("HEAD"))
    private void registerBuiltinObjects(CallbackInfo ci){
        this.registerObject("renderJSTexture", RenderJSTexturePainterObject::new);
        this.registerObject("renderJSText", RenderJSTextPainterObject::new);
        this.registerObject("renderJSItem", RenderJSItemPainterObject::new);
    }
}
