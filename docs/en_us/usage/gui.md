# GUI Rendering

Available in `onGuiPreRender` / `onGuiPostRender` (HUD) and `onScreenPreRender` / `onScreenPostRender`
(any screen). These event objects provide the drawing methods and 2D transforms below.

## Coordinates

- Units are **GUI-scaled pixels**, origin at the top-left, the same as vanilla's `GuiGraphics`;
- Rectangles are defined by their top-left/bottom-right corners: `fill(x0, y0, x1, y1, color)`;
- Text is positioned by its top-left corner: `drawString(component, x, y)`.

## Drawing methods

| Method | Description |
|---|---|
| `fill(x0, y0, x1, y1, color)` | filled rectangle |
| `hLine(minX, maxX, y, color)` | horizontal line |
| `vLine(x, minY, maxY, color)` | vertical line |
| `drawString(component, x, y, color)` | text without shadow |
| `drawShadowString(component, x, y, color)` | text with shadow |
| `renderGuiItem(itemStack, x, y)` | item icon (16x16) |
| `drawTexture(id, x, y, textureWidth, textureHeight)` | full texture at its native size |
| `drawTexture(id, x, y, textureWidth, textureHeight, u, v, uWidth, vHeight)` | part of a texture (UV sub-rect) |
| `drawTextureWithSize(id, x, y, width, height)` | stretch the whole texture to the given size |

Colors accept either a `color` (`long`, `0xAARRGGBB` recommended) or four 0-255 integers `(r, g, b, a)`.

`textureWidth` / `textureHeight` are the **full texture dimensions in pixels**, used to convert
`u/v/uWidth/vHeight` into UV coordinates. `drawTextureWithSize` ignores UVs and stretches the whole texture.

## 2D transforms

The GUI pose is a `Matrix3x2fStack` (not the 3D `PoseStack`):

| Method | Description |
|---|---|
| `pushPose()` / `popPose()` | save / restore transforms (`pushMatrix` / `popMatrix`) |
| `translate(x, y)` | translate |
| `scale(x, y)` | scale (2D) |
| `rotateDegrees(degrees)` | rotate around the origin (degrees) |
| `getPoseStack()` | get the `Matrix3x2fStack` for custom transforms |

## Other

| Method | Description |
|---|---|
| `getWindow()` | the current window (`com.mojang.blaze3d.platform.Window`) |
| `getPartialTick()` | partial tick for the current frame |
| `getScreen()` | current screen (only in `onScreenPreRender/Post`) |
| `getMouseX()` / `getMouseY()` | mouse position (only in `onScreenPreRender/Post`) |

## Example

```js
RenderJSEvents.onGuiPreRender(event => {
  // a panel with a title
  event.fill(10, 10, 210, 64, 0xAA000000)
  event.hLine(10, 210, 10, 0xFFFFFFFF)
  event.drawString(Text.ofString('RenderJS'), 16, 18, 0xFFFFFFFF)
  event.drawShadowString(Text.ofString('Hello world'), 16, 34, 0xFF00FFFF)

  // item icon and a stretched texture
  event.renderGuiItem(Item.of('minecraft:diamond'), 180, 18)

  event.pushPose()
  event.translate(30, 90)
  event.scale(2, 2)
  event.rotateDegrees(-10)
  event.drawString(Text.ofString('ROTATED'), 0, 0, 0xFFFFFF00)
  event.popPose()
})

RenderJSEvents.onScreenPreRender(event => {
  // small square at the mouse position inside any screen
  event.fill(event.getMouseX() - 2, event.getMouseY() - 2, event.getMouseX() + 2, event.getMouseY() + 2, 0xFFFF0000)
})
```

## Notes

- You can only draw inside event callbacks: 26.1.2 GUI rendering is "extract, then render", and there is
  no draw context you can construct yourself;
- `fill`/`drawString` register into the current frame's render state and appear in the same frame;
- the legacy `blitOffset` parameter is still accepted for compatibility but has no effect (GUI now uses
  strata);
- no scissor/clipping API is exposed yet; clip manually with poses or vanilla logic if needed.
