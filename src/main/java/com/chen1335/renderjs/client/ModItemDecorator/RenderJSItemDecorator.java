package com.chen1335.renderjs.client.ModItemDecorator;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemDecorator;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class RenderJSItemDecorator implements IItemDecorator {
    private Consumer<renderContext> consumer;

    public RenderJSItemDecorator(Consumer<renderContext> consumer) {
        this.consumer = consumer;
    }

    @HideFromJS
    public void setRender(Consumer<renderContext> consumer) {
        this.consumer = consumer;
    }

    @HideFromJS
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        this.consumer.accept(new renderContext(guiGraphics, font, stack, xOffset, yOffset));
        return true;
    }

    public static class renderContext {
        public final Font font;
        public final ItemStack itemStack;
        public final int xOffset;
        public final int yOffset;
        public GuiGraphics guiGraphics;

        public renderContext(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
            this.font = font;
            this.itemStack = itemStack;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.guiGraphics = guiGraphics;
        }
    }
}
