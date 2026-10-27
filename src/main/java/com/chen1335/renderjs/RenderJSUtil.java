package com.chen1335.renderjs;

import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLEnvironment;

@Info("General RenderJS utilities.")
public class RenderJSUtil {
    @Info("Returns true when the current physical side is the client.")
    public static boolean isClient() {
        return FMLEnvironment.getDist().isClient();
    }

    @Info("Returns the item stack the mouse is currently hovering over in a container screen, or an empty stack when no item is hovered.")
    public static ItemStack getHoveredItemStack() {
        if (!FMLEnvironment.getDist().isClient()) {
            return ItemStack.EMPTY;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof AbstractContainerScreen<?> containerScreen) {
            Slot slot = containerScreen.getHoveredSlot();
            if (slot != null && slot.hasItem()) {
                return slot.getItem();
            }
        }
        return ItemStack.EMPTY;
    }
}
