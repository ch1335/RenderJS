package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.IGuiRenderEvent;
import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;


@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RenderJSRenderGuiEvent implements ClientKubeEvent, IGuiRenderHelper, IGuiRenderEvent, IRenderJSPoseStackHelper {
    public static boolean SUSPENDED = false;

    protected final RenderGuiEvent event;

    public RenderJSRenderGuiEvent(RenderGuiEvent event) {
        this.event = event;
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderGuiEvent$Pre(RenderGuiEvent.Pre event) {
        if (!RenderJS.CAN_RENDER || SUSPENDED) {
            return;
        }

        try {
            event.getGuiGraphics().pose().pushPose();
            RenderJSEvents.RENDER_GUI_PRE.post(new RenderJSRenderGuiEvent(event));
            event.getGuiGraphics().pose().popPose();
        } catch (RuntimeException e) {
            RenderJS.LOGGER.error("An exception was found during rendering, and rendering has been automatically stopped until the next reload", e);
            SUSPENDED = true;
        }
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderGuiEvent$Post(RenderGuiEvent.Post event) {
        if (!RenderJS.CAN_RENDER || SUSPENDED) {
            return;
        }

        try {
            event.getGuiGraphics().pose().pushPose();
            RenderJSEvents.RENDER_GUI_POST.post(new RenderJSRenderGuiEvent(event));
            event.getGuiGraphics().pose().popPose();
        } catch (RuntimeException e) {
            RenderJS.LOGGER.error("An exception was found during rendering, and rendering has been automatically stopped until the next reload", e);
            SUSPENDED = true;
        }


    }


    @Override
    public RenderGuiEvent getEvent() {
        return event;
    }

    @Override
    public PoseStack getPoseStack() {
        return event.getGuiGraphics().pose();
    }

    @Override
    public GuiGraphics getGuiGraphics() {
        return event.getGuiGraphics();
    }
}
