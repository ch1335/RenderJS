package com.chen1335.renderjs.kubejs;

import com.chen1335.renderjs.RenderJSUtil;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;

public class RenderJSCommonPlugin implements KubeJSPlugin {
    @Override
    public void registerBindings(BindingRegistry bindingRegistry) {
        bindingRegistry.add("RenderJSUtil", RenderJSUtil.class);
    }
}
