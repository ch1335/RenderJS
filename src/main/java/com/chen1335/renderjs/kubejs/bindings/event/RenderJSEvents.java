package com.chen1335.renderjs.kubejs.bindings.event;

import com.chen1335.renderjs.client.events.AddGuiRenderEvent;
import com.chen1335.renderjs.client.events.AddWorldRenderEvent;
import com.chen1335.renderjs.client.events.ItemDecorationsRegisterEvent;
import com.chen1335.renderjs.client.events.RenderJSRenderLivingEvent;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface RenderJSEvents {
    EventGroup MAIN_GROUP = EventGroup.of("RenderJSEvents");

    EventHandler REGISTER_ITEM_DECORATIONS = MAIN_GROUP.client("RegisterItemDecorations", () -> ItemDecorationsRegisterEvent.class);

    EventHandler ADD_GUI_RENDER = MAIN_GROUP.client("AddGuiRender", () -> AddGuiRenderEvent.class);

    EventHandler ADD_WORLD_RENDER = MAIN_GROUP.client("AddWorldRender", () -> AddWorldRenderEvent.class);

    EventHandler LIVING_RENDER = MAIN_GROUP.client("onLivingRender", () -> RenderJSRenderLivingEvent.class);


}
