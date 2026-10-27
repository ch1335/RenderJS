package com.chen1335.renderjs.client.events;

import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecorator;
import com.chen1335.renderjs.client.ModItemDecorator.RenderJSItemDecoratorHandler;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

@Info("Registration event for item decorations. Decorations are deleted and rebuilt on every script reload.")
public class ItemDecorationsRegisterEvent implements ClientKubeEvent {
    @Deprecated
    @Info(value = "Deprecated. Use register(Item, Consumer) instead.",
            params = {
                    @Param(name = "item", value = "Item the decoration is registered for."),
                    @Param(name = "id", value = "Ignored. Kept only for source compatibility."),
                    @Param(name = "consumer", value = "Callback that draws the decoration.")
            })
    public void register(Item item, String id, Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        this.register(item, consumer);
    }

    @Deprecated
    @Info(value = "Deprecated. Use registerForAllItem(Consumer) instead.",
            params = {
                    @Param(name = "id", value = "Ignored. Kept only for source compatibility."),
                    @Param(name = "consumer", value = "Callback that draws the decoration.")
            })
    public void registerForAllItem(String id, Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        this.registerForAllItem(consumer);
    }

    @Info(value = "Registers a decoration for a single item. Re-registering the same item replaces the previous decoration.",
            params = {
                    @Param(name = "item", value = "Item the decoration is registered for."),
                    @Param(name = "consumer", value = "Callback that draws the decoration. The callback receives the item decoration render context.")
            })
    public void register(Item item, Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        RenderJSItemDecoratorHandler.getInstance().register(item, consumer);
    }

    @Info(value = "Registers a decoration for all items.",
            params = {@Param(name = "consumer", value = "Callback that draws the decoration. The callback receives the item decoration render context.")})
    public void registerForAllItem(Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        RenderJSItemDecoratorHandler.getInstance().registerForAllItem(consumer);
    }
}
