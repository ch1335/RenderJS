# Item Decorations

Item decorations draw an extra layer on top of item icons: durability bars, count badges, custom markers,
borders, and so on.

## Registration

```js
RenderJSEvents.RegisterItemDecorations(event => {
  // for a specific item
  event.register(Items.DIAMOND, ctx => {
    ctx.fill(ctx.xOffset, ctx.yOffset, ctx.xOffset + 16, ctx.yOffset + 1, 0xFFFF0000)
  })

  // for all items
  event.registerForAllItem(ctx => {
    if (ctx.itemStack.count > 1) {
      ctx.fill(ctx.xOffset + 12, ctx.yOffset + 12, ctx.xOffset + 16, ctx.yOffset + 16, 0xFF00FF00)
    }
  })
})
```

| Method | Description |
|---|---|
| `register(item, consumer)` | register a decoration for one item (`item` is an `Item`, e.g. `Items.DIAMOND`) |
| `registerForAllItem(consumer)` | register a decoration for every item |

You may register multiple callbacks per item/globally; they run in registration order.

## Render context

The `ctx` argument provides:

| Field/method | Description |
|---|---|
| `ctx.itemStack` | the current item (`ItemStack`) |
| `ctx.font` | the font (`Font`) |
| `ctx.xOffset` / `ctx.yOffset` | screen position of the item icon's top-left corner |
| note | items are 16x16, so draw within `ctx.xOffset ~ ctx.xOffset+16` |
| drawing methods | same as [GUI Rendering](gui.md): `fill` / `hLine` / `vLine` / `drawString` / `drawShadowString` / `drawTexture` / `drawTextureWithSize` / `renderGuiItem` |
| 2D transforms | `pushPose` / `popPose` / `translate` / `scale` / `rotateDegrees` |

## Examples

### Custom durability bar

```js
RenderJSEvents.RegisterItemDecorations(event => {
  event.registerForAllItem(ctx => {
    const stack = ctx.itemStack
    if (!stack.isDamageableItem()) return

    const max = stack.maxDamage
    const damage = stack.damageValue
    if (max <= 0 || damage <= 0) return

    const ratio = 1 - damage / max
    const width = Math.round(ratio * 13)
    const color = ratio > 0.5 ? 0xFF00FF00 : (ratio > 0.2 ? 0xFFFFFF00 : 0xFFFF0000)

    // draw at the bottom of the icon
    ctx.fill(ctx.xOffset + 2, ctx.yOffset + 13, ctx.xOffset + 13, ctx.yOffset + 15, 0xFF000000)
    ctx.fill(ctx.xOffset + 2, ctx.yOffset + 13, ctx.xOffset + 2 + width, ctx.yOffset + 15, color)
  })
})
```

### Custom count badge

```js
RenderJSEvents.RegisterItemDecorations(event => {
  event.registerForAllItem(ctx => {
    if (ctx.itemStack.count > 1) {
      ctx.drawShadowString(Text.ofString(ctx.itemStack.count), ctx.xOffset + 9, ctx.yOffset - 2, 0xFFFFD700)
    }
  })
})
```

## Notes

1. **Only fires for items inside GUIs** (inventory, chests, crafting grids, tooltip items, ...), not for
   dropped items in the world;
2. The callback runs for every item every frame — keep it lightweight;
3. On script/resource reload `RegisterItemDecorations` fires again and RenderJS clears old registrations
   first, so no manual cleanup is needed;
4. The `item` argument is an `Item`, not an `ItemStack`: use constants like `Items.DIAMOND` or
   `Item.of('minecraft:diamond').item`;
5. Global decorations are implemented by injecting into `GuiGraphicsExtractor#itemDecorations` and are drawn
   after vanilla count/durability overlays.
