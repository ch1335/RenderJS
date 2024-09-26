package com.chen1335.renderjs.mixins.minecraft;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin implements IItemExtension {


}
