package com.chen1335.renderjs.kubejs.bindings.event;


import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface RenderJSClientEvents {
    EventGroup GROUP = EventGroup.of("RenderJSClientEvents");
    EventHandler RENDERJS_REGISTER_ITEM_DECORATIONS_EVENT = GROUP.client("registerItemDecorationsEvent", () -> RenderJSRegisterItemDecorationsEvent.class);

    EventHandler RENDERJS_RENDER_GUI_EVENT = GROUP.client("renderGuiEvent",()-> RenderJSRenderGuiEvent.class);

    EventHandler RENDERJS_RENDER_GUI_LAYER_EVENT = GROUP.client("renderGuiLayerEvent",()-> RenderJSRenderGuiEvent.class);

    EventHandler RENDERJS_RENDER_LEVEL_STAGE_EVENT = GROUP.client("renderLevelStageEvent",()-> RenderJSRenderLevelStageEvent.class);

    EventHandler RENDERJS_BLOCK_ENTITY_RENDER_RELOAD_EVENT = GROUP.client("blockEntityRenderReloadEvent",()-> RenderJSBlockEntityRenderReloadEvent.class);

}
