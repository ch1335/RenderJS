package com.chen1335.renderjs.client.events;

import com.chen1335.renderjs.API.IGuiRenderHelper;
import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecorator;
import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class ItemDecorationsRegisterEvent extends EventJS {
    @Deprecated
    @Info("don't use this,This method will be removed in the future")
    public void register(Item item, String id, Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        this.register(item,consumer);
    }

    @Deprecated
    @Info("don't use this,This method will be removed in the future")
    public void registerForAllItem(String id, Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        this.registerForAllItem(consumer);
    }

    @Info("Register an ItemDecorator, and if it has already been registered, return the previously registered ItemDecorator. \nWhen reloading, the new content will be automatically updated to the corresponding ItemDecorator")
    public void register(Item item, Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        RenderJSItemDecoratorHandler.getInstance().register(item, consumer);
    }

    @Info("Register an ItemDecorator for all items")
    public void registerForAllItem(Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        RenderJSItemDecoratorHandler.getInstance().registerForAllItem(consumer);
    }

}
