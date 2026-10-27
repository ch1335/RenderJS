package com.chen1335.renderjs.API;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.rhino.util.HideFromJS;
import org.joml.Quaternionf;

/**
 * 3D pose helper for world / entity / block entity contexts. These contexts still use {@link PoseStack}.
 */
public interface ILevelPoseHelper {
    @HideFromJS
    PoseStack getPoseStack();

    default void scale(float x, float y) {
        getPoseStack().scale(x, y, 1);
    }

    default void scale(float x, float y, float z) {
        getPoseStack().scale(x, y, z);
    }

    default void translate(float x, float y) {
        getPoseStack().translate(x, y, 0);
    }

    default void translate(float x, float y, float z) {
        getPoseStack().translate(x, y, z);
    }

    default void mulPose(Quaternionf quaternion) {
        getPoseStack().mulPose(quaternion);
    }

    default void rotationDegreesX(float degrees) {
        getPoseStack().mulPose(new Quaternionf().rotateX((float) Math.toRadians(degrees)));
    }

    default void rotationDegreesY(float degrees) {
        getPoseStack().mulPose(new Quaternionf().rotateY((float) Math.toRadians(degrees)));
    }

    default void rotationDegreesZ(float degrees) {
        getPoseStack().mulPose(new Quaternionf().rotateZ((float) Math.toRadians(degrees)));
    }

    default void pushPose() {
        getPoseStack().pushPose();
    }

    default void popPose() {
        getPoseStack().popPose();
    }
}
