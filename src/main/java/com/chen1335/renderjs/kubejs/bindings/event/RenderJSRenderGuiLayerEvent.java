package com.chen1335.renderjs.kubejs.bindings.event;

import com.chen1335.renderjs.Renderjs;
import com.chen1335.renderjs.client.RenderJSGuiRenderHelper;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = Renderjs.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class RenderJSRenderGuiLayerEvent implements ClientKubeEvent , RenderJSGuiRenderHelper {

    protected final RenderGuiLayerEvent event;

    @SubscribeEvent
    public static void RenderGuiLayerEvent$Pre(RenderGuiLayerEvent.Pre pre){
        RenderJSClientEvents.RENDERJS_RENDER_GUI_EVENT.post(new RenderJSRenderGuiLayerEvent.Pre(pre));
    }

    @SubscribeEvent
    public static void RenderGuiLayerEvent$Post(RenderGuiLayerEvent.Post post){
        RenderJSClientEvents.RENDERJS_RENDER_GUI_EVENT.post(new RenderJSRenderGuiLayerEvent.Post(post));
    }

    public RenderJSRenderGuiLayerEvent(RenderGuiLayerEvent event){
        this.event = event;
        getGuiRenderHelper().update(event.getGuiGraphics());
    }
    public void pre(Consumer<RenderGuiLayerEvent.Pre> preConsumer){

    }

    public void post(Consumer<RenderGuiLayerEvent.Post> postConsumer){

    }

    public static class Pre extends RenderJSRenderGuiLayerEvent{

        public Pre(RenderGuiLayerEvent.Pre pre) {
            super(pre);
        }

        @Override
        public void pre(Consumer<RenderGuiLayerEvent.Pre> preConsumer) {
            preConsumer.accept((RenderGuiLayerEvent.Pre) event);
        }
    }

    public static class Post extends RenderJSRenderGuiLayerEvent{

        public Post(RenderGuiLayerEvent.Post post) {
            super(post);
        }

        @Override
        public void post(Consumer<RenderGuiLayerEvent.Post> postConsumer) {
            postConsumer.accept((RenderGuiLayerEvent.Post) event);
        }
    }
}
