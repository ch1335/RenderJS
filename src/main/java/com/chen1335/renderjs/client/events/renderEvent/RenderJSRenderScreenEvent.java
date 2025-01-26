package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.IScreenRenderEvent;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderJSRenderScreenEvent extends ClientEventJS implements IGuiRenderHelper, IScreenRenderEvent {

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
    public PoseStack getPoseStack() {
        return guiRenderHelper.getPoseStack();
    }
}
