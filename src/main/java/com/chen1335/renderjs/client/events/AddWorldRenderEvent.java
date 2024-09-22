package com.chen1335.renderjs.client.events;

import com.chen1335.renderjs.client.RenderJSWorldRender;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;

import java.util.function.Consumer;

public class AddWorldRenderEvent extends EventJS {

    @Deprecated
    @Info("Add Render, has deprecated and will be remove in the future,don's use this")
    public void addWorldRender(Consumer<RenderJSWorldRender.RenderContext> consumer) {
        RenderJSWorldRender.getInstance().addWorldRender(consumer);
    }
}
