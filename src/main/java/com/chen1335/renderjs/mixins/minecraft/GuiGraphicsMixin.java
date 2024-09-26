package com.chen1335.renderjs.mixins.minecraft;

import com.chen1335.renderjs.client.RenderJSItemDecoratorHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",at = @At(value = "INVOKE"))
    private void renderItemDecorations(Font pFont, ItemStack pStack, int pX, int pY, @Nullable String pText, CallbackInfo callbackInfo){
        if (!pStack.isEmpty()) {
            RenderJSItemDecoratorHandler.of(pStack.getItem()).render((GuiGraphics) (Object) this,pFont,pStack,pX,pY);
            RenderJSItemDecoratorHandler.GLOBAL_DECORATOR_LOOKUP.render((GuiGraphics) (Object) this,pFont,pStack,pX,pY);
        }
    }
}
