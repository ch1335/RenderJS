# Entities & Block Entities

## Living entity events

`onLivingPreRender` / `onLivingPostRender` fire before/after each visible living entity is rendered.
The event object provides:

| Method | Description |
|---|---|
| `getRenderState()` | the entity's render state (`EntityRenderState`); common fields: `x` / `y` / `z`, `lightCoords`, `partialTick`, `boundingBoxWidth/Height`, `ageInTicks`, `nameTag`, ... |
| `getRenderer()` | the entity renderer (`LivingEntityRenderer`) |
| `getPartialTick()` | partial tick for the current frame |
| world rendering methods | same as [World Rendering](world.md): `renderSingleBlock` / `renderLevelItem` / `renderHitOutline` / `packLight`, etc. |
| 3D transforms | `pushPose` / `popPose` / `translate` / `scale` / `mulPose` / `rotationDegreesX/Y/Z` |

```js
RenderJSEvents.onLivingPreRender(event => {
  const state = event.getRenderState()
  const poseStack = event.getPoseStack()

  event.pushPose()
  event.transformerCamera(poseStack)
  event.translate(state.x, state.y + state.boundingBoxHeight + 0.4, state.z)
  event.renderLevelItem(Item.of('minecraft:emerald'), poseStack, state.lightCoords)
  event.popPose()
})
```

> Rendering happens per entity. Avoid global/entity-independent heavy work here. Always pair
> `pushPose`/`popPose`.

## Block entity renderers

The `registerBlockEntityRender` event registers a **scripted block entity renderer**:

```js
const BlockEntityType = Java.loadClass('net.minecraft.world.level.block.entity.BlockEntityType')

RenderJSEvents.registerBlockEntityRender(event => {
  event.registerOrSwap(BlockEntityType.CHEST, ctx => {
    const renderer = new RenderJSBlockEntityRenderer(ctx)

    renderer.setCustomRender((r, c) => {
      c.pushPose()
      c.translate(0.5, 0.5, 0.5)                       // block center
      c.renderSingleBlock(Blocks.DIAMOND_BLOCK.defaultBlockState(), c.getPoseStack())
      c.popPose()
    })

    renderer.setViewDistance(64)
    renderer.setInfiniteRenderBoundingBox()            // optional: always render
    return renderer
  })
})
```

### Render context (second argument of `setCustomRender`)

| Field/method | Description |
|---|---|
| `blockEntity` | the current block entity |
| `blockEntityJS` | the `KubeBlockEntity` if it comes from KubeJS, otherwise `null` |
| `partialTick` | partial tick for the current frame |
| `poseStack` / `getPoseStack()` | pose already translated to the block position |
| `submitNodeCollector` / `getSubmitNodeCollector()` | geometry submitter (rarely needed directly) |
| `camera` | camera render state (`CameraRenderState`) |
| `packedLight` / `packedOverlay` / `getPackedLight()` / `getPackedOverlay()` | light and overlay values |

The context also provides all [world rendering](world.md) methods and 3D transforms.

### Common renderer settings

| Method | Description |
|---|---|
| `setCustomRender((renderer, context) => {})` | set the per-frame render callback |
| `setViewDistance(distance)` | render distance (default 64) |
| `setShouldRender((blockEntity, cameraPos) => boolean)` | custom visibility check |
| `setRenderBoundingBox(aabb)` | custom render bounding box |
| `setInfiniteRenderBoundingBox()` | infinite bounding box (visible across chunks) |
| `setShouldRenderOffScreen(predicate)` | off-screen rendering check (26.1.2 interface has no entity argument; currently recorded only) |

### Notes

1. `registerBlockEntityRender` fires during **client renderer registration**, not every frame;
2. on script reload (F3 + T) it fires again with no registration context, which hot-swaps the callbacks
   into the already registered renderer;
3. registering a block entity type that was never registered before requires a resource reload
   (F3 + T or re-entering the world);
4. renderers are rebuilt automatically when texture/model resources change.
