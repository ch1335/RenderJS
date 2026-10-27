# RenderJS

English | [简体中文](README.md)

A Minecraft add-on that brings rendering capabilities to [KubeJS](https://kubejs.com/).
KubeJS client scripts can draw HUDs, screens, world geometry, entity / block entity overlays and item icon decorations directly.
Supports Minecraft 26.1.2 / NeoForge 26.1.2.122+ / KubeJS 26.1.2-8.0.7+.

## Quick start

1. Put `renderjs` and KubeJS into `mods/` and start the client once;
2. Create a script in `kubejs/client_scripts/`:

```js
RenderJSEvents.onGuiPreRender(event => {
  event.fill(8, 8, 168, 34, 0xAA000000)                    // ARGB: alpha is required
  event.drawString(Text.ofString('Hello, RenderJS!'), 12, 12, 0xFFFFFFFF)
})

RenderJSEvents.onScreenPreRender(event => {
  event.fill(4, 4, 12, 12, 0xFFFF0000)
})
```

3. Enter the game to see the result; reload client scripts after editing them.

More runnable examples: [`examples/client_scripts/`](examples/client_scripts/).

## Documentation

English documentation: [`docs/en_us/Home.md`](docs/en_us/Home.md)
Chinese documentation: [`docs/zh_cn/首页.md`](docs/zh_cn/首页.md)

| Document | Description |
|---|---|
| [Getting Started](docs/en_us/usage/getting-started.md) | Installation, script location, colors, common objects, debugging and FAQ |
| [Events](docs/en_us/usage/events.md) | All `RenderJSEvents` events and when they fire |
| [GUI Rendering](docs/en_us/usage/gui.md) | HUD / Screen text, textures, rectangles, items and 2D transforms |
| [World Rendering](docs/en_us/usage/world.md) | Blocks, items, geometry, outlines and camera coordinates in the world |
| [Entities & Block Entities](docs/en_us/usage/entity-and-block-entity.md) | Living entity events and block entity renderers |
| [Item Decorations](docs/en_us/usage/item-decorations.md) | Registering and drawing overlays on item icons |
