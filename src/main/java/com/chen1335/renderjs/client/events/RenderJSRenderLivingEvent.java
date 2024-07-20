package com.chen1335.renderjs.client.events;

import com.chen1335.renderjs.Renderjs;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Renderjs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderJSRenderLivingEvent extends EventJS {

    protected final RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>> event;

    public RenderJSRenderLivingEvent(RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>> event) {
        this.event = event;
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderLivingEvent$Pre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        RenderJSEvents.LIVING_RENDER.post(new RenderJSRenderLivingEvent.Pre(event));
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderLivingEvent$Post(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        RenderJSEvents.LIVING_RENDER.post(new RenderJSRenderLivingEvent.Post(event));
    }

    public void pre(Consumer<RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>>> consumer) {

    }

    public void post(Consumer<RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>>> consumer) {

    }

    public static class Pre extends RenderJSRenderLivingEvent {
        public Pre(RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>> event) {
            super(event);
        }

        @Override
        public void pre(Consumer<RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>>> consumer) {
            consumer.accept(event);
        }

        public RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>> getEvent() {
            return event;
        }
    }

    public static class Post extends RenderJSRenderLivingEvent {
        public Post(RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>> event) {
            super(event);
        }

        @Override
        public void post(Consumer<RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>>> consumer) {
            consumer.accept(event);
        }

        public RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>> getEvent() {
            return event;
        }
    }
}
