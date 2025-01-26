package com.chen1335.renderjs.API;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RenderGuiEvent;


public interface IGuiRenderEvent extends IRenderJSEvent {
    @HideFromJS
    RenderGuiEvent getEvent();

    default Window getWindow() {
        return Minecraft.getInstance().getWindow();
    }

    default PoseStack getPoseStack() {
        return getEvent().getGuiGraphics().pose();
    }

    default float getPartialTick() {
        return getEvent().getPartialTick().getGameTimeDeltaPartialTick(true);
    }
}
