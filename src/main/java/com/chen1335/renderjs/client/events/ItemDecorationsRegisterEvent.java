package com.chen1335.renderjs.client.events;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecorator;
import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class ItemDecorationsRegisterEvent extends EventJS {
    @Info("为某个物品注册一个ItemDecorator\nRegister an ItemDecorator, and if it has already been registered, return the previously registered ItemDecorator. \nWhen reloading, the new content will be automatically updated to the corresponding ItemDecorator")
    public void register(Item item, Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        RenderJSItemDecoratorHandler.getInstance().register(item, consumer);
    }

    @Info("为所有物品注册一个ItemDecorator\nRegister an ItemDecorator for all items")
    public void registerForAllItem(Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        RenderJSItemDecoratorHandler.getInstance().registerForAllItem(consumer);
    }
}
