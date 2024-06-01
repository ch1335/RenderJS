package com.chen1335.renderjs.client.ModItemDecorator;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class RenderJSItemDecoratorHandler {

    @HideFromJS
    private static final RenderJSItemDecoratorHandler instance = new RenderJSItemDecoratorHandler();

    private static final Map<Item, Map<String, RenderJSItemDecorator>> REGISTERED_ITEM_DECORATOR = new HashMap<>();

    @Info("注册一个ItemDecorator,如果之前已经注册则返回之前注册的ItemDecorator")
    public RenderJSItemDecorator register(Item item, String id, Consumer<RenderJSItemDecorator.renderContext> consumer) {

        RenderJSItemDecorator renderJSItemDecorator = new RenderJSItemDecorator(consumer);
        if (REGISTERED_ITEM_DECORATOR.containsKey(item) && REGISTERED_ITEM_DECORATOR.get(item).containsKey(id)) {
            return REGISTERED_ITEM_DECORATOR.get(item).get(id);
        }

        if (!REGISTERED_ITEM_DECORATOR.containsKey(item)) {
            REGISTERED_ITEM_DECORATOR.put(item, new HashMap<>());
        }

        REGISTERED_ITEM_DECORATOR.get(item).put(id, renderJSItemDecorator);
        return renderJSItemDecorator;
    }

    @HideFromJS
    public static RenderJSItemDecoratorHandler getInstance() {
        return instance;
    }


    public static void RegisterItemDecorationsEvent(RegisterItemDecorationsEvent event) {
        REGISTERED_ITEM_DECORATOR.forEach((Item item, Map<String, RenderJSItemDecorator> map) -> {
            Iterator<RenderJSItemDecorator> iterator = map.values().iterator();
            if (iterator.hasNext()) {
                event.register(item, iterator.next());
            }
        });
    }
}
