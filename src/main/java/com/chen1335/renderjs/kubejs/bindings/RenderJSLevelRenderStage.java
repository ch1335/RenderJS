package com.chen1335.renderjs.kubejs.bindings;

import dev.latvian.mods.kubejs.typings.Info;

/**
 * Level render stages exposed to scripts.
 * <p>
 * MC 26.1.2 removed the 1.21.1 {@code RenderLevelStageEvent.Stage} enum; RenderJS now draws world
 * geometry from a single submission pass, so only {@link #SUBMIT} is available.
 */
@Info("Level render stages exposed to scripts. MC 26.1.2 has a single submission pass, so only SUBMIT exists.")
public final class RenderJSLevelRenderStage {
    @Info("The single world geometry submission stage.")
    public static final String SUBMIT = "submit";

    private RenderJSLevelRenderStage() {
    }
}
