package com.chen1335.renderjs.client.events;

import com.chen1335.renderjs.client.RenderJSWorldRender;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;

import java.util.function.Consumer;

public class AddWorldRenderEvent extends EventJS {
    @Info("添加render")
    public void addWorldRender(Consumer<RenderJSWorldRender.RenderContext> consumer) {
        RenderJSWorldRender.getInstance().addWorldRender(consumer);
    }
}
