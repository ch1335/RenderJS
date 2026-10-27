package com.chen1335.renderjs.client.ModItemDecorator;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.client.renderer.RenderJSPoseStackUtil;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.function.Consumer;

@Info("Item decoration implementation. Scripts normally create decorations through registerItemDecorations.")
public class RenderJSItemDecorator implements IItemDecorator {
    private final ArrayList<Consumer<RenderContext>> consumers;

    public RenderJSItemDecorator(@Nullable Consumer<RenderContext> consumer) {
        consumers = new ArrayList<>();
        if (consumer != null) {
            consumers.add(consumer);
        }
    }

    @Override
    public boolean render(@NotNull GuiGraphicsExtractor guiGraphics, @NotNull Font font, @NotNull ItemStack stack, int xOffset, int yOffset) {
        if (RenderJS.CAN_RENDER) {
            RenderContext context = RenderContext.getContext().update(
                    guiGraphics,
                    RenderJSPoseStackUtil.copy(guiGraphics.pose()),
                    font,
                    stack,
                    xOffset,
                    yOffset
            );
            int depth = RenderJSPoseStackUtil.depth(context.getPoseStack());
            consumers.forEach(consumer -> consumer.accept(context));
            RenderJSPoseStackUtil.checkBalanced("itemDecoration", context.getPoseStack(), depth);
        }
        return true;
    }

    @Info(value = "Adds an additional decoration callback.",
            params = {@Param(name = "consumer", value = "Callback that draws the decoration.")})
    public void addRender(Consumer<RenderContext> consumer) {
        consumers.add(consumer);
    }

    @Info("Removes all decoration callbacks.")
    public void clear() {
        consumers.clear();
    }

    @Info("Render context passed to item decoration callbacks. Provides the item stack, the icon offset and 2D drawing helpers.")
    public static class RenderContext implements IGuiRenderHelper, IRenderJSPoseStackHelper {
        private static final RenderContext context = new RenderContext(null, null, null, null, 0, 0);

        @Info("Font used to draw text on the item icon.")
        public Font font;
        @Info("Item stack whose icon is being decorated.")
        public ItemStack itemStack;
        @Info("Horizontal offset of the item icon, in GUI pixels.")
        public int xOffset;
        @Info("Vertical offset of the item icon, in GUI pixels.")
        public int yOffset;
        @Info("GUI graphics extractor of the current frame.")
        public GuiGraphicsExtractor guiGraphics;
        @Info("Private copy of the GUI matrix stack used by the decoration callback.")
        public Matrix3x2fStack poseStack;

        public RenderContext(GuiGraphicsExtractor guiGraphics, Matrix3x2fStack poseStack, Font font, ItemStack itemStack, int xOffset, int yOffset) {
            this.guiGraphics = guiGraphics;
            this.poseStack = poseStack;
            this.font = font;
            this.itemStack = itemStack;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }

        public static RenderContext getContext() {
            return context;
        }

        public RenderContext update(GuiGraphicsExtractor guiGraphics, Matrix3x2fStack poseStack, Font font, ItemStack itemStack, int xOffset, int yOffset) {
            this.guiGraphics = guiGraphics;
            this.poseStack = poseStack;
            this.font = font;
            this.itemStack = itemStack;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            return this;
        }

        @Override
        public GuiGraphicsExtractor getGuiGraphics() {
            return guiGraphics;
        }

        @Override
        public Matrix3x2fStack getPoseStack() {
            return poseStack;
        }
    }
}
