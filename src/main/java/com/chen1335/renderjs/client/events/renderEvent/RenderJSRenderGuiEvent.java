package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.IGuiRenderEvent;
import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderJSRenderGuiEvent extends ClientEventJS implements IGuiRenderHelper, IGuiRenderEvent, IRenderJSPoseStackHelper {

    protected final RenderGuiEvent event;

    public RenderJSRenderGuiEvent(RenderGuiEvent event) {
        this.event = event;
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderGuiEvent$Pre(RenderGuiEvent.Pre event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        event.getGuiGraphics().pose().pushPose();
        RenderJSEvents.RENDER_GUI_PRE.post(new RenderJSRenderGuiEvent(event));
        event.getGuiGraphics().pose().popPose();
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderGuiEvent$Post(RenderGuiEvent.Post event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        event.getGuiGraphics().pose().pushPose();
        RenderJSEvents.RENDER_GUI_POST.post(new RenderJSRenderGuiEvent(event));
        event.getGuiGraphics().pose().popPose();
    }


    @Override
    public RenderGuiEvent getEvent() {
        return event;
    }

    @Override
    public PoseStack getPoseStack() {
        return event.getGuiGraphics().pose();
    }

    @Override
    public GuiGraphics getGuiGraphics() {
        return event.getGuiGraphics();
    }
}
