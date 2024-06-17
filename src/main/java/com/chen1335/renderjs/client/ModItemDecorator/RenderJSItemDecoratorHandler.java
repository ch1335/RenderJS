package com.chen1335.renderjs.client.ModItemDecorator;

import com.chen1335.renderjs.client.events.ItemDecorationsRegisterEvent;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class RenderJSItemDecoratorHandler {
    @HideFromJS
    private static final RenderJSItemDecoratorHandler instance = new RenderJSItemDecoratorHandler();
    @HideFromJS
    private static final Map<Item, Map<String, RenderJSItemDecorator>> REGISTERED_ITEM_DECORATOR = new HashMap<>();

    @HideFromJS
    public static final Map<String, RenderJSItemDecorator> REGISTERED_ALL_ITEM_DECORATOR = new HashMap<>();

    @HideFromJS
    public static RenderJSItemDecoratorHandler getInstance() {
        return instance;
    }

    @HideFromJS
    public static void RegisterItemDecorationsEvent(RegisterItemDecorationsEvent event) {
        RenderJSEvents.REGISTER_ITEM_DECORATIONS.post(new ItemDecorationsRegisterEvent());

        REGISTERED_ITEM_DECORATOR.forEach((Item item, Map<String, RenderJSItemDecorator> map) -> {
            Iterator<RenderJSItemDecorator> iterator = map.values().iterator();
            if (iterator.hasNext()) {
                event.register(item, iterator.next());
            }
        });
    }

    @HideFromJS
    public RenderJSItemDecorator register(Item item, String id, Consumer<RenderJSItemDecorator.renderContext> consumer) {

        RenderJSItemDecorator renderJSItemDecorator = new RenderJSItemDecorator(consumer);
        if (REGISTERED_ITEM_DECORATOR.containsKey(item) && REGISTERED_ITEM_DECORATOR.get(item).containsKey(id)) {
            REGISTERED_ITEM_DECORATOR.get(item).get(id).setRender(consumer);
            return REGISTERED_ITEM_DECORATOR.get(item).get(id);
        }

        if (!REGISTERED_ITEM_DECORATOR.containsKey(item)) {
            REGISTERED_ITEM_DECORATOR.put(item, new HashMap<>());
        }

        REGISTERED_ITEM_DECORATOR.get(item).put(id, renderJSItemDecorator);
        return renderJSItemDecorator;
    }
    @HideFromJS
    public RenderJSItemDecorator  registerForAllItem(String id, Consumer<RenderJSItemDecorator.renderContext> consumer) {
        if (REGISTERED_ALL_ITEM_DECORATOR.containsKey(id)) {
            return REGISTERED_ALL_ITEM_DECORATOR.get(id);
        }
        RenderJSItemDecorator renderJSItemDecorator = new RenderJSItemDecorator(consumer);
        REGISTERED_ALL_ITEM_DECORATOR.put(id,renderJSItemDecorator);
        return renderJSItemDecorator;
    }
}
