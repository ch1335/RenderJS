package com.chen1335.renderjs.kubejs.bindings;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

@Info("部分RenderSystem常用方法")
public class RenderJSRenderSystem extends RenderSystem {


    public static void enableBlendJS() {
        RenderSystem.enableBlend();
    }

    public static void disableBlendJS() {
        RenderSystem.disableBlend();
    }

    public static void enableDepthTestJS() {
        RenderSystem.enableDepthTest();
    }

    public static void disableDepthTestJS() {
        RenderSystem.disableDepthTest();
    }

    @Info("color (0~1)")
    public static void setShaderColorJS(float r, float g, float b, float a) {
        RenderSystem.setShaderColor(r, g, b, a);
    }

    public static void defaultBlendFuncJS() {
        RenderSystem.defaultBlendFunc();
    }

    public static void setPositionTexShader() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
    }

    public static void setShaderTextureJS(ResourceLocation resourceLocation) {
        RenderSystem.setShaderTexture(0, resourceLocation);
    }

    public static void disableTextureJS() {
        RenderSystem.disableTexture();
    }

    public static void enableTextureJS() {
        RenderSystem.enableTexture();
    }
}
