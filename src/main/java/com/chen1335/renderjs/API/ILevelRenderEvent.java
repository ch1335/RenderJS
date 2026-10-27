package com.chen1335.renderjs.API;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

public interface ILevelRenderEvent extends IRenderJSEvent {
    @HideFromJS
    SubmitCustomGeometryEvent getEvent();

    @HideFromJS
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

    default LevelRenderer getLevelRenderer() {
        return Minecraft.getInstance().levelRenderer;
    }

    /**
     * MC 26.1.2 removed the per-stage level render events in favour of a single custom geometry
     * submission pass, so scripts now always see this pseudo stage.
     */
    default String getStage() {
        return "submit";
    }

    default float getPartialTick() {
        return Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
    }

    @HideFromJS
    default net.minecraft.client.renderer.state.level.CameraRenderState getCamera() {
        return getEvent().getLevelRenderState().cameraRenderState;
    }

    default net.minecraft.world.phys.Vec3 getCameraPosition() {
        return getEvent().getLevelRenderState().cameraRenderState.pos;
    }

    /**
     * Translates the pose stack into camera relative world space, the same transformation the old
     * 1.21.1 {@code transformerCamera(poseStack, camera)} helper applied.
     */
    default void transformerCamera(PoseStack poseStack) {
        net.minecraft.world.phys.Vec3 position = getCameraPosition();
        poseStack.translate(-position.x, -position.y, -position.z);
    }
}
