package com.chen1335.renderjs.client.ModItemDecorator;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
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
    public boolean render(Font font, ItemStack itemStack, int xOffset, int yOffset, float blitOffset) {
        this.consumer.accept(new renderContext(font, itemStack, xOffset, yOffset, blitOffset));
        return true;
    }

    @Info("注册的不满意?,使用这个来重新定义!")
    public void setRender(Consumer<renderContext> consumer) {
        this.consumer = consumer;
    }

    public static class renderContext {
        public final Font font;
        public final ItemStack itemStack;
        public final int xOffset;
        public final int yOffset;
        public float blitOffset;

        public renderContext(Font font, ItemStack itemStack, int xOffset, int yOffset, float blitOffset) {
            this.font = font;
            this.itemStack = itemStack;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.blitOffset = blitOffset;
        }
    }
}
