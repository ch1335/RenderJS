# Events

All RenderJS events live in the `RenderJSEvents` event group:

```js
RenderJSEvents.onGuiPreRender(event => {
  // ...
})
```

| Event | When it fires | Times per frame | Callback capabilities |
|---|---|---|---|
| `onGuiPreRender` | before the HUD is drawn | 1 | [GUI rendering](gui.md) + 2D transforms + `getWindow()` / `getPartialTick()` |
| `onGuiPostRender` | after the HUD is drawn | 1 | same as above |
| `onScreenPreRender` | before any `Screen` is drawn | 1 per open screen | GUI rendering + 2D transforms + `getScreen()` / `getMouseX()` / `getMouseY()` |
| `onScreenPostRender` | after any `Screen` is drawn | 1 per open screen | same as above |
| `onLevelRender` | world custom geometry submission | 1 | [world rendering](world.md) + 3D transforms + camera/level state |
| `onLivingPreRender` | before each living entity is rendered | 1 per visible entity | [world rendering](world.md) + 3D transforms + render state/renderer |
| `onLivingPostRender` | after each living entity is rendered | 1 per visible entity | same as above |
| `RegisterItemDecorations` | client start / resource reload | once | register item decorations (see [Item Decorations](item-decorations.md)) |
| `registerBlockEntityRender` | block entity renderer registration / script reload | once | register or hot-swap a scripted block entity renderer (see [Entities & Block Entities](entity-and-block-entity.md)) |

> Since 26.1.2 the world has a **single geometry submission pass**; there are no `AFTER_ENTITIES`-style
> stages anymore. `event.getStage()` is kept for compatibility and always returns `"submit"`.

## GUI events

```js
RenderJSEvents.onGuiPreRender(event => {
  event.fill(8, 8, 160, 30, 0xAA000000)
  event.drawString(Text.ofString('HUD'), 12, 12, 0xFFFFFFFF)
})

RenderJSEvents.onScreenPreRender(event => {
  // only fires for non-HUD screens (inventory, chest, custom screens)
  event.fill(2, 2, 10, 10, 0xFF00FF00)
})
```

- The event object provides both [GUI rendering](gui.md) and 2D transforms;
- Exceptions thrown in GUI events are caught by RenderJS, which suspends GUI rendering until the next
  script reload to avoid crashing/spamming every frame.

## World events

```js
RenderJSEvents.onLevelRender(event => {
  const poseStack = event.getPoseStack()
  event.pushPose()
  event.transformerCamera(poseStack)              // world space -> camera-relative space
  event.transformerToXYZ(poseStack, 0, 64, 0)
  event.renderHitOutline(Blocks.DIAMOND_BLOCK.defaultBlockState(), poseStack)
  event.popPose()
})
```

- Camera-relative coordinates, draw order and limitations are documented in [World Rendering](world.md);
- `onLivingPreRender/Post` also supports world rendering for each visible entity.

## Item decorations and block entities

These two events fire **only during registration** (not every frame):

```js
RenderJSEvents.RegisterItemDecorations(event => {
  event.registerForAllItem(ctx => {
    ctx.fill(ctx.xOffset + 12, ctx.yOffset + 12, ctx.xOffset + 16, ctx.yOffset + 16, 0xFF00FF00)
  })
})
```

```js
RenderJSEvents.registerBlockEntityRender(event => {
  event.registerOrSwap(BlockEntityType.CHEST, ctx => new RenderJSBlockEntityRenderer(ctx))
})
```

See [Item Decorations](item-decorations.md) and [Entities & Block Entities](entity-and-block-entity.md).

## Notes

1. **All rendering events are client-side**; registering them in server scripts does nothing.
2. **Keep callbacks cheap**: they run every frame; long loops or lots of allocations will drop FPS.
3. **Never store event objects across frames**: `event`/`ctx` are reused and only valid during the call.
4. **Do not register decorations/block entities from render callbacks**: use the registration events.
5. Script reloads (F3 + T) re-fire registration events; RenderJS clears old registrations before applying
   the new ones.
