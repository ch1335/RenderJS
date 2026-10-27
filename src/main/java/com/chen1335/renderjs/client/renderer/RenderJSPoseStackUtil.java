package com.chen1335.renderjs.client.renderer;

import com.chen1335.renderjs.RenderJS;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix3x2fStack;

import java.lang.reflect.Field;

/**
 * Helpers for snapshotting and validating the pose stacks that RenderJS hands to scripts.
 */
public final class RenderJSPoseStackUtil {
    private static final int GUI_STACK_CAPACITY = 64;

    private static final Field POSE_LAST_INDEX = findField(PoseStack.class, "lastIndex");
    private static final Field MATRIX_CURR = findField(Matrix3x2fStack.class, "curr");

    private RenderJSPoseStackUtil() {
    }

    /**
     * Creates a cheap copy of the given 3D pose stack. Only the current (top) pose is copied, which is the
     * only pose a render submission uses, so scripts can transform the copy without mutating the pose
     * stack owned by the game.
     */
    public static PoseStack copy(PoseStack source) {
        PoseStack copy = new PoseStack();
        copy.last().set(source.last());
        return copy;
    }

    /**
     * Creates a cheap copy of the given GUI matrix stack. Only the current matrix is copied and a fresh
     * stack is allocated, so scripts can freely push and pop without mutating the GUI stack owned by the game.
     */
    public static Matrix3x2fStack copy(Matrix3x2fStack source) {
        Matrix3x2fStack copy = new Matrix3x2fStack(GUI_STACK_CAPACITY);
        copy.set(source);
        return copy;
    }

    /**
     * Returns the current depth (number of unpopped pushes) of the 3D pose stack. Falls back to
     * {@link PoseStack#isEmpty()} when the depth field cannot be read reflectively.
     */
    public static int depth(PoseStack poseStack) {
        if (POSE_LAST_INDEX != null) {
            try {
                return POSE_LAST_INDEX.getInt(poseStack);
            } catch (IllegalAccessException ignored) {
            }
        }
        return poseStack.isEmpty() ? 0 : 1;
    }

    /**
     * Returns the current depth (number of unpopped pushes) of the GUI matrix stack, or {@code 0} when the
     * depth field cannot be read reflectively.
     */
    public static int depth(Matrix3x2fStack poseStack) {
        if (MATRIX_CURR != null) {
            try {
                return MATRIX_CURR.getInt(poseStack);
            } catch (IllegalAccessException ignored) {
            }
        }
        return 0;
    }

    /**
     * Verifies that a script render callback restored the pose stack depth it started with. An unbalanced
     * stack means a script called {@code pushPose()} without a matching {@code popPose()}; rendering is
     * paused so the leaked transform cannot corrupt following frames. The warning is emitted once because
     * rendering stays paused until the next script reload.
     */
    public static void checkBalanced(String callback, PoseStack poseStack, int expectedDepth) {
        int actualDepth = depth(poseStack);
        if (actualDepth != expectedDepth) {
            pause(callback, expectedDepth, actualDepth);
        }
    }

    /**
     * GUI matrix stack variant of {@link #checkBalanced(String, PoseStack, int)}.
     */
    public static void checkBalanced(String callback, Matrix3x2fStack poseStack, int expectedDepth) {
        int actualDepth = depth(poseStack);
        if (actualDepth != expectedDepth) {
            pause(callback, expectedDepth, actualDepth);
        }
    }

    private static void pause(String callback, int expectedDepth, int actualDepth) {
        RenderJS.LOGGER.warn(
                "RenderJS paused rendering: '{}' left the pose stack unbalanced (expected depth {}, got {}). "
                        + "Every pushPose() must be matched by a popPose().",
                callback, expectedDepth, actualDepth
        );
        RenderJS.CAN_RENDER = false;
    }

    private static Field findField(Class<?> owner, String name) {
        try {
            Field field = owner.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Throwable throwable) {
            return null;
        }
    }
}
