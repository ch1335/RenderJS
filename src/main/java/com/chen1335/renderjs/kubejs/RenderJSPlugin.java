package com.chen1335.renderjs.kubejs;

import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import com.chen1335.renderjs.client.events.ItemDecorationsRegisterEvent;
import com.chen1335.renderjs.client.events.renderEvent.RegisterBlockEntityRenderEvent;
import com.chen1335.renderjs.client.renderer.RenderJSBlockEntityRenderer;
import com.chen1335.renderjs.client.renderer.RenderJSRenderType;
import com.chen1335.renderjs.kubejs.bindings.RenderJSLevelRenderStage;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.chen1335.renderjs.kubejs.client.RenderJSRenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.neoforged.fml.loading.FMLEnvironment;


public class RenderJSPlugin implements KubeJSPlugin {

    private static boolean PENDING_CLIENT_INIT = false;

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(RenderJSEvents.GROUP);
    }

    @Override
    public void init() {

    }

    @Override
    public void afterScriptsLoaded(ScriptManager manager) {
        if (!manager.scriptType.isClient()) {
            return;
        }
        if (Minecraft.getInstance() == null) {
            // First client script load happens inside the KubeJS @Mod constructor, before Minecraft exists.
            PENDING_CLIENT_INIT = true;
            return;
        }
        Minecraft.getInstance().execute(RenderJSPlugin::initClientScripts);
    }

    @Override
    public void afterInit() {
        if (!PENDING_CLIENT_INIT) {
            return;
        }
        PENDING_CLIENT_INIT = false;
        if (FMLEnvironment.getDist().isClient()) {
            Minecraft.getInstance().execute(RenderJSPlugin::initClientScripts);
        }
    }

    private static void initClientScripts() {
        RenderJSItemDecoratorHandler.clearRender();
        RenderJSEvents.REGISTER_ITEM_DECORATIONS.post(new ItemDecorationsRegisterEvent());
        RenderJSEvents.REGISTER_BLOCK_ENTITY_RENDER.post(new RegisterBlockEntityRenderEvent(null));

        RenderJS.CAN_RENDER = true;
    }

    @Override
    public void registerBindings(BindingRegistry bindingRegistry) {
        bindingRegistry.add("RenderJSItemDecoratorHandler", RenderJSItemDecoratorHandler.getInstance());
        bindingRegistry.add("RenderJSRenderSystem", RenderJSRenderSystem.class);
        bindingRegistry.add("PoseStack", PoseStack.class);
        bindingRegistry.add("Mth", Mth.class);
        bindingRegistry.add("LightCoordsUtil", LightCoordsUtil.class);
        bindingRegistry.add("LevelRenderer", LevelRenderer.class);
        bindingRegistry.add("GuiGraphicsExtractor", GuiGraphicsExtractor.class);
        bindingRegistry.add("RenderJSBlockEntityRenderer", RenderJSBlockEntityRenderer.class);
        bindingRegistry.add("RenderJSRenderType", RenderJSRenderType.class);
        bindingRegistry.add("RenderJSLevelRenderStage", RenderJSLevelRenderStage.class);
    }
}
