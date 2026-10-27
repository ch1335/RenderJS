package com.chen1335.renderjs.client.renderer;

import com.chen1335.renderjs.RenderJS;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

/**
 * Custom GPU pipelines used by RenderJS.
 * <p>
 * In MC 26.1.2 depth/layering is baked into the {@link RenderPipeline} ({@link DepthStencilState}); there
 * is no global {@code RenderSystem.enableDepthTest()} anymore. These pipelines disable depth testing so
 * script rendered geometry can be drawn through terrain.
 */
@EventBusSubscriber(value = Dist.CLIENT, modid = RenderJS.MODID)
public final class RenderJSRenderPipelines {
    /**
     * Clone of vanilla {@code ENTITY_CUTOUT_CULL} (used by the cutout block sheet) with depth testing
     * ignored: the geometry is always drawn on top of terrain. Depth writing stays enabled so later
     * passes (clouds, translucent geometry, ...) are correctly occluded by the submitted block instead
     * of overpainting it.
     */
    public static final RenderPipeline BLOCK_NO_DEPTH = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(RenderJS.MODID, "pipeline/block_no_depth"))
            .withShaderDefine("ALPHA_CUTOUT", 0.1F)
            .withSampler("Sampler1")
            .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, true))
            .build();

    /**
     * Clone of vanilla lines pipeline with depth testing disabled (outlines drawn through terrain).
     */
    public static final RenderPipeline LINES_NO_DEPTH = RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(RenderJS.MODID, "pipeline/lines_no_depth"))
            .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, true))
            .build();

    private RenderJSRenderPipelines() {
    }

    @SubscribeEvent
    public static void registerPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(BLOCK_NO_DEPTH);
        event.registerPipeline(LINES_NO_DEPTH);
    }
}
