package com.chen1335.renderjs.kubejs;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import com.chen1335.renderjs.client.events.ItemDecorationsRegisterEvent;
import com.chen1335.renderjs.client.events.renderEvent.RegisterBlockEntityRenderEvent;
import com.chen1335.renderjs.client.events.renderEvent.RenderJSRenderGuiEvent;
import com.chen1335.renderjs.client.renderer.RenderJSBlockEntityRenderer;
import com.chen1335.renderjs.client.renderer.RenderJSRenderType;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.chen1335.renderjs.kubejs.client.RenderJSRenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.concurrent.CompletableFuture;


public class RenderJSPlugin implements KubeJSPlugin {

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(RenderJSEvents.GROUP);
    }

    @Override
    public void init() {

    }

    @Override
    public void afterScriptsLoaded(ScriptManager manager) {
        if (manager.scriptType.isClient()) {
            CompletableFuture<Void> submit = Minecraft.getInstance().submit(
                    () -> {
                        RenderJSItemDecoratorHandler.clearRender();
                        RenderJSEvents.REGISTER_ITEM_DECORATIONS.post(new ItemDecorationsRegisterEvent());
                        RenderJSEvents.REGISTER_BLOCK_ENTITY_RENDER.post(new RegisterBlockEntityRenderEvent(null));

                        RenderJSRenderGuiEvent.SUSPENDED = false;
                    }
            );
        }
    }

    @Override
    public void registerBindings(BindingRegistry bindingRegistry) {
        bindingRegistry.add("RenderJSItemDecoratorHandler", RenderJSItemDecoratorHandler.getInstance());
        bindingRegistry.add("RenderJSRenderSystem", RenderJSRenderSystem.class);
        bindingRegistry.add("PoseStack", PoseStack.class);
        bindingRegistry.add("Mth", Mth.class);
        bindingRegistry.add("LightTexture", LightTexture.class);
        bindingRegistry.add("LevelRenderer", LevelRenderer.class);
        bindingRegistry.add("GuiGraphics", GuiGraphics.class);
        bindingRegistry.add("GuiRenderHelper", IGuiRenderHelper.guiRenderHelper);
        bindingRegistry.add("LevelRenderHelper", ILevelRenderHelper.levelRenderHelper);
        bindingRegistry.add("RenderJSBlockEntityRenderer", RenderJSBlockEntityRenderer.class);
        bindingRegistry.add("RenderJSRenderType", RenderJSRenderType.class);
        bindingRegistry.add("RenderJSLevelRenderStage", RenderLevelStageEvent.Stage.class);
    }
}
