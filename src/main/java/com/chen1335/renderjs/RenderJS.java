package com.chen1335.renderjs;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import com.chen1335.renderjs.client.events.ItemDecorationsRegisterEvent;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RenderJS.MODID)
public class RenderJS {
    public static final String MODID = "renderjs";
    public static boolean CLIENT_INIT = false;
    public static boolean CAN_RENDER = true;
    public static final Logger LOGGER = LogUtils.getLogger();
    public static RenderJSItemDecoratorHandler itemDecoratorHandler;
    public static IEventBus MOD_EVENT_BUS;


    public RenderJS(IEventBus modEventBus, ModContainer modContainer) {
        MOD_EVENT_BUS = modEventBus;
    }

    public static void reloadRenders() {
        RenderJSItemDecoratorHandler.clearRender();
        RenderJSEvents.REGISTER_ITEM_DECORATIONS.post(new ItemDecorationsRegisterEvent());
    }


    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            CLIENT_INIT = true;
            if (ModList.get().isLoaded("probejs")) {
                LOGGER.info("You have Probejs! Good!");
            }
        }
    }
}
