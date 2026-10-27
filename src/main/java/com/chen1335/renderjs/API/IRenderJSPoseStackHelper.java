package com.chen1335.renderjs.API;

import dev.latvian.mods.rhino.util.HideFromJS;
import org.joml.Matrix3x2fStack;

/**
 * 2D pose helper for GUI contexts (MC 26.1.2 replaced the GUI {@code PoseStack} with {@link Matrix3x2fStack}).
 */
public interface IRenderJSPoseStackHelper {
    @HideFromJS
    Matrix3x2fStack getPoseStack();

    default void scale(float x, float y) {
        getPoseStack().scale(x, y);
    }

    default void scale(float x, float y, float z) {
        getPoseStack().scale(x, y);
    }

    default void translate(float x, float y) {
        getPoseStack().translate(x, y);
    }

    default void translate(float x, float y, float z) {
        getPoseStack().translate(x, y);
    }

    default void rotateDegrees(float degrees) {
        getPoseStack().rotate((float) Math.toRadians(degrees));
    }

    default void pushPose() {
        getPoseStack().pushMatrix();
    }

    default void popPose() {
        getPoseStack().popMatrix();
    }
}
