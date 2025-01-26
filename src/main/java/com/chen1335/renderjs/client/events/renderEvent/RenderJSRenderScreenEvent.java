package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.IScreenRenderEvent;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;


@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RenderJSRenderScreenEvent implements ClientKubeEvent, IGuiRenderHelper, IScreenRenderEvent {

    protected final ScreenEvent.Render event;

    public RenderJSRenderScreenEvent(ScreenEvent.Render event) {
        this.event = event;
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderGuiEvent$Pre(ScreenEvent.Render.Pre event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSEvents.RENDER_SCREEN_PRE.post(new RenderJSRenderScreenEvent(event));
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderGuiEvent$Post(ScreenEvent.Render.Post event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSEvents.RENDER_SCREEN_POST.post(new RenderJSRenderScreenEvent(event));
    }


    @Override
    public ScreenEvent.Render getEvent() {
        return event;
    }

    @Override
    public GuiGraphics getGuiGraphics() {
        return event.getGuiGraphics();
    }
}
