package com.chen1335.renderjs.kubejs;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import com.chen1335.renderjs.client.RenderJSGUI;
import com.chen1335.renderjs.client.RenderJSWorldRender;
import com.chen1335.renderjs.client.events.AddGuiRenderEvent;
import com.chen1335.renderjs.client.events.AddWorldRenderEvent;
import com.chen1335.renderjs.client.renderer.RenderJSBlockEntityRenderer;
import com.chen1335.renderjs.kubejs.bindings.RenderJSGuiComponent;
import com.chen1335.renderjs.kubejs.bindings.RenderJSRenderSystem;
import com.chen1335.renderjs.kubejs.bindings.RenderJSUtils;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;

public class RenderJSPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        RenderJSEvents.MAIN_GROUP.register();
    }

    @Override
    public void clientInit() {
        RenderJSEvents.ADD_GUI_RENDER.post(new AddGuiRenderEvent());
        RenderJSEvents.ADD_WORLD_RENDER.post(new AddWorldRenderEvent());
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (!event.getType().isServer()) {
            event.add("RenderJSGUI", RenderJSGUI.getInstance());
            event.add("RenderJSItemDecoratorHandler", RenderJSItemDecoratorHandler.class);
            event.add("RenderJSRenderSystem", RenderJSRenderSystem.class);
            event.add("RenderJSUtils", RenderJSUtils.class);
            event.add("RenderJSWorldRender", RenderJSWorldRender.class);
            event.add("RenderJSGuiComponent", RenderJSGuiComponent.class);
            event.add("PoseStack", PoseStack.class);
            event.add("Mth", Mth.class);
            event.add("RenderJSBlockEntityRenderer", RenderJSBlockEntityRenderer.class);
            event.add("LightTexture", LightTexture.class);
            event.add("LevelRenderer", LevelRenderer.class);
        }
    }
}
