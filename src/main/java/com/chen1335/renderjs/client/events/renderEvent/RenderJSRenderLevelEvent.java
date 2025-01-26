package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.ILevelRenderEvent;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderJSRenderLevelEvent extends ClientEventJS implements ILevelRenderEvent, ILevelRenderHelper, IGuiRenderHelper, IRenderJSPoseStackHelper {
    private final RenderLevelStageEvent event;

    public RenderJSRenderLevelEvent(RenderLevelStageEvent event) {
        this.event = event;
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderLevelEvent(RenderLevelStageEvent event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        event.getPoseStack().pushPose();
        RenderJSEvents.RENDER_LEVEL.post(new RenderJSRenderLevelEvent(event));
        event.getPoseStack().popPose();
    }


    @Override
    public RenderLevelStageEvent getEvent() {
        return event;
    }

    @Override
    public PoseStack getPoseStack() {
        return event.getPoseStack();
    }

}
