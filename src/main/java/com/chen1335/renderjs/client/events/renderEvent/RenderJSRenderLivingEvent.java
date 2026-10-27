package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.ILevelPoseHelper;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.API.IRenderJSEvent;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.client.renderer.RenderJSPoseStackUtil;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.kubejs.typings.Info;
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

    protected final LivingEntityRenderState renderState;
    protected final LivingEntityRenderer<?, ?, ?> renderer;
    protected final float partialTick;
    protected final PoseStack poseStack;
    protected final SubmitNodeCollector submitNodeCollector;

    public RenderJSRenderLivingEvent(LivingEntityRenderState renderState, LivingEntityRenderer<?, ?, ?> renderer, float partialTick, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        this.renderState = renderState;
        this.renderer = renderer;
        this.partialTick = partialTick;
        this.poseStack = poseStack;
        this.submitNodeCollector = submitNodeCollector;
    }

    @SubscribeEvent
    @SuppressWarnings("rawtypes")
    public static void RenderLivingEvent$Pre(RenderLivingEvent.Pre event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSRenderLivingEvent kubeEvent = fromEvent(event);
        int depth = RenderJSPoseStackUtil.depth(kubeEvent.getPoseStack());
        RenderJSEvents.LIVING_RENDER_PRE.post(kubeEvent);
        RenderJSPoseStackUtil.checkBalanced("onLivingPreRender", kubeEvent.getPoseStack(), depth);
    }

    @SubscribeEvent
    @SuppressWarnings("rawtypes")
    public static void RenderLivingEvent$Post(RenderLivingEvent.Post event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSRenderLivingEvent kubeEvent = fromEvent(event);
        int depth = RenderJSPoseStackUtil.depth(kubeEvent.getPoseStack());
        RenderJSEvents.LIVING_RENDER_Post.post(kubeEvent);
        RenderJSPoseStackUtil.checkBalanced("onLivingPostRender", kubeEvent.getPoseStack(), depth);
    }

    @SuppressWarnings("rawtypes")
    private static RenderJSRenderLivingEvent fromEvent(RenderLivingEvent event) {
        return new RenderJSRenderLivingEvent(
                event.getRenderState(),
                event.getRenderer(),
                event.getPartialTick(),
                RenderJSPoseStackUtil.copy(event.getPoseStack()),
                event.getSubmitNodeCollector()
        );
    }

    @Override
    public PoseStack getPoseStack() {
        return poseStack;
    }

    @Override
    public SubmitNodeCollector getSubmitNodeCollector() {
        return submitNodeCollector;
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