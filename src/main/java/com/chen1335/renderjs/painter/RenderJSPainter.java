package com.chen1335.renderjs.painter;

import com.chen1335.renderjs.RenderJS;
import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.client.painter.screen.ScreenPainterObject;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RenderJS.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderJSPainter {
    public enum Layer {
        hud,
        screen
    }

    public enum Step {
        belowAll,
        aboveAll
    }

    @HideFromJS
    @SubscribeEvent
    public static void renderGuiPre(RenderGuiEvent.Pre event) {
        render(event.getGuiGraphics(), Layer.hud, Step.belowAll);

    }

    @HideFromJS
    @SubscribeEvent
    public static void renderGuiPost(RenderGuiEvent.Post event) {
        render(event.getGuiGraphics(), Layer.hud, Step.aboveAll);
    }

    @HideFromJS
    @SubscribeEvent
    public static void renderScreenPre(ScreenEvent.Render.Pre event) {
        render(event.getGuiGraphics(), Layer.screen, Step.belowAll);
    }

    @HideFromJS
    @SubscribeEvent
    public static void renderScreenPost(ScreenEvent.Render.Post event) {
        render(event.getGuiGraphics(), Layer.screen, Step.aboveAll);
    }

    @HideFromJS
    private static void render(GuiGraphics guiGraphics, Layer layer, Step step) {
        for (ScreenPainterObject screenObject : Painter.INSTANCE.getScreenObjects()) {
            if (screenObject instanceof IRenderJSPainterObject object && object.visible() && object.getLayer() == layer && object.getStep() == step) {
                object.draw(guiGraphics);
            }
        }
    }
}
