package com.chen1335.renderjs;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import net.neoforged.fml.ModList;

public class RenderJSClient {
    public static RenderJSItemDecoratorHandler ITEM_DECORATOR_HANDLER = new RenderJSItemDecoratorHandler();


    public static void setup() {
        if (ModList.get().isLoaded("probejs")) {
            RenderJS.LOGGER.info("You have Probejs! Good!");
        }
    }
}
