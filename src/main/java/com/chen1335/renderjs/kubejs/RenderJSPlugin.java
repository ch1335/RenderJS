package com.chen1335.renderjs.kubejs;

import com.chen1335.renderjs.Renderjs;
import com.chen1335.renderjs.client.RenderJSBlockEntityRenderer;
import com.chen1335.renderjs.client.RenderJSGuiRenderHelper;
import com.chen1335.renderjs.client.RenderJSItemDecoratorHandler;
import com.chen1335.renderjs.client.RenderJSWorldRenderHelper;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSBlockEntityRenderReloadEvent;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSClientEvents;
import com.mojang.math.Axis;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.client.renderer.LevelRenderer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.model.data.ModelData;

public class RenderJSPlugin implements KubeJSPlugin {

    @Override
    public void init() {
        if (ModList.get().isLoaded("probejs")) {
            Renderjs.LOGGER.info("YOU HAVE PROBEJS!");
        }
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(RenderJSClientEvents.GROUP);
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        if (ScriptType.STARTUP_OR_CLIENT.test(bindings.type())) {
            bindings.add("RenderJSBlockEntityRenderer", RenderJSBlockEntityRenderer.class);
            bindings.add("RenderJSGuiRenderHelper", RenderJSGuiRenderHelper.class);
            bindings.add("RenderJSLevelRenderHelper", RenderJSWorldRenderHelper.class);
            bindings.add("RenderJSLevelRenderHelper", RenderJSWorldRenderHelper.class);
            bindings.add("LevelRenderer", LevelRenderer.class);
            bindings.add("ModelData", ModelData.class);
            bindings.add("Axis", Axis.class);
        }
    }

    @Override
    public void afterScriptsLoaded(ScriptManager manager) {
        if (manager.scriptType == ScriptType.CLIENT) {
            Renderjs.LOGGER.info("Client Scripts loaded");
            RenderJSItemDecoratorHandler.init();
            if (Renderjs.clientSetup) {
                RenderJSClientEvents.RENDERJS_BLOCK_ENTITY_RENDER_RELOAD_EVENT.post(new RenderJSBlockEntityRenderReloadEvent());
            }
        }
    }
}
