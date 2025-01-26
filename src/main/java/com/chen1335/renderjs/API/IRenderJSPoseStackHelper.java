package com.chen1335.renderjs.API;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

public interface IRenderJSPoseStackHelper {
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

    default void mulPose(Quaternion quaternion) {
        getPoseStack().mulPose(quaternion);
    }

    default void rotationDegreesX(float degrees) {
        getPoseStack().mulPose(Vector3f.XP.rotationDegrees(degrees));
    }

    default void rotationDegreesY(float degrees) {
        getPoseStack().mulPose(Vector3f.YP.rotationDegrees(degrees));
    }

    default void rotationDegreesZ(float degrees) {
        getPoseStack().mulPose(Vector3f.ZP.rotationDegrees(degrees));
    }

    default void pushPose() {
        getPoseStack().pushPose();
    }

    default void popPose() {
        getPoseStack().popPose();
    }
}
