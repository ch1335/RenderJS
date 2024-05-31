package com.chen1335.renderjs.kubejs.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

@Info("部分RenderSystem常用方法,使用完整方法请loadClass原版RenderSystem类")
public class RenderJSRenderSystem extends RenderSystem {
    @HideFromJS
    private static final RenderJSRenderSystem instance = new RenderJSRenderSystem();
    @HideFromJS
    public static RenderJSRenderSystem getInstance() {
        return instance;
    }
    @Info("启用混合")
    public void enableBlendJS() {
        RenderSystem.enableBlend();
    }
    @Info("禁用用混合")
    public void disableBlendJS(){
        RenderSystem.disableBlend();
    }
    @Info("启用深度测试")
    public void enableDepthTestJS() {
        RenderSystem.enableDepthTest();
    }
    @Info("禁用深度测试")
    public void disableDepthTestJS() {
        RenderSystem.disableDepthTest();
    }
    @Info("设置着色器颜色(0~1)")
    public void setShaderColorJS(float r,float g,float b,float a){
        RenderSystem.setShaderColor(r,g,b,a);
    }
    @Info("使用混合方法为默认方法(常用)")
    public void defaultBlendFuncJS(){
        RenderSystem.defaultBlendFunc();
    }
    @Info("设置着色器为PositionTexShader，该着色器为gui中常用着色器")
    public void setPositionTexShader(){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
    }
    @Info("设置纹理")
    public void setShaderTextureJS(ResourceLocation resourceLocation){
        RenderSystem.setShaderTexture(0,resourceLocation);
    }
    @Info("禁用纹理")
    public void disableTextureJS(){
        RenderSystem.disableTexture();
    }
    @Info("启用纹理")
    public void enableTextureJS(){
        RenderSystem.enableTexture();
    }
}
