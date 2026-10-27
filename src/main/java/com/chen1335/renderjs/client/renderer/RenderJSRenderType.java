package com.chen1335.renderjs.client.renderer;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;

/**
 * Render types that RenderJS exposes to scripts.
 * <p>
 * The {@code TOP_LAYER_*} render types use custom pipelines with depth testing disabled, so they are
 * always drawn in front of terrain (the 26.1.2 replacement for the old "no depth test" RenderStateShard).
 */
public final class RenderJSRenderType {
    /**
     * Block model render type that ignores depth: geometry is drawn through terrain.
     * Use with {@code renderSingleBlock(state, poseStack, RenderJSRenderType.TOP_LAYER_TARGET)}.
     */
    public static final RenderType TOP_LAYER_TARGET = RenderType.create(
            "renderjs:top_layer_target",
            RenderSetup.builder(RenderJSRenderPipelines.BLOCK_NO_DEPTH)
                    .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
                    .useLightmap()
                    .useOverlay()
                    .setOutline(RenderSetup.OutlineProperty.NONE)
                    .createRenderSetup()
    );

    /**
     * Line render type that ignores depth: lines are drawn through terrain.
     * Use with {@code renderHitOutline(state, poseStack, RenderJSRenderType.TOP_LAYER_LINE_TARGET)}.
     */
    public static final RenderType TOP_LAYER_LINE_TARGET = RenderType.create(
            "renderjs:top_layer_line_target",
            RenderSetup.builder(RenderJSRenderPipelines.LINES_NO_DEPTH)
                    .createRenderSetup()
    );

    private RenderJSRenderType() {
    }
}
