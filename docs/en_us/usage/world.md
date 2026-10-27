# World Rendering

Applies to `onLevelRender` (world geometry submission) and `onLivingPreRender` / `onLivingPostRender`
(living entity rendering).

## Coordinate system

World rendering uses **camera-relative coordinates**: first translate the pose to the camera position,
then all following coordinates are relative to the camera.

```js
RenderJSEvents.onLevelRender(event => {
  const poseStack = event.getPoseStack()

  event.pushPose()
  event.transformerCamera(poseStack)                       // move to camera-relative space
  event.transformerToXYZ(poseStack, 0, 64, 0)  // then to the block position
  // (0,0,0) is now the north-west-bottom corner of block (0,64,0)
  event.renderHitOutline(Blocks.DIAMOND_BLOCK.defaultBlockState(), poseStack)
  event.popPose()
})
```

- `event.transformerCamera(poseStack)` is equivalent to `translate(-cameraX, -cameraY, -cameraZ)`;
- `event.transformerToXYZ(poseStack, x, y, z)` translates to the given coordinates; `event.transformerToBlockPose(poseStack, pos)` translates to a block position;
- `event.getCameraPosition()` returns the camera position in world space (`Vec3`).

## Rendering methods

| Method | Description |
|---|---|
| `renderSingleBlock(blockState, poseStack)` | render a block model (full bright, no tint) |
| `renderSingleBlock(blockState, poseStack, color)` | tint the whole model |
| `renderSingleBlock(blockState, poseStack, color, renderType)` | tint + custom RenderType (layering / through terrain) |
| `renderSingleBlock(blockState, poseStack, color, renderType, packedLight)` | with explicit light |
| `renderSingleBlock(blockState, poseStack, color, renderType, packedLight, packedOverlay)` | all parameters |
| `renderSingleBlockWithType(blockState, poseStack, renderType)` | no tint, custom RenderType |
| `renderSingleBlockWithType(blockState, poseStack, renderType, packedLight)` | no tint + light |
| `renderSingleBlockWithType(blockState, poseStack, renderType, packedLight, packedOverlay)` | no tint + light/overlay |
| `renderLevelItem(itemStack, poseStack)` | render an item in the `GROUND` context |
| `renderLevelItem(itemStack, poseStack, packedLight)` | with explicit light |
| `renderLevelItem(itemStack, poseStack, packedLight, packedOverlay)` | with explicit light/overlay |
| `renderHitOutline(blockState, poseStack)` | draw a block outline |
| `renderHitOutline(blockState, poseStack, color)` | custom outline color |
| `renderHitOutline(blockState, poseStack, color, renderType)` | custom color + RenderType |
| `renderHitOutlineWithType(blockState, poseStack, renderType)` | custom RenderType (default color) |
| `packLight(level, blockPos)` | packed light value at a position |
| `LightCoordsUtil.FULL_BRIGHT` | full-bright light constant (global binding) |

## Color

`renderSingleBlock` and `renderHitOutline` accept an ARGB color to tint the whole model:

```js
RenderJSEvents.onLevelRender(event => {
  const poseStack = event.getPoseStack()
  event.pushPose()
  event.transformerCamera(poseStack)
  event.transformerToXYZ(poseStack, 0, 64, 0)

  const state = Blocks.DIAMOND_BLOCK.defaultBlockState()

  // tint the whole model red (all quads, including ones without a tint index)
  event.renderSingleBlock(state, poseStack, 0xFFFF0000)

  // combined with a RenderType (e.g. no-depth, drawn through terrain)
  event.renderSingleBlock(state, poseStack, 0x80FF0000, RenderJSRenderType.TOP_LAYER_TARGET)

  // outline color
  event.renderHitOutline(state, poseStack, 0xFFFF0000)

  event.popPose()
})
```

Notes:

- Whole-model tint is implemented with custom geometry (`QuadInstance` vertex colors) and is submitted with a
  **single RenderType**: semi-transparent colors need a translucent RenderType (custom pipeline) to blend;
- the model's own tints (grass, leaves, ...) are applied first, then multiplied by your color;
- special models (chests, banners, ...) have no plain model parts and fall back to the default path, so they
  are **not** tinted;
- `renderLevelItem` (items) does not support whole-model tint yet.

## Render state

There is no global color/RenderType state: pass a RenderType directly to `renderSingleBlockWithType(..., renderType)` when you need custom layering (see the section above).

## 3D transforms

| Method | Description |
|---|---|
| `pushPose()` / `popPose()` | save / restore the pose |
| `translate(x, y)` / `translate(x, y, z)` | translate |
| `scale(x, y)` / `scale(x, y, z)` | scale |
| `mulPose(quaternion)` | apply a quaternion rotation |
| `rotationDegreesX/Y/Z(degrees)` | rotate around an axis (degrees) |
| `getPoseStack()` | get the `PoseStack` |

## Other

| Method | Description |
|---|---|
| `getStage()` | always `"submit"` (26.1.2 has no stages) |
| `getPartialTick()` | partial tick for the current frame |
| `getLevelRenderer()` | the `LevelRenderer` instance |
| `getCameraPosition()` | camera position in world space |

