package com.chen1335.renderjs.client.events;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecorator;
import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class ItemDecorationsRegisterEvent extends EventJS {
    @Info("注册一个ItemDecorator,如果之前已经注册则返回之前注册的ItemDecorator,reload时会自动将新内容更新到对应的ItemDecorator")
    public RenderJSItemDecorator register(Item item, String id, Consumer<RenderJSItemDecorator.renderContext> consumer) {
        return RenderJSItemDecoratorHandler.getInstance().register(item, id, consumer);
    }
}
