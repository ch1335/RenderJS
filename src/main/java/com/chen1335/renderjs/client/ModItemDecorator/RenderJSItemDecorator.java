package com.chen1335.renderjs.client.ModItemDecorator;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.RenderJS;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public boolean render(@NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull ItemStack stack, int xOffset, int yOffset) {
        if (RenderJS.CAN_RENDER) {
            guiGraphics.pose().pushPose();
            RenderContext context = RenderContext.getContext().update(guiGraphics, font, stack, xOffset, yOffset);
            consumers.forEach(consumer -> consumer.accept(context));
            guiGraphics.pose().popPose();

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
        public GuiGraphics guiGraphics;

        public RenderContext(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
            this.font = font;
            this.itemStack = itemStack;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.guiGraphics = guiGraphics;
        }

        public static RenderContext getContext() {
            return context;
        }

        public RenderContext update(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
            this.font = font;
            this.itemStack = itemStack;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.guiGraphics = guiGraphics;
            return this;
        }

        @Override
        public GuiGraphics getGuiGraphics() {
            return guiGraphics;
        }

        @Override
        public PoseStack getPoseStack() {
            return getGuiGraphics().pose();
        }
    }
}
