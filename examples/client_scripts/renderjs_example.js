// RenderJS 26.1.2 完整示例（KubeJS 客户端脚本）
// 放置位置：run/kubejs/client_scripts/renderjs_example.js
// 覆盖：HUD / Screen / 世界渲染 / 物品装饰。
//
// 颜色是 ARGB 8 位：0xAARRGGBB，必须带 alpha；
// 6 位颜色（0xRRGGBB）等价于 alpha=0，不会渲染。

RenderJSEvents.onGuiPreRender(event => {
  event.fill(6, 6, 168, 46, 0xAA000000)                    // 半透明黑底
  event.hLine(6, 168, 6, 0xFFFFFFFF)                       // 不透明白线
  event.drawString(Text.ofString('RenderJS 26.1.2'), 10, 12, 0xFFFFFFFF)
  event.drawString(Text.ofString('item HUD'), 10, 26, 0xFF00FFFF)
})

RenderJSEvents.onScreenPreRender(event => {
  event.fill(2, 2, 10, 10, 0xFFFF0000)                     // 界面左上角红块
})

RenderJSEvents.onLevelRender(event => {
  // 在方块 (0, 64, 0) 上画描边 / 方块；先把方块放在该位置，或改成你想要的坐标
  const poseStack = event.getPoseStack()
  event.pushPose()
  event.transformerCamera(poseStack)                       // 世界坐标 → 相机相对坐标
  event.transformerToBlockPose(poseStack, new BlockPos(0, 64, 0))

  const state = Blocks.DIAMOND_BLOCK.defaultBlockState()

  // 普通方块：会被地形遮挡
  event.renderSingleBlock(state, poseStack)

  // 穿过地形：使用无深度的 RenderType，永远画在最上层
  event.renderSingleBlock(state, poseStack, RenderJSRenderType.TOP_LAYER_TARGET)

  // 描边（普通 / 穿过地形）
  event.renderHitOutline(state, poseStack)
  event.renderHitOutline(state, poseStack, RenderJSRenderType.TOP_LAYER_LINE_TARGET)

  event.popPose()
})

RenderJSEvents.RegisterItemDecorations(event => {
  // 所有物品右下角绿色角标
  event.registerForAllItem(ctx => {
    ctx.fill(ctx.xOffset + 12, ctx.yOffset + 12, ctx.xOffset + 16, ctx.yOffset + 16, 0xFF00FF00)
  })
})
