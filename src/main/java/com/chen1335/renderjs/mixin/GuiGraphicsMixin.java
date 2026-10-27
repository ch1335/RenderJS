package com.chen1335.renderjs.mixin;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsMixin {
    @Inject(method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("RETURN"))
    public void renderGuiItemDecorations(Font font, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
        RenderJSItemDecoratorHandler.getInstance().registeredGlobalItemDecorator.render((GuiGraphicsExtractor) (Object) this, font, stack, xPosition, yPosition);
    }
}
