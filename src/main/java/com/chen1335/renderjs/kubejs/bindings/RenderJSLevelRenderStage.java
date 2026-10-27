package com.chen1335.renderjs.kubejs.bindings;

/**
 * Level render stages exposed to scripts.
 * <p>
 * MC 26.1.2 removed the 1.21.1 {@code RenderLevelStageEvent.Stage} enum; RenderJS now draws world
 * geometry from a single submission pass, so only {@link #SUBMIT} is available.
 */
public final class RenderJSLevelRenderStage {
    public static final String SUBMIT = "submit";

    private RenderJSLevelRenderStage() {
    }
}
