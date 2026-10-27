// RenderJS 26.1.2 冒烟测试脚本（KubeJS 客户端脚本）
// 放到 run/kubejs/client_scripts/renderjs_smoke_test.js 后启动游戏。
// 用途：人工验证移植后的渲染事件、helper 与物品装饰是否工作。
// 注意：26.1.2 的 GUI 绘制语义有变化，若 Text 相关调用报错，先只保留 fill/hLine 部分。

RenderJSEvents.onGuiPreRender(event => {
  // HUD：半透明底板 + 文字
  event.fill(6, 6, 150, 46, 0xAA000000)
  event.hLine(6, 150, 6, 0xFFFFFFFF)
  event.drawString(Text.of('RenderJS 26.1.2 HUD OK'), 10, 12, 0xFFFFFFFF)
  event.drawString(Text.of('partialTick=' + event.getPartialTick()), 10, 26, 0xFFFFFF00)
})

RenderJSEvents.onGuiPostRender(event => {
  // HUD 之后画一条线，验证 Post 阶段
  event.fill(6, 46, 150, 47, 0xFF00FF00)
})

RenderJSEvents.onScreenPreRender(event => {
  // 任意 Screen 打开时在左上角画标记
  event.fill(2, 2, 10, 10, 0xFFFF0000)
})

RenderJSEvents.onLevelRender(event => {
  // 世界渲染：画一个方块选中框（钻石块，原点附近）。需要先在世界里放一个钻石块再观察。
  let poseStack = event.getPoseStack()
  event.pushPose()
  event.transformerCamera(poseStack)
  event.transformerToBlockPose(poseStack, new BlockPos(0, 64, 0))
  let state = BlockState.of('minecraft:diamond_block')
  if (state) {
    event.renderHitOutline(state, poseStack)
  }
  event.popPose()
})

RenderJSEvents.RegisterItemDecorations(event => {
  // 物品装饰：所有物品右下角画一个绿色角标
  event.registerForAllItem(ctx => {
    ctx.fill(ctx.xOffset + 12, ctx.yOffset + 12, ctx.xOffset + 16, ctx.yOffset + 16, 0xFF00FF00)
  })
})