## Example: draw a block and an item at a fixed position

```js
RenderJSEvents.onLevelRender(event => {
  const poseStack = event.getPoseStack()
  event.pushPose()
  event.transformerCamera(poseStack)
  event.transformerToXYZ(poseStack, 0, 64, 0)

  // full-bright diamond block
  event.renderSingleBlock(Blocks.DIAMOND_BLOCK.defaultBlockState(), poseStack)

  // rotating item above the block
  event.pushPose()
  event.translate(0.5, 1.2, 0.5)
  event.rotationDegreesY(event.getPartialTick() * 60)
  event.renderLevelItem(Item.of('minecraft:diamond'), poseStack)
  event.popPose()

  event.popPose()
})
```

## Example: draw something following an entity

```js
RenderJSEvents.onLivingPreRender(event => {
  const state = event.getRenderState()   // EntityRenderState: x/y/z, light, partialTick, etc.
  const poseStack = event.getPoseStack()

  event.pushPose()
  event.transformerCamera(poseStack)
  event.translate(state.x, state.y + state.boundingBoxHeight + 0.3, state.z)
  event.renderLevelItem(Item.of('minecraft:emerald'), poseStack, state.lightCoords)
  event.popPose()
})
```

## Render layers / drawing through terrain

MC 26.1.2 has no global `RenderSystem.enableDepthTest()`. **Layering is decided by the `DepthStencilState`
of the `RenderPipeline` used by the RenderType.** To draw a block through terrain (always on top), give it a
render type that disables depth testing.

RenderJS ships two such render types:

| RenderType | Purpose |
|---|---|
| `RenderJSRenderType.TOP_LAYER_TARGET` | block models, depth test disabled, always drawn on top of terrain |
| `RenderJSRenderType.TOP_LAYER_LINE_TARGET` | lines/outlines, depth test disabled |

```js
RenderJSEvents.onLevelRender(event => {
  const poseStack = event.getPoseStack()
  event.pushPose()
  event.transformerCamera(poseStack)
  event.transformerToXYZ(poseStack, 0, 64, 0)

  const state = Blocks.DIAMOND_BLOCK.defaultBlockState()

  // normal block, hidden by terrain
  event.renderSingleBlock(state, poseStack)

  // block drawn through terrain, always visible
  event.renderSingleBlockWithType(state, poseStack, RenderJSRenderType.TOP_LAYER_TARGET)

  // outline drawn through terrain
  event.renderHitOutlineWithType(state, poseStack, RenderJSRenderType.TOP_LAYER_LINE_TARGET)

  event.popPose()
})
```

### How it works / custom pipelines

The two built-in render types are backed by custom pipelines registered in `RegisterRenderPipelinesEvent`:

```java
RenderPipeline pipeline = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)   // entity shader used by blocks
        .withLocation(Identifier.fromNamespaceAndPath("mymod", "pipeline/block_no_depth"))
        .withShaderDefine("ALPHA_CUTOUT", 0.1F)
        .withSampler("Sampler1")                                                    // overlay
        .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false)) // no depth test/write
        .build();

RenderType type = RenderType.create("mymod:block_no_depth",
        RenderSetup.builder(pipeline)
                .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)              // block atlas
                .useLightmap()
                .useOverlay()
                .setOutline(RenderSetup.OutlineProperty.NONE)
                .createRenderSetup());
```

Common layering needs:

| Need | DepthStencilState |
|---|---|
| Normal (occluded) | `DepthStencilState.DEFAULT` (`LESS_THAN_OR_EQUAL` + depth write) |
| Always on top (through terrain) | `new DepthStencilState(CompareOp.ALWAYS_PASS, false)` |
| Slightly in front, avoid z-fighting | `LESS_THAN_OR_EQUAL` with negative depth bias (e.g. `new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false, -1F, -10F)`) |
| Draw in front of existing geometry without writing depth | `new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false)` |

If the two built-in render types are not enough, register your own pipeline + RenderType as above and pass it
to `renderSingleBlockWithType(state, poseStack, yourRenderType)`. A RenderType is just an object; you can store and
pass it around from scripts.

Notes:

- Geometry drawn through terrain is still subject to **batch sorting**: RenderJS submits it after normal
  terrain, but its order relative to other translucent/special batches is decided by the engine by RenderType;
- `renderSingleBlockWithType(..., renderType)` works for **regular block models**. Special models (chests, banners,
  ...) have no regular model parts and fall back to the default render type, so they will not draw through
  terrain.

## Notes

- World rendering is **submission-based**: geometry goes into the current frame's render queue, sorted by
  RenderType, so draw order is not guaranteed to match call order;
- `onLevelRender` fires once per frame; `onLivingPreRender/Post` fire once per visible entity;
- Keep callbacks cheap; derive animated values from partialTick/time instead of doing heavy work;
- There is no `MultiBufferSource`/immediate-mode API anymore: the old immediate-mode render state helpers were removed.
