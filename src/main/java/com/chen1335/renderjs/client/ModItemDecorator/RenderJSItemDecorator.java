package com.chen1335.renderjs.client.ModItemDecorator;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.RenderJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.function.Consumer;

public class RenderJSItemDecorator implements IItemDecorator {
    private final ArrayList<Consumer<RenderContext>> consumers;

    public RenderJSItemDecorator(@Nullable Consumer<RenderContext> consumer) {
        consumers = new ArrayList<>();
        if (consumer != null) {
            consumers.add(consumer);
        }
    }

    @HideFromJS
    @Override
    public boolean render(@NotNull GuiGraphicsExtractor guiGraphics, @NotNull Font font, @NotNull ItemStack stack, int xOffset, int yOffset) {
        if (RenderJS.CAN_RENDER) {
            guiGraphics.pose().pushMatrix();
            RenderContext context = RenderContext.getContext().update(guiGraphics, font, stack, xOffset, yOffset);
            consumers.forEach(consumer -> consumer.accept(context));
            guiGraphics.pose().popMatrix();
        }
        return true;
    }

    public void addRender(Consumer<RenderContext> consumer) {
        consumers.add(consumer);
    }

    public void clear() {
        consumers.clear();
    }

    public static class RenderContext implements IGuiRenderHelper, IRenderJSPoseStackHelper {
        private static final RenderContext context = new RenderContext(null, null, null, 0, 0);
        public Font font;
        public ItemStack itemStack;
        public int xOffset;
        public int yOffset;
        public GuiGraphicsExtractor guiGraphics;

        public RenderContext(GuiGraphicsExtractor guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
            this.font = font;
            this.itemStack = itemStack;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.guiGraphics = guiGraphics;
        }

        public static RenderContext getContext() {
            return context;
        }

        public RenderContext update(GuiGraphicsExtractor guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
            this.font = font;
            this.itemStack = itemStack;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.guiGraphics = guiGraphics;
            return this;
        }

        @Override
        public GuiGraphicsExtractor getGuiGraphics() {
            return guiGraphics;
        }

        @Override
        public Matrix3x2fStack getPoseStack() {
            return guiGraphics.pose();
        }
    }
}
