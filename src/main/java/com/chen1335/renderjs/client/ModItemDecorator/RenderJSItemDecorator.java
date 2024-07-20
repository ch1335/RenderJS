package com.chen1335.renderjs.client.ModItemDecorator;

import com.chen1335.renderjs.Renderjs;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemDecorator;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class RenderJSItemDecorator implements IItemDecorator {
    private final ArrayList<Consumer<RenderContext>> consumers;

    public RenderJSItemDecorator(@Nullable Consumer<RenderContext> consumer) {
        consumers = new ArrayList<>();
        if (consumer != null) {
            consumers.add(consumer);
        }
    }

    @HideFromJS
    public boolean render(Font font, ItemStack itemStack, int xOffset, int yOffset, float blitOffset) {
        if (Renderjs.CAN_RENDER) {
            consumers.forEach(consumer -> consumer.accept(RenderContext.getContext().update(font, itemStack, xOffset, yOffset, blitOffset)));
        }
        return true;
    }

    public void addRender(Consumer<RenderContext> consumer) {
        consumers.add(consumer);
    }

    public void clear() {
        consumers.clear();
    }

    public static class RenderContext {
        private static final RenderContext context = new RenderContext(null, null, 0, 0, 0);
        public Font font;
        public ItemStack itemStack;
        public int xOffset;
        public int yOffset;
        public float blitOffset;

        public RenderContext(Font font, ItemStack itemStack, int xOffset, int yOffset, float blitOffset) {
            this.font = font;
            this.itemStack = itemStack;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.blitOffset = blitOffset;
        }

        public static RenderContext getContext() {
            return context;
        }

        public RenderContext update(Font font, ItemStack itemStack, int xOffset, int yOffset, float blitOffset) {
            this.font = font;
            this.itemStack = itemStack;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.blitOffset = blitOffset;
            return this;
        }
    }
}
