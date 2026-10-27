package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.ILevelPoseHelper;
import com.chen1335.renderjs.API.ILevelRenderEvent;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.client.renderer.RenderJSPoseStackUtil;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

/**
 * World rendering context. MC 26.1.2 dropped the per-stage level render events; custom world
 * geometry is submitted through {@link SubmitCustomGeometryEvent} instead.
 */
@Info("World render event context. Geometry submitted here is queued for the current frame's custom geometry pass.")
@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID)
public class RenderJSRenderLevelEvent implements ClientKubeEvent, ILevelRenderEvent, ILevelRenderHelper, ILevelPoseHelper {
    private final PoseStack poseStack;
    private final SubmitNodeCollector submitNodeCollector;
    private final LevelRenderState levelRenderState;

    public RenderJSRenderLevelEvent(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LevelRenderState levelRenderState) {
        this.poseStack = poseStack;
        this.submitNodeCollector = submitNodeCollector;
        this.levelRenderState = levelRenderState;
    }

    @SubscribeEvent
    public static void RenderLevelEvent(SubmitCustomGeometryEvent event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }

        PoseStack poseStack = RenderJSPoseStackUtil.copy(event.getPoseStack());
        int depth = RenderJSPoseStackUtil.depth(poseStack);

        RenderJSEvents.RENDER_LEVEL.post(new RenderJSRenderLevelEvent(poseStack, event.getSubmitNodeCollector(), event.getLevelRenderState()));

        RenderJSPoseStackUtil.checkBalanced("onLevelRender", poseStack, depth);
    }

    @Override
    public PoseStack getPoseStack() {
        return poseStack;
    }

    @Override
    public SubmitNodeCollector getSubmitNodeCollector() {
        return submitNodeCollector;
    }

    @Override
    public LevelRenderState getLevelRenderState() {
        return levelRenderState;
    }
}