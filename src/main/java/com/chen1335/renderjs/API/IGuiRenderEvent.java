package com.chen1335.renderjs.API;

import com.mojang.blaze3d.platform.Window;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Matrix3x2fStack;

@Info("HUD render event context. Provides window information and partial tick for HUD callbacks.")
public interface IGuiRenderEvent extends IRenderJSEvent {
    @HideFromJS
    RenderGuiEvent getEvent();

    @Info("Returns the game window.")
    default Window getWindow() {
        return Minecraft.getInstance().getWindow();
    }

    default Matrix3x2fStack getPoseStack() {
        return getEvent().getGuiGraphics().pose();
    }

    @Info("Returns the partial tick of the current frame.")
    default float getPartialTick() {
        return getEvent().getPartialTick().getGameTimeDeltaPartialTick(true);
    }
}
