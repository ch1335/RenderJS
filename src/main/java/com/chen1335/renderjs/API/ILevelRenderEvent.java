package com.chen1335.renderjs.API;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

@Info("World render event context. Geometry submitted here is queued for the current frame's custom geometry pass.")
public interface ILevelRenderEvent extends IRenderJSEvent {
    @HideFromJS
    SubmitCustomGeometryEvent getEvent();

    default PoseStack getPoseStack() {
        return getEvent().getPoseStack();
    }

    @HideFromJS
    default SubmitNodeCollector getSubmitNodeCollector() {
        return getEvent().getSubmitNodeCollector();
    }

    @HideFromJS
    default LevelRenderState getLevelRenderState() {
        return getEvent().getLevelRenderState();
    }

    @Info("Returns the client level renderer.")
    default LevelRenderer getLevelRenderer() {
        return Minecraft.getInstance().levelRenderer;
    }

    @Info("Returns the current render stage. MC 26.1.2 has a single submission pass, so this is always \"submit\".")
    default String getStage() {
        return "submit";
    }

    @Info("Returns the partial tick of the current frame.")
    default float getPartialTick() {
        return Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
    }

    @HideFromJS
    default net.minecraft.client.renderer.state.level.CameraRenderState getCamera() {
        return getEvent().getLevelRenderState().cameraRenderState;
    }

    @Info("Returns the camera position in world space.")
    default net.minecraft.world.phys.Vec3 getCameraPosition() {
        return getEvent().getLevelRenderState().cameraRenderState.pos;
    }

    @Info(value = "Translates the pose stack into camera relative world space.",
            params = {@Param(name = "poseStack", value = "Pose stack to transform.")})
    default void transformerCamera(PoseStack poseStack) {
        net.minecraft.world.phys.Vec3 position = getCameraPosition();
        poseStack.translate(-position.x, -position.y, -position.z);
    }
}
