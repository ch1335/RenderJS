package com.chen1335.renderjs.kubejs.bindings.event;

import com.chen1335.renderjs.Renderjs;
import com.chen1335.renderjs.client.RenderJSGuiRenderHelper;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = Renderjs.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class RenderJSRenderGuiEvent implements ClientKubeEvent , RenderJSGuiRenderHelper {

    protected final RenderGuiEvent event;

    @SubscribeEvent
    public static void RenderGuiEvent$Pre(RenderGuiEvent.Pre pre){
        RenderJSClientEvents.RENDERJS_RENDER_GUI_EVENT.post(new RenderJSRenderGuiEvent.Pre(pre));
    }

    @SubscribeEvent
    public static void RenderGuiEvent$Post(RenderGuiEvent.Post post){
        RenderJSClientEvents.RENDERJS_RENDER_GUI_EVENT.post(new RenderJSRenderGuiEvent.Post(post));
    }

    public RenderJSRenderGuiEvent(RenderGuiEvent event){
        this.event = event;
        getGuiRenderHelper().update(event.getGuiGraphics());
    }
    public void pre(Consumer<RenderGuiEvent.Pre> preConsumer){

    }

    public void post(Consumer<RenderGuiEvent.Post> postConsumer){

    }

    public static class Pre extends RenderJSRenderGuiEvent{

        public Pre(RenderGuiEvent.Pre pre) {
            super(pre);
        }

        @Override
        public void pre(Consumer<RenderGuiEvent.Pre> preConsumer) {
            preConsumer.accept((RenderGuiEvent.Pre) event);
        }
    }

    public static class Post extends RenderJSRenderGuiEvent{

        public Post(RenderGuiEvent.Post post) {
            super(post);
        }

        @Override
        public void post(Consumer<RenderGuiEvent.Post> postConsumer) {
            postConsumer.accept((RenderGuiEvent.Post) event);
        }
    }
}
