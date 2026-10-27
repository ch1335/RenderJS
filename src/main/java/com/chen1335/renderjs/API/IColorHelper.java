package com.chen1335.renderjs.API;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;

/**
 * Color packing helpers shared by the RenderJS drawing APIs. Implementing contexts gain an
 * {@link #argb(int, int, int, int)} helper that packs channels without allocating an intermediate object.
 */
@Info("Color helpers for packing raw channels into ARGB colors.")
public interface IColorHelper {
    @Info(value = "Packs red, green, blue and alpha channels into one ARGB color without allocating an intermediate object.",
            params = {
                    @Param(name = "r", value = "Red channel, 0 to 255."),
                    @Param(name = "g", value = "Green channel, 0 to 255."),
                    @Param(name = "b", value = "Blue channel, 0 to 255."),
                    @Param(name = "a", value = "Alpha channel, 0 to 255.")
            })
    default int argb(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }
}
