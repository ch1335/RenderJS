package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.IRenderJSPoseStackHelper;
import com.chen1335.renderjs.API.IScreenRenderEvent;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.client.renderer.RenderJSPoseStackUtil;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.joml.Matrix3x2fStack;


@Info("Screen render event context. Fires before and after a screen is rendered.")
@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID)
public class RenderJSRenderScreenEvent implements ClientKubeEvent, IGuiRenderHelper, IScreenRenderEvent, IRenderJSPoseStackHelper {

    protected final GuiGraphicsExtractor guiGraphics;
    protected final Matrix3x2fStack poseStack;
    protected final int mouseX;
    protected final int mouseY;
    protected final float partialTick;
    protected final Screen screen;

    public RenderJSRenderScreenEvent(GuiGraphicsExtractor guiGraphics, Matrix3x2fStack poseStack, int mouseX, int mouseY, float partialTick, Screen screen) {
        this.guiGraphics = guiGraphics;
        this.poseStack = poseStack;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.partialTick = partialTick;
        this.screen = screen;
    }

    @SubscribeEvent
    public static void RenderGuiEvent$Pre(ScreenEvent.Render.Pre event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSRenderScreenEvent kubeEvent = fromEvent(event);
        int depth = RenderJSPoseStackUtil.depth(kubeEvent.getPoseStack());
        RenderJSEvents.RENDER_SCREEN_PRE.post(kubeEvent);
        RenderJSPoseStackUtil.checkBalanced("onScreenPreRender", kubeEvent.getPoseStack(), depth);
    }

    @SubscribeEvent
    public static void RenderGuiEvent$Post(ScreenEvent.Render.Post event) {
        if (!RenderJS.CAN_RENDER) {
            return;
        }
        RenderJSRenderScreenEvent kubeEvent = fromEvent(event);
        int depth = RenderJSPoseStackUtil.depth(kubeEvent.getPoseStack());
        RenderJSEvents.RENDER_SCREEN_POST.post(kubeEvent);
        RenderJSPoseStackUtil.checkBalanced("onScreenPostRender", kubeEvent.getPoseStack(), depth);
    }

    private static RenderJSRenderScreenEvent fromEvent(ScreenEvent.Render event) {
        GuiGraphicsExtractor guiGraphics = event.getGuiGraphics();
        return new RenderJSRenderScreenEvent(
                guiGraphics,
                RenderJSPoseStackUtil.copy(guiGraphics.pose()),
                event.getMouseX(),
                event.getMouseY(),
                event.getPartialTick(),
                event.getScreen()
        );
    }

    @Override
    public GuiGraphicsExtractor getGuiGraphics() {
        return guiGraphics;
    }

    @Override
    public Matrix3x2fStack getPoseStack() {
        return poseStack;
    }

    @Override
    @Info("Returns the horizontal position of the mouse, in GUI pixels.")
    public int getMouseX() {
        return mouseX;
    }

    @Override
    @Info("Returns the vertical position of the mouse, in GUI pixels.")
    public int getMouseY() {
        return mouseY;
    }

    @Override
    @Info("Returns the partial tick of the current frame.")
    public float getPartialTick() {
        return partialTick;
    }

    @Override
    @Info("Returns the screen currently being rendered.")
    public Screen getScreen() {
        return screen;
    }
}
