package com.chen1335.renderjs;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;

/**
 * Client side bootstrap of RenderJS.
 */
public class RenderJSClient {
    public static final RenderJSItemDecoratorHandler ITEM_DECORATOR_HANDLER = new RenderJSItemDecoratorHandler();

    public static void setup() {
        RenderJS.LOGGER.info("RenderJS client setup done");
    }
}
