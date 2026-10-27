package com.chen1335.renderjs.kubejs.client;

/**
 * The old immediate-mode RenderSystem state helpers no longer exist in MC 26.1.2: rendering state is
 * baked into {@code RenderPipeline}s. The methods stay as no-ops so old scripts keep loading, but they
 * have no effect. See {@code docs/porting/03-mc-render-api.md}.
 */
@Deprecated
public class RenderJSRenderSystem {

    @Deprecated
    public static void enableBlendJS() {
    }

    @Deprecated
    public static void disableBlendJS() {
    }

    @Deprecated
    public static void enableDepthTestJS() {
    }

    @Deprecated
    public static void disableDepthTestJS() {
    }

    @Deprecated
    public static void setShaderColorJS(float r, float g, float b, float a) {
    }

    @Deprecated
    public static void defaultBlendFuncJS() {
    }

    @Deprecated
    public static void setPositionTexShader() {
    }

    @Deprecated
    public static void setShaderTextureJS(net.minecraft.resources.Identifier resourceLocation) {
    }
}
