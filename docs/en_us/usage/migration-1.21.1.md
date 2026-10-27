# Upgrading from 1.21.1

This page describes what to change when migrating RenderJS `2101.x` (Minecraft 1.21.1) scripts to
`26.1.2-1.0.0` (Minecraft 26.1.2). **Event names are mostly unchanged**; the breaking changes are in the GUI
context, world rendering and block entity renderers.

## Breaking changes

| Old (1.21.1) | New (26.1.2) | What to do |
|---|---|---|
| `GuiRenderHelper` global object | removed | use drawing methods on event objects (`onGuiPreRender` / `onScreenPreRender` / item decoration `ctx`) |
| `GuiGraphics` binding | `GuiGraphicsExtractor` | usually not referenced directly; draw via event objects |
| GUI `getPoseStack()` returns `PoseStack` | returns `Matrix3x2fStack` (2D) | `pushPose/popPose` names are kept; `mulPose(quaternion)` becomes `rotateDegrees(degrees)` |
| fake GUI context in world rendering (`GuiGraphics.pose = worldPoseStack`) | removed | use [world rendering](world.md) methods |
| `renderSingleBlock(state, pose, bufferSource, ...)` | `renderSingleBlock(state, pose[, light, overlay])` | drop the `MultiBufferSource` argument |
| `renderLevelItem(stack, pose, bufferSource, ...)` | `renderLevelItem(stack, pose[, light])` | drop the `MultiBufferSource` argument |
| `renderHitOutline(state, pose, bufferSource, ...)` | `renderHitOutline(state, pose[, entity[, pos]])` | drop the `MultiBufferSource` argument |
| `RenderLevelStageEvent.Stage` enum; `event.getStage()` returns enum | `event.getStage()` returns `"submit"`; `RenderJSLevelRenderStage.SUBMIT` | remove stage-based branches; there is a single submission pass per frame |
| `RenderJSRenderSystem` state methods (blend/depth/shader/color) | no-ops | remove those calls; state is defined by RenderType/pipelines |
| `RenderJSRenderType.TOP_LAYER_TARGET` "always on top" | plain vanilla RenderType aliases | no longer guaranteed on top; custom pipelines are not exposed to scripts yet |
| script-defined RenderType / global tint for blocks | global color/RenderType state was removed; pass a RenderType per call instead (`renderSingleBlock(..., renderType)`) | pass the render type you need (e.g. `RenderJSRenderType.TOP_LAYER_TARGET`) |
| block entity renderer single-stage `render(...)` | two stages `createRenderState/extractRenderState/submit` | use `RenderJSBlockEntityRenderer` + `setCustomRender((renderer, ctx) => {})` |
| custom renderer created in `RegisterBlockEntityRender` | event name unchanged, `registerOrSwap` still exists | see the new pattern in [Entities & Block Entities](entity-and-block-entity.md) |
| color parameters `int` | `long` (`0xAARRGGBB`) | usually no change needed; if you see `Cannot convert ... to int`, write `0xFF00FF00 | 0` |

## Unchanged

- `RenderJSEvents` group and event names (`onGuiPreRender`, `onGuiPostRender`, `onScreenPreRender`,
  `onScreenPostRender`, `onLevelRender`, `onLivingPreRender`, `onLivingPostRender`,
  `RegisterItemDecorations`, `registerBlockEntityRender`);
- `kubejs.plugins.txt` and the plugin registration mechanism;
- GUI drawing method names (`fill` / `hLine` / `vLine` / `drawString` / `drawShadowString` /
  `drawTexture` / `drawTextureWithSize` / `renderGuiItem`);
- item decoration registration and `ctx.xOffset/yOffset/itemStack` fields;
- 3D pose method names (`pushPose` / `popPose` / `translate` / `scale` / `mulPose` / `rotationDegreesX/Y/Z`);
- bindings such as `PoseStack`, `Mth`, `LevelRenderer`.

## Migration checklist

1. **Replace `GuiRenderHelper`**: search globally and move each call into a GUI event callback, using
   `event.xxx(...)`.
2. **Check world rendering**: replace "call GUI drawing methods from world render events" with
   `renderSingleBlock` / `renderLevelItem` / `renderHitOutline`.
3. **Remove stage checks**: search for `RenderJSLevelRenderStage.` / `.getStage()` and drop branches such as
   `AFTER_ENTITIES` (26.1.2 only has `SUBMIT`).
4. **Remove `RenderJSRenderSystem` calls**: they have no effect and only mislead.
5. **Drop `MultiBufferSource` arguments** from `renderSingleBlock` / `renderLevelItem` / `renderHitOutline`.
6. **Check colors**: use full 8-digit ARGB; 6-digit colors (`0xRRGGBB`) have alpha = 0 and are invisible.
7. **Block entity renderers**: migrate to `RenderJSBlockEntityRenderer` as described in
   [Entities & Block Entities](entity-and-block-entity.md).
8. **Item decorations**: logic is unchanged; mind the color alpha, registrations are rebuilt on script reload.
9. **Verify in-game**: HUD -> world -> item decorations -> block entities, one by one.

## Internal implementation changes (for maintainers)

| Old implementation | 26.1.2 implementation |
|---|---|
| `GuiGraphicsMixin` injected into `GuiGraphics#renderItemDecorations` | injects into `GuiGraphicsExtractor#itemDecorations` (method/params changed) |
| `PoseStackAccessor` (read pose stack internals) | removed (field changed, old code unused) |
| `BlockEntityRenderDispatcherMixin` (read private renderers) | removed; use the public `BlockEntityRenderDispatcher#getRenderer` |
| 6 access transformers | all removed (no longer needed) |
| mixin `refmap` / `compatibilityLevel: JAVA_17` | refmap removed; `compatibilityLevel` omitted |
| CurseForge KubeJS/Rhino dependencies | `maven.latvian.dev` KubeJS 8 + its transitive Rhino |
