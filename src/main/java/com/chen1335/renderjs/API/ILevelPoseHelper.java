package com.chen1335.renderjs.API;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import org.joml.Quaternionf;

/**
 * 3D pose helper for world / entity / block entity contexts. These contexts still use {@link PoseStack}.
 */
@Info("3D transform helpers for world, entity and block entity rendering. These contexts use PoseStack.")
public interface ILevelPoseHelper {
    @HideFromJS
    PoseStack getPoseStack();

    @Info(value = "Scales the pose stack in the X and Y axes.",
            params = {
                    @Param(name = "x", value = "Horizontal scale factor."),
                    @Param(name = "y", value = "Vertical scale factor.")
            })
    default void scale(float x, float y) {
        getPoseStack().scale(x, y, 1);
    }

    @Info(value = "Scales the pose stack in all three axes.",
            params = {
                    @Param(name = "x", value = "X axis scale factor."),
                    @Param(name = "y", value = "Y axis scale factor."),
                    @Param(name = "z", value = "Z axis scale factor.")
            })
    default void scale(float x, float y, float z) {
        getPoseStack().scale(x, y, z);
    }

    @Info(value = "Translates the pose stack in the X and Y axes.",
            params = {
                    @Param(name = "x", value = "X axis translation."),
                    @Param(name = "y", value = "Y axis translation.")
            })
    default void translate(float x, float y) {
        getPoseStack().translate(x, y, 0);
    }

    @Info(value = "Translates the pose stack in all three axes.",
            params = {
                    @Param(name = "x", value = "X axis translation."),
                    @Param(name = "y", value = "Y axis translation."),
                    @Param(name = "z", value = "Z axis translation.")
            })
    default void translate(float x, float y, float z) {
        getPoseStack().translate(x, y, z);
    }

    @Info(value = "Applies a quaternion rotation to the pose stack.",
            params = {@Param(name = "quaternion", value = "Rotation to apply.")})
    default void mulPose(Quaternionf quaternion) {
        getPoseStack().mulPose(quaternion);
    }

    @Info(value = "Rotates the pose stack around the X axis.",
            params = {@Param(name = "degrees", value = "Rotation angle in degrees.")})
    default void rotationDegreesX(float degrees) {
        getPoseStack().mulPose(new Quaternionf().rotateX((float) Math.toRadians(degrees)));
    }

    @Info(value = "Rotates the pose stack around the Y axis.",
            params = {@Param(name = "degrees", value = "Rotation angle in degrees.")})
    default void rotationDegreesY(float degrees) {
        getPoseStack().mulPose(new Quaternionf().rotateY((float) Math.toRadians(degrees)));
    }

    @Info(value = "Rotates the pose stack around the Z axis.",
            params = {@Param(name = "degrees", value = "Rotation angle in degrees.")})
    default void rotationDegreesZ(float degrees) {
        getPoseStack().mulPose(new Quaternionf().rotateZ((float) Math.toRadians(degrees)));
    }

    @Info("Pushes a copy of the current pose onto the stack.")
    default void pushPose() {
        getPoseStack().pushPose();
    }

    @Info("Pops the last pose from the stack.")
    default void popPose() {
        getPoseStack().popPose();
    }
}
