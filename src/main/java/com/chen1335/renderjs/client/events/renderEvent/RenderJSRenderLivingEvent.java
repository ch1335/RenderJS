package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.ILevelPoseHelper;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.API.IRenderJSEvent;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;


@Info("Living entity render event context. Fires before and after a living entity model is submitted.")
@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID)
public class RenderJSRenderLivingEvent implements ClientKubeEvent, ILevelRenderHelper, ILevelPoseHelper, IRenderJSEvent {

    protected final RenderLivingEvent<?, ?, ?> event;
    protected final LivingEntityRenderState renderState;
    protected final LivingEntityRenderer<?, ?, ?> renderer;
    protected final float partialTick;

    @SuppressWarnings("rawtypes")
    public RenderJSRenderLivingEvent(RenderLivingEvent event) {
        this.event = event;
        this.renderState = event.getRenderState();
        this.renderer = event.getRenderer();
        this.partialTick = event.getPartialTick();
    }

    @SubscribeEvent
    @HideFromJS
    @SuppressWarnings("rawtypes")
    public static void RenderLivingEvent$Pre(RenderLivingEvent.Pre event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSEvents.LIVING_RENDER_PRE.post(new RenderJSRenderLivingEvent(event));
    }

    @SubscribeEvent
    @HideFromJS
    @SuppressWarnings("rawtypes")
    public static void RenderLivingEvent$Post(RenderLivingEvent.Post event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSEvents.LIVING_RENDER_Post.post(new RenderJSRenderLivingEvent(event));
    }

    @Override
    public PoseStack getPoseStack() {
        return event.getPoseStack();
    }

    @Override
    public SubmitNodeCollector getSubmitNodeCollector() {
        return event.getSubmitNodeCollector();
    }

    @Info("Returns the render state of the living entity being rendered.")
    public LivingEntityRenderState getRenderState() {
        return renderState;
    }

    @Info("Returns the renderer of the living entity being rendered.")
    public LivingEntityRenderer<?, ?, ?> getRenderer() {
        return renderer;
    }

    @Info("Returns the partial tick of the current frame.")
    public float getPartialTick() {
        return partialTick;
    }
}
