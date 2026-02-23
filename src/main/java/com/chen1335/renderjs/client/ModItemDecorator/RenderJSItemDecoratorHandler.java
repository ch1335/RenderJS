package com.chen1335.renderjs.client.ModItemDecorator;

import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.RenderJSClient;
import com.chen1335.renderjs.client.events.ItemDecorationsRegisterEvent;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;

import java.util.HashMap;
import java.util.function.Consumer;

@EventBusSubscriber(modid = RenderJS.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        return RenderJSClient.ITEM_DECORATOR_HANDLER;
    }

    public static void clearRender() {
        getInstance().registeredGlobalItemDecorator.clear();
        registeredItemDecorators.values().forEach(RenderJSItemDecorator::clear);
    }

    @HideFromJS
    @SubscribeEvent
    public static void RegisterItemDecorationsEvent(RegisterItemDecorationsEvent event) {
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
