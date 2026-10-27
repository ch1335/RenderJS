# GUI 绘制

适用于 `onGuiPreRender` / `onGuiPostRender`（HUD）与 `onScreenPreRender` / `onScreenPostRender`（任意界面）。
这些事件对象同时提供下面的绘制方法和 2D 变换。

## 坐标

- 单位是 **GUI 缩放后的像素**，原点在左上角，和原版 `GuiGraphics` 一致；
- 左下/右上的矩形约定：`fill(x0, y0, x1, y1, color)` 画的是左上到右下的矩形；
- 文字基线：`drawString(component, x, y)` 的 `y` 是文字顶部。

## 绘制方法

| 方法 | 说明 |
|---|---|
| `fill(x0, y0, x1, y1, color)` | 画矩形 |
| `hLine(minX, maxX, y, color)` | 水平线 |
| `vLine(x, minY, maxY, color)` | 垂直线 |
| `drawString(component, x, y, color)` | 画文字（无阴影） |
| `drawShadowString(component, x, y, color)` | 画带阴影的文字 |
| `renderGuiItem(itemStack, x, y)` | 画物品图标（16×16） |
| `drawTexture(id, x, y, textureWidth, textureHeight)` | 按原始尺寸画整张贴图 |
| `drawTexture(id, x, y, textureWidth, textureHeight, u, v, uWidth, vHeight)` | 画贴图的一部分（UV 子区域） |
| `drawTextureWithSize(id, x, y, width, height)` | 把整张贴图拉伸到指定尺寸 |

颜色参数同时支持两种写法：`color`（`long`，推荐 `0xAARRGGBB`）或 `(r, g, b, a)` 四个 0–255 整数。

`textureWidth` / `textureHeight` 是**整张贴图的像素尺寸**，用于把 `u/v/uWidth/vHeight` 换算成 UV；
`drawTextureWithSize` 会忽略 UV，直接把整张图拉伸。

## 2D 变换

GUI 的 pose 是 `Matrix3x2fStack`（不是 3D 的 `PoseStack`）：

| 方法 | 说明 |
|---|---|
| `pushPose()` / `popPose()` | 保存 / 恢复变换（内部是 `pushMatrix` / `popMatrix`） |
| `translate(x, y)` | 平移 |
| `scale(x, y)` | 缩放（2D） |
| `rotateDegrees(degrees)` | 绕原点旋转（角度制） |
| `getPoseStack()` | 直接拿 `Matrix3x2fStack` 做更复杂的变换 |

## 其它

| 方法 | 说明 |
|---|---|
| `getWindow()` | 当前窗口（`com.mojang.blaze3d.platform.Window`） |
| `getPartialTick()` | 当前帧的 partial tick |
| `getScreen()` | 当前 Screen（仅 `onScreenPreRender/Post`） |
| `getMouseX()` / `getMouseY()` | 鼠标坐标（仅 `onScreenPreRender/Post`） |

## 示例

```js
RenderJSEvents.onGuiPreRender(event => {
  // 一个带标题的面板
  event.fill(10, 10, 210, 64, 0xAA000000)
  event.hLine(10, 210, 10, 0xFFFFFFFF)
  event.drawString(Text.ofString('RenderJS'), 16, 18, 0xFFFFFFFF)
  event.drawShadowString(Text.ofString('Hello world'), 16, 34, 0xFF00FFFF)

  // 物品图标 + 缩放后的贴图
  event.renderGuiItem(Item.of('minecraft:diamond'), 180, 18)

  event.pushPose()
  event.translate(30, 90)
  event.scale(2, 2)
  event.rotateDegrees(-10)
  event.drawString(Text.ofString('ROTATED'), 0, 0, 0xFFFFFF00)
  event.popPose()
})

RenderJSEvents.onScreenPreRender(event => {
  // 背包/箱子界面内，在鼠标位置画个小方块
  event.fill(event.getMouseX() - 2, event.getMouseY() - 2, event.getMouseX() + 2, event.getMouseY() + 2, 0xFFFF0000)
})
```

## 注意

- 只能在事件回调里绘制：26.1.2 的 GUI 是「先 extract 再统一渲染」，没有可以直接 new 出来的绘制上下文；
- `fill`/`drawString` 等会登记到当前帧的 render state，同帧生效；
- 旧版 `blitOffset` 参数仍保留在签名里（兼容旧脚本），但 GUI 已改为分层（stratum）模型，它不再影响层级；
- 目前没有暴露 scissor（裁剪）API；需要裁剪时可先用原版 GUI 绘制逻辑，或自行通过 pose 控制绘制范围。
