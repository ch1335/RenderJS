# Getting Started

## Requirements

- Minecraft 26.1.2 + NeoForge 26.1.2.122+
- [KubeJS](https://kubejs.com/) 26.1.2-8.0.7+ (and its Rhino dependency)
- RenderJS `26.1.2-1.0.0`

## Installation

1. Put `renderjs-neoforge-26.1.2-1.0.0.jar` into `mods/`;
2. Launch the client once so KubeJS creates its script folders.

> The mod loads on both sides, but **all rendering features are client-side only**.
> Do not put render events in server scripts.

## Where scripts live

Client scripts go into `kubejs/client_scripts/`:

```
instance root/ (or modpack root/)
  kubejs/
    client_scripts/
      renderjs_demo.js
```

Reload the client scripts after editing, or press **F3 + T** to reload scripts and resources.

## First script

```js
// kubejs/client_scripts/renderjs_demo.js
RenderJSEvents.onGuiPreRender(event => {
  event.fill(8, 8, 168, 34, 0xAA000000)                    // semi-transparent black
  event.drawString(Text.ofString('Hello, RenderJS!'), 12, 12, 0xFFFFFFFF)
})

RenderJSEvents.onScreenPreRender(event => {
  event.fill(4, 4, 12, 12, 0xFFFF0000)                     // red square in any screen
})
```

More runnable examples: [`examples/client_scripts/`](../../../examples/client_scripts/).

## Colors

Colors are **8-digit ARGB** hex values written as `0xAARRGGBB`:

| Value | Meaning | Visible |
|---|---|---|
| `0xFFFFFFFF` | opaque white | yes |
| `0x80FF0000` | semi-transparent red | yes |
| `0xFFFF0000` | opaque red | yes |
| `0x00FF00` (6 digits) | actually `0x0000FF00`, alpha = 0 | no (fully transparent) |
| `0xFF00` (4 digits) | actually `0x0000FF00`, alpha = 0 | no |

If your tooling complains that a color is out of `int` range, append `| 0` (`0xFF00FF00 | 0`).
RenderJS color parameters are `long`, so `0xFF00FF00` also works directly.

## Common objects

| Purpose | How |
|---|---|
| Text | `Text.ofString('hello')` (`Component` is an alias) |
| Resource ID | pass a string `'minecraft:textures/gui/...'`, or `ID.of('ns:path')` |
| Item | `Item.of('minecraft:diamond')` |
| Block state | `Blocks.DIAMOND_BLOCK.defaultBlockState()`; most APIs also accept a string `'minecraft:diamond_block'` |
| Position | `new BlockPos(x, y, z)` |
| Color | `0xAARRGGBB` (long) |

## Debugging

- Log output: `console.info('...')`, `console.warn('...')`, `console.error('...')`;
- Client log: `run/logs/latest.log` (dev) or `logs/latest.log` (instance);
- Script errors are printed by KubeJS with the line number in your script;
- **F3 + T** reloads scripts/resources, much faster than restarting.

## FAQ

**Q: Nothing is drawn.**
Check the color alpha first — 6-digit colors have alpha = 0 and are invisible. Make sure the script is in
`client_scripts/` and draws inside an event callback, not at top level.

**Q: `Cannot convert 4278255360 to int`?**
Older RenderJS versions used `int` colors; this version uses `long`. If it still happens, write
`0xFF00FF00 | 0`.

**Q: The callback runs every frame — will it lag?**
Yes, `onGuiPreRender`, `onScreenPreRender`, `onLevelRender`, etc. fire every frame. Avoid heavy loops,
allocations and complex work inside them; cache data outside the callback.

**Q: Can I use it in server scripts?**
No. Rendering events are client-side; dedicated servers have no rendering context.

**Q: Item decorations are registered multiple times?**
`RegisterItemDecorations` fires on every resource/script reload, and RenderJS clears the old registrations
before applying the new ones. Do not register decorators from per-frame events.
