package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.API.IRenderJSEvent;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.client.ClientEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderJSRenderLivingEvent extends ClientEventJS implements IGuiRenderHelper, ILevelRenderHelper, IRenderJSPoseStackHelper, IRenderJSEvent {

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
