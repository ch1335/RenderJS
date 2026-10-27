package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.client.renderer.RenderJSBlockEntityRenderer;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Info("Registration event for block entity renderers. Registration is applied on game start and swapped on script reload.")
@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID)
public class RegisterBlockEntityRenderEvent implements KubeEvent {

    private static final Map<BlockEntityType<?>, BlockEntityRenderer<?, ?>> LIVE_RENDERERS = new ConcurrentHashMap<>();
    private static BlockEntityRendererProvider.Context lastContext;

    private final EntityRenderersEvent.RegisterRenderers event;

    public RegisterBlockEntityRenderEvent(EntityRenderersEvent.RegisterRenderers event) {
        this.event = event;
    }

    @SubscribeEvent
    public static void EntityRenderersEvent(EntityRenderersEvent.RegisterRenderers event) {
        RenderJSEvents.REGISTER_BLOCK_ENTITY_RENDER.post(new RegisterBlockEntityRenderEvent(event));
    }

    @Info(value = "Registers a block entity renderer, or replaces the renderer of an already registered block entity type. Replacing is applied on script reload.",
            params = {
                    @Param(name = "entityType", value = "Block entity type to render."),
                    @Param(name = "provider", value = "Provider that creates the renderer for the given type.")
            })
    public <T extends BlockEntity, S extends BlockEntityRenderState> void registerOrSwap(BlockEntityType<T> entityType, BlockEntityRendererProvider<T, S> provider) {
        if (event != null) {
            BlockEntityRendererProvider<T, S> wrapped = context -> {
                lastContext = context;
                BlockEntityRenderer<T, S> renderer = provider.create(context);
                LIVE_RENDERERS.put(entityType, renderer);
                return renderer;
            };
            event.registerBlockEntityRenderer(entityType, wrapped);
            return;
        }

        BlockEntityRenderer<?, ?> live = LIVE_RENDERERS.get(entityType);
        if (live == null) {
            RenderJS.LOGGER.info("The old provider of {} does not exist, you may need to use F+H to reload the resource pack", entityType);
            return;
        }
        if (!(live instanceof RenderJSBlockEntityRenderer oldRenderer)) {
            RenderJS.LOGGER.error("The old renderer of {} is not a RenderJS renderer, and cannot be swapped", entityType);
            return;
        }
        if (lastContext == null) {
            RenderJS.LOGGER.warn("RenderJS has no renderer context yet, {} will be updated on the next resource reload", entityType);
            return;
        }
        BlockEntityRenderer<T, S> newRenderer = provider.create(lastContext);
        if (newRenderer instanceof RenderJSBlockEntityRenderer newRenderJS) {
            oldRenderer.copyFrom(newRenderJS);
        } else {
            RenderJS.LOGGER.error("The new provider of {} is not a RenderJS renderer, and cannot be swapped", entityType);
        }
    }

    public static void clearLiveRenderers() {
        LIVE_RENDERERS.clear();
    }

    public static int getLiveRendererCount() {
        return LIVE_RENDERERS.size();
    }
}
