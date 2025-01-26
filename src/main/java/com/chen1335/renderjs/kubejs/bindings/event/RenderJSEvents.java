package com.chen1335.renderjs.kubejs.bindings.event;

import com.chen1335.renderjs.client.events.*;
import com.chen1335.renderjs.client.events.renderEvent.RenderJSRenderGuiEvent;
import com.chen1335.renderjs.client.events.renderEvent.RenderJSRenderLevelEvent;
import com.chen1335.renderjs.client.events.renderEvent.RenderJSRenderLivingEvent;
import com.chen1335.renderjs.client.events.renderEvent.RenderJSRenderScreenEvent;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface RenderJSEvents {
    EventGroup MAIN_GROUP = EventGroup.of("RenderJSEvents");

    EventHandler REGISTER_ITEM_DECORATIONS = MAIN_GROUP.client("RegisterItemDecorations", () -> ItemDecorationsRegisterEvent.class);

    EventHandler ADD_GUI_RENDER = MAIN_GROUP.client("AddGuiRender", () -> AddGuiRenderEvent.class);

    EventHandler ADD_WORLD_RENDER = MAIN_GROUP.client("AddWorldRender", () -> AddWorldRenderEvent.class);

    EventHandler LIVING_RENDER_PRE = MAIN_GROUP.client("onLivingPreRender", () -> RenderJSRenderLivingEvent.class);

    EventHandler LIVING_RENDER_Post = MAIN_GROUP.client("onLivingPostRender", () -> RenderJSRenderLivingEvent.class);

    EventHandler RENDER_GUI_PRE = MAIN_GROUP.client("onGuiPreRender", () -> RenderJSRenderGuiEvent.class);

    EventHandler RENDER_GUI_POST = MAIN_GROUP.client("onGuiPostRender", () -> RenderJSRenderGuiEvent.class);

    EventHandler RENDER_SCREEN_PRE = MAIN_GROUP.client("onScreenPreRender", () -> RenderJSRenderScreenEvent.class);

    EventHandler RENDER_SCREEN_POST = MAIN_GROUP.client("onScreenPostRender", () -> RenderJSRenderScreenEvent.class);

    EventHandler RENDER_LEVEL = MAIN_GROUP.client("onLevelRender", () -> RenderJSRenderLevelEvent.class);

}
