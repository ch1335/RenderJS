package com.chen1335.renderjs.API;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;

@Info("World render event context. Geometry submitted here is queued for the current frame's custom geometry pass.")
public interface ILevelRenderEvent extends IRenderJSEvent {
    PoseStack getPoseStack();

    SubmitNodeCollector getSubmitNodeCollector();

    LevelRenderState getLevelRenderState();

    @Info("Returns the client level renderer.")
    default LevelRenderer getLevelRenderer() {
        return Minecraft.getInstance().levelRenderer;
    }

    @Info("Returns the partial tick of the current frame.")
    default float getPartialTick() {
        return Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
    }

    default CameraRenderState getCamera() {
        return getLevelRenderState().cameraRenderState;
    }

    @Info("Returns the camera position in world space.")
    default net.minecraft.world.phys.Vec3 getCameraPosition() {
        return getCamera().pos;
    }

    @Info(value = "Translates the pose stack into camera relative world space.",
            params = {@Param(name = "poseStack", value = "Pose stack to transform.")})
    default void transformerCamera(PoseStack poseStack) {
        net.minecraft.world.phys.Vec3 position = getCameraPosition();
        poseStack.translate(-position.x, -position.y, -position.z);
    }
}