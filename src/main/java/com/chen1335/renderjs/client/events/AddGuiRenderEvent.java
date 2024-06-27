package com.chen1335.renderjs.client.events;

import com.chen1335.renderjs.client.RenderJSGUI;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;

import java.util.function.Consumer;

public class AddGuiRenderEvent extends EventJS {
    @Info("添加render")
    public void addRender(Consumer<RenderJSGUI.renderContext> consumer) {
        RenderJSGUI.getInstance().addRender(consumer);
    }
}
