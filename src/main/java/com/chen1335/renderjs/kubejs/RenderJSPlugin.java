package com.chen1335.renderjs.kubejs;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import com.chen1335.renderjs.client.RenderJSGUI;
import com.chen1335.renderjs.kubejs.client.RenderJSRenderSystem;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class RenderJSPlugin extends KubeJSPlugin {
    @Override
    public void clearCaches() {
        RenderJSGUI.isReload=true;
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (event.getType().isClient()) {
            event.add("RenderJSGUI", RenderJSGUI.getInstance());
            event.add("RenderJSItemDecoratorHandler", RenderJSItemDecoratorHandler.getInstance());
            event.add("RenderJSRenderSystem", RenderJSRenderSystem.getInstance());
        }
    }
}