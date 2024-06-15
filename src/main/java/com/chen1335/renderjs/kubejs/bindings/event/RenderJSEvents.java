package com.chen1335.renderjs.kubejs.bindings.event;

import com.chen1335.renderjs.client.events.AddGuiRenderEvent;
import com.chen1335.renderjs.client.events.AddWorldRenderEvent;
import com.chen1335.renderjs.client.events.ItemDecorationsRegisterEvent;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface RenderJSEvents {
    EventGroup GROUP = EventGroup.of("RenderJSEvents");

    EventHandler REGISTER_ITEM_DECORATIONS = GROUP.client("RegisterItemDecorations", () -> {
        return ItemDecorationsRegisterEvent.class;
    });

    EventHandler ADD_GUI_RENDER = GROUP.client("AddGuiRender", () -> {
        return AddGuiRenderEvent.class;
    });

    EventHandler ADD_WORLD_RENDER = GROUP.client("AddWorldRender", () -> {
        return AddWorldRenderEvent.class;
    });
}
