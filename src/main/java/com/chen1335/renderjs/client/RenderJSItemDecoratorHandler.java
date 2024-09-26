package com.chen1335.renderjs.client;

import com.chen1335.renderjs.kubejs.bindings.event.RenderJSClientEvents;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSRegisterItemDecorationsEvent;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.GlStateBackup;
import net.neoforged.neoforge.client.IItemDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderJSItemDecoratorHandler {
    private final List<IItemDecorator> itemDecorators;

    public static RenderJSItemDecoratorHandler GLOBAL_DECORATOR_LOOKUP = new RenderJSItemDecoratorHandler();
    private final GlStateBackup stateBackup = new GlStateBackup();
    private static Map<Item, RenderJSItemDecoratorHandler> DECORATOR_LOOKUP = ImmutableMap.of();

    public RenderJSItemDecoratorHandler(){
        this.itemDecorators = ImmutableList.of();
    }

    public RenderJSItemDecoratorHandler(List<IItemDecorator> itemDecorators){
        this.itemDecorators = itemDecorators;
    }

    public static void init(){
        var decorators = new HashMap<Item, List<IItemDecorator>>();
        var globalDecorators = new ArrayList<IItemDecorator>();
        RenderJSClientEvents.RENDERJS_REGISTER_ITEM_DECORATIONS_EVENT.post(new RenderJSRegisterItemDecorationsEvent(decorators,globalDecorators));
        var builder = new ImmutableMap.Builder<Item, RenderJSItemDecoratorHandler>();
        decorators.forEach((item, itemDecorators) -> builder.put(item, new RenderJSItemDecoratorHandler(itemDecorators)));
        GLOBAL_DECORATOR_LOOKUP = new RenderJSItemDecoratorHandler(globalDecorators);
        DECORATOR_LOOKUP = builder.build();
    }
    private static final RenderJSItemDecoratorHandler EMPTY = new RenderJSItemDecoratorHandler();

    public static RenderJSItemDecoratorHandler of(Item item){
        return DECORATOR_LOOKUP.getOrDefault(item,EMPTY);
    }

    public void render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        RenderSystem.backupGlState(stateBackup);

        resetRenderState();
        for (IItemDecorator itemDecorator : itemDecorators) {
            if (itemDecorator.render(guiGraphics, font, stack, xOffset, yOffset))
                resetRenderState();
        }

        RenderSystem.restoreGlState(stateBackup);
    }

    private void resetRenderState() {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }
}
