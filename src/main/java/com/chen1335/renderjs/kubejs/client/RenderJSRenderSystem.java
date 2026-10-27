package com.chen1335.renderjs.kubejs.client;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;

/**
 * The old immediate-mode RenderSystem state helpers no longer exist in MC 26.1.2: rendering state is
 * baked into {@code RenderPipeline}s. The methods stay as no-ops so old scripts keep loading, but they
 * have no effect.
 */
@Deprecated
@Info("Deprecated. 26.1.2 has no immediate-mode render state; every method is a no-op kept for old scripts.")
public class RenderJSRenderSystem {

    @Deprecated
    @Info("No-op. Blending is defined by the RenderType pipeline.")
    public static void enableBlendJS() {
    }

    @Deprecated
    @Info("No-op. Blending is defined by the RenderType pipeline.")
    public static void disableBlendJS() {
    }

    @Deprecated
    @Info("No-op. Depth testing is defined by the RenderType pipeline.")
    public static void enableDepthTestJS() {
    }

    @Deprecated
    @Info("No-op. Depth testing is defined by the RenderType pipeline.")
    public static void disableDepthTestJS() {
    }

    @Deprecated
    @Info(value = "No-op. Shader color is not a global state in 26.1.2.",
            params = {
                    @Param(name = "r", value = "Red channel, 0.0 to 1.0. Ignored."),
                    @Param(name = "g", value = "Green channel, 0.0 to 1.0. Ignored."),
                    @Param(name = "b", value = "Blue channel, 0.0 to 1.0. Ignored."),
                    @Param(name = "a", value = "Alpha channel, 0.0 to 1.0. Ignored.")
            })
    public static void setShaderColorJS(float r, float g, float b, float a) {
    }

    @Deprecated
    @Info("No-op. Blend functions are defined by the RenderType pipeline.")
    public static void defaultBlendFuncJS() {
    }

    @Deprecated
    @Info("No-op. Shaders are defined by the RenderType pipeline.")
    public static void setPositionTexShader() {
    }

    @Deprecated
    @Info(value = "No-op. Textures are bound by the RenderType pipeline.",
            params = {@Param(name = "resourceLocation", value = "Texture identifier. Ignored.")})
    public static void setShaderTextureJS(net.minecraft.resources.Identifier resourceLocation) {
    }
}
