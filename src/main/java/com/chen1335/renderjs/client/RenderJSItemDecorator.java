package com.chen1335.renderjs.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RenderJSItemDecorator implements IItemDecorator {

    private final Consumer<Context> renderConsumer;
    public RenderJSItemDecorator(Consumer<Context> consumer){
        renderConsumer = consumer;
    }
    @Override
    public boolean render(@NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull ItemStack stack, int xOffset, int yOffset) {
        renderConsumer.accept(Context.update(guiGraphics, font, stack, xOffset, yOffset));
        return true;
    }

    public static class Context implements RenderJSGuiRenderHelper {
        private static final Context INSTANCE = new Context();
        public GuiGraphics guiGraphics;
        public Font font;
        public ItemStack stack;
        public int xOffset;
        public int yOffset;
        public static Context update(@NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull ItemStack stack, int xOffset, int yOffset){
            INSTANCE.guiGraphics = guiGraphics;
            INSTANCE.font = font;
            INSTANCE.stack = stack;
            INSTANCE.xOffset = xOffset;
            INSTANCE.yOffset = yOffset;
            INSTANCE.getGuiRenderHelper().update(guiGraphics);
            return INSTANCE;
        }
    }
}
