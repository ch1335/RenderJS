// RenderJS 26.1.2 smoke test script (KubeJS client script)
// Place at run/kubejs/client_scripts/renderjs_smoke_test.js and start the game.
// Purpose: manually verify the render events, helpers and item decorations after the port.
// Note: 26.1.2 changed the GUI drawing API; if Text-related calls error, keep only the fill/hLine parts first.

RenderJSEvents.onGuiPreRender(event => {
  // HUD: translucent background + text
  event.fill(6, 6, 150, 46, 0xAA000000)
  event.hLine(6, 150, 6, 0xFFFFFFFF)
  event.drawString(Text.of('RenderJS 26.1.2 HUD OK'), 10, 12, 0xFFFFFFFF)
  event.drawString(Text.of('partialTick=' + event.getPartialTick()), 10, 26, 0xFFFFFF00)
})

RenderJSEvents.onGuiPostRender(event => {
  // Draw a line after the HUD to verify the Post phase
  event.fill(6, 46, 150, 47, 0xFF00FF00)
})

RenderJSEvents.onScreenPreRender(event => {
  // Draw a marker in the top-left corner of any open screen
  event.fill(2, 2, 10, 10, 0xFFFF0000)
})

RenderJSEvents.onLevelRender(event => {
  // World rendering: draw a block outline (diamond block near the origin). Place a diamond block there first.
  let poseStack = event.getPoseStack()
  event.pushPose()
  event.transformerCamera(poseStack)
  event.transformerToXYZ(poseStack, 0, 64, 0)
  let state = BlockState.of('minecraft:diamond_block')
  if (state) {
    event.renderHitOutline(state, poseStack)
  }
  event.popPose()
})

RenderJSEvents.RegisterItemDecorations(event => {
  // Item decoration: green marker in the bottom-right corner of every item
  event.registerForAllItem(ctx => {
    ctx.fill(ctx.xOffset + 12, ctx.yOffset + 12, ctx.xOffset + 16, ctx.yOffset + 16, 0xFF00FF00)
  })
})
