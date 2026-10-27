# RenderJS

一个给 [KubeJS](https://kubejs.com/) 提供渲染能力的 Minecraft 附属模组。
可以在 KubeJS 客户端脚本里直接绘制 HUD、Screen、世界几何、实体/方块实体叠加层与物品图标装饰。
适配 Minecraft 26.1.2 / NeoForge 26.1.2.122+ / KubeJS 26.1.2-8.0.7+。

## 快速开始

1. 把 `renderjs` 与 KubeJS 放进 `mods/`，启动一次客户端；
2. 在 `kubejs/client_scripts/` 新建脚本：

```js
RenderJSEvents.onGuiPreRender(event => {
  event.fill(8, 8, 168, 34, 0xAA000000)                    // ARGB：必须带 alpha
  event.drawString(Text.ofString('Hello, RenderJS!'), 12, 12, 0xFFFFFFFF)
})

RenderJSEvents.onScreenPreRender(event => {
  event.fill(4, 4, 12, 12, 0xFFFF0000)
})
```

3. 进游戏即可看到效果；修改脚本后重载客户端脚本即可看到效果。

更多可运行示例见 [`examples/client_scripts/`](examples/client_scripts/)。

## 文档

中文文档：[`docs/zh_cn/首页.md`](docs/zh_cn/首页.md)
English documentation: [`docs/en_us/Home.md`](docs/en_us/Home.md)

| 文档 | 内容 |
|---|---|
| [快速开始](docs/zh_cn/usage/快速开始.md) | 安装、脚本位置、颜色与常用对象、调试与常见问题 |
| [事件](docs/zh_cn/usage/事件.md) | `RenderJSEvents` 全部事件与触发时机 |
| [GUI 绘制](docs/zh_cn/usage/GUI绘制.md) | HUD / Screen 的文字、贴图、矩形、物品与 2D 变换 |
| [世界渲染](docs/zh_cn/usage/世界渲染.md) | 世界中的方块、物品、几何体、选中框与相机坐标 |
| [实体与方块实体](docs/zh_cn/usage/实体与方块实体.md) | 生物实体事件与方块实体渲染器 |
| [物品装饰](docs/zh_cn/usage/物品装饰.md) | 物品图标角标的注册与绘制 |
