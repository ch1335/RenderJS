package com.chen1335.renderjs;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

@Mod(RenderJS.MODID)
public class RenderJS {
    public static final String MODID = "renderjs";
    public static boolean CAN_RENDER = true;
    public static final Logger LOGGER = LogUtils.getLogger();

    public RenderJS(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::FMLClientSetupEvent);
    }


    public void FMLClientSetupEvent(FMLClientSetupEvent event) {
        RenderJSClient.setup();
    }

}
