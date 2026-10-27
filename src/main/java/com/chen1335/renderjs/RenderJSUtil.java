package com.chen1335.renderjs;

import dev.latvian.mods.kubejs.typings.Info;
import net.neoforged.fml.loading.FMLEnvironment;

@Info("General RenderJS utilities.")
public class RenderJSUtil {
    @Info("Returns true when the current physical side is the client.")
    public static boolean isClient() {
        return FMLEnvironment.getDist().isClient();
    }
}
