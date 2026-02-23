package com.chen1335.renderjs.client.events.renderEvent;

import com.chen1335.renderjs.RenderJS;
import com.chen1335.renderjs.client.renderer.RenderJSBlockEntityRenderer;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSEvents;
import com.chen1335.renderjs.kubejs.bindings.event.RenderJSStartupEvents;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegisterBlockEntityRenderEvent implements KubeEvent {

    private final EntityRenderersEvent.RegisterRenderers event;

    public RegisterBlockEntityRenderEvent(EntityRenderersEvent.RegisterRenderers event) {
        this.event = event;
    }

    @SubscribeEvent
    public static void EntityRenderersEvent(EntityRenderersEvent.RegisterRenderers event) {
        RenderJSEvents.REGISTER_BLOCK_ENTITY_RENDER.post(new RegisterBlockEntityRenderEvent(event));
    }

    public <T extends BlockEntity> void registerOrSwap(BlockEntityType<T> entityType, BlockEntityRendererProvider<T> provider) {
        if (event != null) {
            BlockEntityRendererProvider<?> blockEntityRendererProvider = BlockEntityRenderers.PROVIDERS.get(entityType);
            if (blockEntityRendererProvider == null) {
                BlockEntityRenderers.register(entityType, provider);
            } else if (blockEntityRendererProvider instanceof RenderJSBlockEntityRenderer) {
                BlockEntityRenderers.register(entityType, provider);
            }
        } else {
            BlockEntityRenderer<?> blockEntityRenderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().renderers.get(entityType);
            Minecraft instance = Minecraft.getInstance();
            BlockEntityRenderer<T> tBlockEntityRenderer = provider.create(new BlockEntityRendererProvider.Context(
                    instance.getBlockEntityRenderDispatcher(),
                    instance.getBlockRenderer(),
                    instance.getItemRenderer(),
                    instance.getEntityRenderDispatcher(),
                    instance.getEntityModels(),
                    instance.font
            ));
            if (blockEntityRenderer == null) {
                RenderJS.LOGGER.info("The old provider of {} does not exist, you may need to use F+H to reload the resource package", entityType);
            } else if (blockEntityRenderer instanceof RenderJSBlockEntityRenderer old && tBlockEntityRenderer instanceof RenderJSBlockEntityRenderer theNew) {
                old.setCustomRender(theNew.customRender);
                old.setShouldRender(theNew.shouldRenderPredicate);
                old.setRenderBoundingBox(theNew.renderBoundingBox);
                old.setViewDistance(theNew.getViewDistance());
                old.setShouldRenderOffScreen(theNew.shouldRenderOffScreen);
            } else {
                RenderJS.LOGGER.error("The old or new provider of {} is not a renderJSRenderer! and can not be swap", entityType);
            }
        }
    }
}
