package com.chen1335.renderjs.kubejs.bindings.event;

import com.chen1335.renderjs.client.events.AddGuiRenderEvent;
import com.chen1335.renderjs.client.events.AddWorldRenderEvent;
import com.chen1335.renderjs.client.events.ItemDecorationsRegisterEvent;
import com.chen1335.renderjs.client.events.renderEvent.RenderJSRenderGuiEvent;
import com.chen1335.renderjs.client.events.renderEvent.RenderJSRenderLevelEvent;
import com.chen1335.renderjs.client.events.renderEvent.RenderJSRenderScreenEvent;
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


    EventHandler LIVING_RENDER_PRE = GROUP.client("onLivingPreRender", () -> com.chen1335.renderjs.client.events.renderEvent.RenderJSRenderLivingEvent.class);

    EventHandler LIVING_RENDER_Post = GROUP.client("onLivingPostRender", () -> com.chen1335.renderjs.client.events.renderEvent.RenderJSRenderLivingEvent.class);

    EventHandler RENDER_GUI_PRE = GROUP.client("onGuiPreRender", () -> RenderJSRenderGuiEvent.class);

    EventHandler RENDER_GUI_POST = GROUP.client("onGuiPostRender", () -> RenderJSRenderGuiEvent.class);

    EventHandler RENDER_SCREEN_PRE = GROUP.client("onScreenPreRender", () -> RenderJSRenderScreenEvent.class);

    EventHandler RENDER_SCREEN_POST = GROUP.client("onScreenPostRender", () -> RenderJSRenderScreenEvent.class);

    EventHandler RENDER_LEVEL = GROUP.client("onLevelRender", () -> RenderJSRenderLevelEvent.class);
}
