package com.chen1335.renderjs.API;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import org.joml.Matrix3x2fStack;

/**
 * 2D pose helper for GUI contexts (MC 26.1.2 replaced the GUI {@code PoseStack} with {@link Matrix3x2fStack}).
 */
@Info("2D transform helpers for GUI contexts. The GUI pose stack uses Matrix3x2fStack.")
public interface IRenderJSPoseStackHelper {
    Matrix3x2fStack getPoseStack();

    @Info(value = "Scales the GUI pose stack in the X and Y axes.",
            params = {
                    @Param(name = "x", value = "Horizontal scale factor."),
                    @Param(name = "y", value = "Vertical scale factor.")
            })
    default void scale(float x, float y) {
        getPoseStack().scale(x, y);
    }

    @Info(value = "Scales the GUI pose stack. The Z axis is ignored because the GUI pose is two dimensional.",
            params = {
                    @Param(name = "x", value = "Horizontal scale factor."),
                    @Param(name = "y", value = "Vertical scale factor."),
                    @Param(name = "z", value = "Ignored. Kept for source compatibility with the 3D pose helper.")
            })
    default void scale(float x, float y, float z) {
        getPoseStack().scale(x, y);
    }

    @Info(value = "Translates the GUI pose stack in the X and Y axes.",
            params = {
                    @Param(name = "x", value = "Horizontal translation, in GUI pixels."),
                    @Param(name = "y", value = "Vertical translation, in GUI pixels.")
            })
    default void translate(float x, float y) {
        getPoseStack().translate(x, y);
    }

    @Info(value = "Translates the GUI pose stack. The Z axis is ignored because the GUI pose is two dimensional.",
            params = {
                    @Param(name = "x", value = "Horizontal translation, in GUI pixels."),
                    @Param(name = "y", value = "Vertical translation, in GUI pixels."),
                    @Param(name = "z", value = "Ignored. Kept for source compatibility with the 3D pose helper.")
            })
    default void translate(float x, float y, float z) {
        getPoseStack().translate(x, y);
    }

    @Info(value = "Rotates the GUI pose stack around its origin.",
            params = {@Param(name = "degrees", value = "Rotation angle in degrees.")})
    default void rotateDegrees(float degrees) {
        getPoseStack().rotate((float) Math.toRadians(degrees));
    }

    @Info("Pushes a copy of the current GUI transform onto the stack.")
    default void pushPose() {
        getPoseStack().pushMatrix();
    }

    @Info("Pops the last GUI transform from the stack.")
    default void popPose() {
        getPoseStack().popMatrix();
    }
}