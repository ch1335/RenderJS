package com.chen1335.renderjs.kubejs.bindings.event;

import com.chen1335.renderjs.client.RenderJSItemDecorator;
import dev.latvian.mods.kubejs.client.ClientKubeEvent;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.IItemDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class RenderJSRegisterItemDecorationsEvent implements ClientKubeEvent {
    private final HashMap<Item, List<IItemDecorator>> itemDecorators;

    private final List<IItemDecorator> globalDecorators;

    public RenderJSRegisterItemDecorationsEvent(HashMap<Item, List<IItemDecorator>> decorators, ArrayList<IItemDecorator> globalDecorators) {
        itemDecorators = decorators;
        this.globalDecorators = globalDecorators;
    }

    public void register(Item item, Consumer<RenderJSItemDecorator.Context> consumer){
        List<IItemDecorator> itemDecoratorList = itemDecorators.computeIfAbsent(item, item1 -> new ArrayList<>());
        itemDecoratorList.add(new RenderJSItemDecorator(consumer));
    }

    public void register(Consumer<RenderJSItemDecorator.Context> consumer){
        globalDecorators.add(new RenderJSItemDecorator(consumer));
    }
}
