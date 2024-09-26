package com.chen1335.renderjs.kubejs.bindings.event;

import com.chen1335.renderjs.Renderjs;
import com.chen1335.renderjs.client.RenderJSWorldRenderHelper;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = Renderjs.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class RenderJSRenderLevelStageEvent extends RenderLevelStageEvent implements ClientKubeEvent, RenderJSWorldRenderHelper {
    public RenderJSRenderLevelStageEvent(RenderLevelStageEvent event) {
        super(event.getStage(), event.getLevelRenderer(), event.getPoseStack(), event.getModelViewMatrix(), event.getProjectionMatrix(), event.getRenderTick(), event.getPartialTick(), event.getCamera(), event.getFrustum());
    }
    @SubscribeEvent
    public static void RenderLevelStageEvent(RenderLevelStageEvent event){
        RenderJSClientEvents.RENDERJS_RENDER_LEVEL_STAGE_EVENT.post(new RenderJSRenderLevelStageEvent(event));
    }
}
