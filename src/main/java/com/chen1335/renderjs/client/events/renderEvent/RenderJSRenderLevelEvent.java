package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.ILevelRenderEvent;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RenderJSRenderLevelEvent implements ClientKubeEvent, ILevelRenderEvent, ILevelRenderHelper, IGuiRenderHelper, IRenderJSPoseStackHelper {
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

        RenderJSRenderLevelEvent event1 = new RenderJSRenderLevelEvent(event);
        event1.getGuiGraphics().pose = event.getPoseStack();
        RenderJSEvents.RENDER_LEVEL.post(event1);
        event.getPoseStack().popPose();
        event1.restColor();
    }


    @Override
    public RenderLevelStageEvent getEvent() {
        return event;
    }

    @Override
    public PoseStack getPoseStack() {
        return event.getPoseStack();
    }

    GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());

    @Override
    public GuiGraphics getGuiGraphics() {
        return guiGraphics;
    }
}
