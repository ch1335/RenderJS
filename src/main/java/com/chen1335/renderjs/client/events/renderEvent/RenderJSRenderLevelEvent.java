package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.ILevelPoseHelper;
import com.chen1335.renderjs.API.ILevelRenderEvent;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

/**
 * World rendering context. MC 26.1.2 dropped the per-stage level render events; custom world
 * geometry is submitted through {@link SubmitCustomGeometryEvent} instead.
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID)
public class RenderJSRenderLevelEvent implements ClientKubeEvent, ILevelRenderEvent, ILevelRenderHelper, ILevelPoseHelper {
    private final SubmitCustomGeometryEvent event;

    public RenderJSRenderLevelEvent(SubmitCustomGeometryEvent event) {
        this.event = event;
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderLevelEvent(SubmitCustomGeometryEvent event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }

        event.getPoseStack().pushPose();

        RenderJSRenderLevelEvent event1 = new RenderJSRenderLevelEvent(event);
        RenderJSEvents.RENDER_LEVEL.post(event1);

        event.getPoseStack().popPose();
    }

    @Override
    public SubmitCustomGeometryEvent getEvent() {
        return event;
    }

    @Override
    public PoseStack getPoseStack() {
        return event.getPoseStack();
    }

    @Override
    public SubmitNodeCollector getSubmitNodeCollector() {
        return event.getSubmitNodeCollector();
    }
}
