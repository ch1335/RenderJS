package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.API.IRenderJSEvent;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;


@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RenderJSRenderLivingEvent implements ClientKubeEvent, IGuiRenderHelper, ILevelRenderHelper, IRenderJSPoseStackHelper, IRenderJSEvent {

    protected final RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>> event;

    public RenderJSRenderLivingEvent(RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>> event) {
        this.event = event;
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderLivingEvent$Pre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSRenderLivingEvent e = new RenderJSRenderLivingEvent(event);
        e.getGuiGraphics().pose = event.getPoseStack();
        RenderJSEvents.LIVING_RENDER_PRE.post(e);
    }

    @SubscribeEvent
    @HideFromJS
    public static void RenderLivingEvent$Post(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSRenderLivingEvent e = new RenderJSRenderLivingEvent(event);
        e.getGuiGraphics().pose = event.getPoseStack();
        RenderJSEvents.LIVING_RENDER_Post.post(e);
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
