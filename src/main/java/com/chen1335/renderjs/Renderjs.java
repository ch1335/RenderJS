package com.chen1335.renderjs;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

@Mod(Renderjs.MODID)
public class Renderjs {
    public static final String MODID = "renderjs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean clientSetup = false;
    public Renderjs(IEventBus modEventBus, ModContainer modContainer) {

    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            Renderjs.clientSetup = true;
        }
    }
}
