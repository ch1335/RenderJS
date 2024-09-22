package com.chen1335.renderjs;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import com.chen1335.renderjs.client.RenderJSGUI;
import com.chen1335.renderjs.client.RenderJSWorldRender;
import com.chen1335.renderjs.client.events.AddGuiRenderEvent;
import com.chen1335.renderjs.client.events.AddWorldRenderEvent;
import com.chen1335.renderjs.client.events.ItemDecorationsRegisterEvent;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(RenderJS.MODID)
public class RenderJS {
    public static final String MODID = "renderjs";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static boolean CAN_RENDER = true;

    public static GuiGraphics guiGraphics;

    public static boolean CLIENT_INIT = false;
    public static RenderJSItemDecoratorHandler itemDecoratorHandler;

    public RenderJS() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void reloadRenders() {
        RenderJSGUI.clearRender();
        RenderJSWorldRender.clearRender();
        RenderJSItemDecoratorHandler.clearRender();
        RenderJSEvents.REGISTER_ITEM_DECORATIONS.post(new ItemDecorationsRegisterEvent());
        RenderJSEvents.ADD_GUI_RENDER.post(new AddGuiRenderEvent());
        RenderJSEvents.ADD_WORLD_RENDER.post(new AddWorldRenderEvent());
        RenderJSGUI.reload();
        RenderJSWorldRender.reload();
    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            RenderJSGUI.instance = new RenderJSGUI(Minecraft.getInstance());
            RenderJSWorldRender.init();
            guiGraphics = new GuiGraphics(Minecraft.getInstance(),Minecraft.getInstance().renderBuffers().bufferSource());
            CLIENT_INIT = true;
            if (ModList.get().isLoaded("probejs")) {
                LOGGER.info("You have Probejs! Good!");
            }
            reloadRenders();
        }
    }
}
