package com.chen1335.renderjs.API;

import com.mojang.blaze3d.platform.Window;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Matrix3x2fStack;

public interface IGuiRenderEvent extends IRenderJSEvent {
    @HideFromJS
    RenderGuiEvent getEvent();

    default Window getWindow() {
        return Minecraft.getInstance().getWindow();
    }

    @HideFromJS
    default Matrix3x2fStack getPoseStack() {
        return getEvent().getGuiGraphics().pose();
    }

    default float getPartialTick() {
        return getEvent().getPartialTick().getGameTimeDeltaPartialTick(true);
    }
}
