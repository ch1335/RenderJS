package com.chen1335.renderjs.kubejs;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.API.ILevelRenderHelper;
import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import com.chen1335.renderjs.client.RenderJSGUI;
import com.chen1335.renderjs.client.RenderJSWorldRender;
import com.chen1335.renderjs.client.renderer.RenderJSBlockEntityRenderer;
import com.chen1335.renderjs.client.renderer.RenderJSRenderType;
import com.chen1335.renderjs.kubejs.bindings.RenderJSUtils;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.chen1335.renderjs.kubejs.client.RenderJSRenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RenderJSPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        RenderJSEvents.GROUP.register();
    }

    @Override
    public void clientInit() {
        RenderJS.itemDecoratorHandler = new RenderJSItemDecoratorHandler();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(RenderJS.itemDecoratorHandler::RegisterItemDecorationsEvent);
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (!event.getType().isServer()) {
            event.add("RenderJSGUI", RenderJSGUI.getInstance());
            event.add("RenderJSItemDecoratorHandler", RenderJSItemDecoratorHandler.getInstance());
            event.add("RenderJSRenderSystem", RenderJSRenderSystem.class);
            event.add("RenderJSUtils", RenderJSUtils.class);
            event.add("RenderJSWorldRender", RenderJSWorldRender.class);
            event.add("PoseStack", PoseStack.class);
            event.add("Mth", Mth.class);
            event.add("LightTexture", LightTexture.class);
            event.add("LevelRenderer", LevelRenderer.class);
            event.add("GuiGraphics", GuiGraphics.class);
            event.add("GuiRenderHelper", IGuiRenderHelper.guiRenderHelper);
            event.add("LevelRenderHelper", ILevelRenderHelper.levelRenderHelper);
            event.add("RenderJSBlockEntityRenderer", RenderJSBlockEntityRenderer.class);
            event.add("RenderJSRenderType", RenderJSRenderType.class);
            event.add("RenderJSLevelRenderStage", RenderLevelStageEvent.Stage.class);
        }
    }
}
