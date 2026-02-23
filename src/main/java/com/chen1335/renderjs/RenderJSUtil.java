package com.chen1335.renderjs;

import net.neoforged.neoforgespi.Environment;

public class RenderJSUtil {
    public static boolean isClient() {
        return Environment.get().getDist().isClient();
    }
}
