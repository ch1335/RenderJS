package com.chen1335.renderjs.client.renderer;

import dev.latvian.mods.kubejs.typings.Info;
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
@Info("Render types exposed to scripts.")
public final class RenderJSRenderType {
    @Info("Block model render type that ignores depth testing, so geometry is drawn through terrain. Use with renderSingleBlockWithType.")
    public static final RenderType TOP_LAYER_TARGET = RenderType.create(
            "renderjs:top_layer_target",
            RenderSetup.builder(RenderJSRenderPipelines.BLOCK_NO_DEPTH)
                    .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
                    .useLightmap()
                    .useOverlay()
                    .setOutline(RenderSetup.OutlineProperty.NONE)
                    .createRenderSetup()
    );

    @Info("Line render type that ignores depth testing, so lines are drawn through terrain. Use with renderHitOutlineWithType.")
    public static final RenderType TOP_LAYER_LINE_TARGET = RenderType.create(
            "renderjs:top_layer_line_target",
            RenderSetup.builder(RenderJSRenderPipelines.LINES_NO_DEPTH)
                    .createRenderSetup()
    );

    private RenderJSRenderType() {
    }
}
