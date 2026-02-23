package com.chen1335.renderjs.kubejs;

import com.chen1335.renderjs.RenderJSUtil;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.neoforged.neoforgespi.Environment;

public class RenderJSCommonPlugin implements KubeJSPlugin {
    @Override
    public void registerBindings(BindingRegistry bindingRegistry) {
        Environment environment = Environment.get();
        bindingRegistry.add("RenderJSUtil", RenderJSUtil.class);
    }
}
