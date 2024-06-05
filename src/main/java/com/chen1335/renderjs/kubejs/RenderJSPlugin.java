package com.chen1335.renderjs.kubejs;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import com.chen1335.renderjs.client.RenderJSGUI;
import com.chen1335.renderjs.client.RenderJSWorldRender;
import com.chen1335.renderjs.kubejs.bindings.RenderJSUtils;
import com.chen1335.renderjs.kubejs.client.RenderJSRenderSystem;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class RenderJSPlugin extends KubeJSPlugin {
    @Override
    public void clearCaches() {
        RenderJSGUI.clearRender();
        RenderJSWorldRender.clearRender();

    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (event.getType().isClient()) {
            event.add("RenderJSGUI", RenderJSGUI.getInstance());
            event.add("RenderJSItemDecoratorHandler", RenderJSItemDecoratorHandler.getInstance());
            event.add("RenderJSRenderSystem", RenderJSRenderSystem.class);
            event.add("RenderJSUtils", RenderJSUtils.class);
            event.add("RenderJSWorldRender", RenderJSWorldRender.getInstance());
        }
    }
}
