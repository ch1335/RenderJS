package com.chen1335.renderjs.client.ModItemDecorator;

import com.chen1335.renderjs.Renderjs;
import com.chen1335.renderjs.client.events.ItemDecorationsRegisterEvent;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;

import java.util.HashMap;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class RenderJSItemDecoratorHandler {
    @HideFromJS
    private static final HashMap<Item, RenderJSItemDecorator> registeredItemDecorators = new HashMap<>();
    @HideFromJS
    public final RenderJSItemDecorator registeredGlobalItemDecorator;

    public RenderJSItemDecoratorHandler() {
        registeredGlobalItemDecorator = new RenderJSItemDecorator(null);
    }

    @HideFromJS
    public static RenderJSItemDecoratorHandler getInstance() {
        return Renderjs.itemDecoratorHandler;
    }

    public static void clearRender() {
        getInstance().registeredGlobalItemDecorator.clear();
        registeredItemDecorators.values().forEach(RenderJSItemDecorator::clear);
    }

    @HideFromJS
    public void RegisterItemDecorationsEvent(RegisterItemDecorationsEvent event) {
        RenderJSItemDecoratorHandler.clearRender();
        RenderJSEvents.REGISTER_ITEM_DECORATIONS.post(new ItemDecorationsRegisterEvent());
        registeredItemDecorators.forEach(event::register);
    }

    @HideFromJS
    public void register(Item item, Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        if (registeredItemDecorators.containsKey(item)) {
            registeredItemDecorators.get(item).addRender(consumer);
            return;
        }
        registeredItemDecorators.put(item, new RenderJSItemDecorator(consumer));
    }

    @HideFromJS
    public void registerForAllItem(Consumer<RenderJSItemDecorator.RenderContext> consumer) {
        registeredGlobalItemDecorator.addRender(consumer);
    }
}
