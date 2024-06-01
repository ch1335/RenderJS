package com.chen1335.renderjs.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class ModRenderType extends RenderStateShard{
    public static RenderType CUTOUT_OUTLINE_TARGET = RenderType.create("cutout_outline_target", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS,2097152,true,false,RenderType.CompositeState.builder().setLightmapState(LIGHTMAP).setShaderState(RENDERTYPE_CUTOUT_SHADER).setTextureState(BLOCK_SHEET_MIPPED).setOutputState(OUTLINE_TARGET).createCompositeState(true));

    public ModRenderType(String pName, Runnable pSetupState, Runnable pClearState) {
        super(pName, pSetupState, pClearState);
    }
}
