package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.IGuiRenderEvent;
import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.client.renderer.RenderJSPoseStackUtil;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Matrix3x2fStack;


@Info("HUD render event context. Fires before and after the HUD is rendered.")
@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID)
public class RenderJSRenderGuiEvent implements ClientKubeEvent, IGuiRenderHelper, IGuiRenderEvent, IRenderJSPoseStackHelper {
    protected final GuiGraphicsExtractor guiGraphics;
    protected final Matrix3x2fStack poseStack;
    protected final float partialTick;

    public RenderJSRenderGuiEvent(GuiGraphicsExtractor guiGraphics, Matrix3x2fStack poseStack, float partialTick) {
        this.guiGraphics = guiGraphics;
        this.poseStack = poseStack;
        this.partialTick = partialTick;
    }

    @SubscribeEvent
    public static void RenderGuiEvent$Pre(RenderGuiEvent.Pre event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSRenderGuiEvent kubeEvent = fromEvent(event);
        RenderJSEvents.RENDER_GUI_PRE.post(kubeEvent);
        RenderJSPoseStackUtil.checkBalanced("onRenderGuiPre", kubeEvent.getPoseStack(), 0);
    }

    @SubscribeEvent
    public static void RenderGuiEvent$Post(RenderGuiEvent.Post event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSRenderGuiEvent kubeEvent = fromEvent(event);
        RenderJSEvents.RENDER_GUI_POST.post(kubeEvent);
        RenderJSPoseStackUtil.checkBalanced("onRenderGuiPost", kubeEvent.getPoseStack(), 0);
    }

    private static RenderJSRenderGuiEvent fromEvent(RenderGuiEvent event) {
        GuiGraphicsExtractor guiGraphics = event.getGuiGraphics();
        return new RenderJSRenderGuiEvent(
                guiGraphics,
                RenderJSPoseStackUtil.copy(guiGraphics.pose()),
                event.getPartialTick().getGameTimeDeltaPartialTick(true)
        );
    }

    @Override
    public Matrix3x2fStack getPoseStack() {
        return poseStack;
    }

    @Override
    public GuiGraphicsExtractor getGuiGraphics() {
        return guiGraphics;
    }

    @Override
    @Info("Returns the partial tick of the current frame.")
    public float getPartialTick() {
        return partialTick;
    }
}
