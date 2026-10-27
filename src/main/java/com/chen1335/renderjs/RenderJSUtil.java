package com.chen1335.renderjs;

import net.neoforged.fml.loading.FMLEnvironment;

public class RenderJSUtil {
    public static boolean isClient() {
        return FMLEnvironment.getDist().isClient();
    }
}
