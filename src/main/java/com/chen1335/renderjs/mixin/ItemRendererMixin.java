package com.chen1335.renderjs.mixin;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow
    public float blitOffset;

    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("RETURN"))
    public void renderGuiItemDecorations(Font pFr, ItemStack pStack, int pXPosition, int pYPosition, String pText, CallbackInfo ci) {
        RenderJSItemDecoratorHandler.getInstance().registeredGlobalItemDecorator.render(pFr, pStack, pXPosition, pYPosition, blitOffset);
    }
}
