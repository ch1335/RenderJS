// RenderJS 26.1.2 complete example (KubeJS client script)
// Place at: run/kubejs/client_scripts/renderjs_example.js
// Covers: HUD / Screen / world rendering / item decorations.
//
// Colors are 8-digit ARGB: 0xAARRGGBB and must include alpha.
// A 6-digit color (0xRRGGBB) has alpha = 0 and will not be drawn.

RenderJSEvents.onGuiPreRender(event => {
  event.fill(6, 6, 168, 46, 0xAA000000)                    // translucent black background
  event.hLine(6, 168, 6, 0xFFFFFFFF)                       // opaque white line
  event.drawString(Text.ofString('RenderJS 26.1.2'), 10, 12, 0xFFFFFFFF)
  event.drawString(Text.ofString('item HUD'), 10, 26, 0xFF00FFFF)
})

RenderJSEvents.onScreenPreRender(event => {
  event.fill(2, 2, 10, 10, 0xFFFF0000)                     // red square in the top-left corner of any screen
})

RenderJSEvents.onLevelRender(event => {
  // Draw outlines / blocks around block (0, 64, 0); place a block there first or change the coordinates.
  const poseStack = event.getPoseStack()
  event.pushPose()
  event.transformerCamera(poseStack)                       // world coordinates -> camera-relative coordinates
  event.transformerToXYZ(poseStack, 0, 64, 0)

  const state = Blocks.DIAMOND_BLOCK.defaultBlockState()

  // Normal block: occluded by terrain
  event.renderSingleBlock(state, poseStack)
  event.translate(1, 0, 0)

  // Whole-model tint: multiplied into every vertex (including blocks without a tint index)
  event.renderSingleBlock(state, poseStack, 0xFFFF0000)
  event.translate(1, 0, 0)

  // Through terrain: uses a no-depth RenderType, always drawn on the top layer
  event.renderSingleBlockWithType(state, poseStack, RenderJSRenderType.TOP_LAYER_TARGET)
  event.translate(1, 0, 0)

  // Tint + through terrain
  event.renderSingleBlock(state, poseStack, 0x80FF0000, RenderJSRenderType.TOP_LAYER_TARGET)
  event.translate(1, 0, 0)

  // Outlines (normal / through terrain / custom color)
  event.renderHitOutline(state, poseStack)
  event.translate(1, 0, 0)
  event.renderHitOutlineWithType(state, poseStack, RenderJSRenderType.TOP_LAYER_LINE_TARGET)
  event.translate(1, 0, 0)
  event.renderHitOutline(state, poseStack, 0xFFFF0000)

  event.popPose()
})

RenderJSEvents.RegisterItemDecorations(event => {
  // Green marker in the bottom-right corner of every item
  event.registerForAllItem(ctx => {
    ctx.fill(ctx.xOffset + 12, ctx.yOffset + 12, ctx.xOffset + 16, ctx.yOffset + 16, 0xFF00FF00)
  })
})
