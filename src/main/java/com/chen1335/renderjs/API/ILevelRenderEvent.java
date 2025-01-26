package com.chen1335.renderjs.API;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

public interface ILevelRenderEvent extends IRenderJSEvent{

    RenderLevelStageEvent getEvent();

    default RenderLevelStageEvent.Stage getStage()
    {
        return getEvent().getStage();
    }


    default LevelRenderer getLevelRenderer()
    {
        return getEvent().getLevelRenderer();
    }


    default PoseStack getPoseStack()
    {
        return getEvent().getPoseStack();
    }


    default Matrix4f getProjectionMatrix()
    {
        return getEvent().getProjectionMatrix();
    }


    default int getRenderTick()
    {
        return getEvent().getRenderTick();
    }


    default float getPartialTick()
    {
        return getEvent().getPartialTick().getGameTimeDeltaPartialTick(true);
    }


    default Camera getCamera()
    {
        return getEvent().getCamera();
    }


    default Frustum getFrustum()
    {
        return getEvent().getFrustum();
    }
}
