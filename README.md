# GlyphSynapse

Always-On Display controller for Nothing Phone (2) and Nothing Phone (2a) Pro.
Drive the Glyph Interface matrix with smooth, looping animations from a first-party-quality NothingOS app.

---

## Features

| Category | Feature |
|---|---|
| Animations | 8 built-in scenes: Breathe, Cascade, Orbit, Pulse Wave, Heartbeat, Matrix Rain, Charging Fill, Idle Drift |
| Control | Speed multiplier 0.25×–4.0×, Brightness 0–100%, Stealth Mode (caps at 15%) |
| AOD | Auto-starts animation on screen-off, stops on screen-on |
| Smart | Charging Fill override when plugged in, notification-aware zone reactions |
| Schedule | Time-window auto-activation (e.g. 22:00–07:00 nightstand mode) |
| Per-Zone | Assign different animations to Ring, Strip, Battery zones independently |
| Presets | Save, load, export/import via Android Share Sheet |
| Quick Tile | Toggle GlyphSynapse from Quick Settings panel |

---

## Requirements

- **Nothing Phone (3)** (25×25 LED matrix) or **Nothing Phone (4a) Pro** (13×13 LED matrix)
- NothingOS system version **20250801** or later
- Android **14+** (API 34+)

---

## Setup Instructions

See **[SETUP.md](SETUP.md)** for SDK installation and sideloading steps.

---

## Building

### 1. Clone the repo

```bash
git clone https://github.com/yourname/GlyphSynapse.git
cd GlyphSynapse
```

### 2. Add fonts

Place the following in `app/src/main/res/font/`:
- `spacemono_regular.ttf` — [Google Fonts](https://fonts.google.com/specimen/Space+Mono)
- `spacemono_bold.ttf`
- `ndot55.ttf` — Nothing proprietary font (optional; app falls back to dot-matrix renderer)

### 3. Add GlyphMatrix SDK AAR

Download `glyph-matrix-sdk-2.0.aar` from [GlyphMatrix-Developer-Kit](https://github.com/Nothing-Developer-Programme/GlyphMatrix-Developer-Kit) and place it in `app/libs/`. See [SETUP.md](SETUP.md) for full instructions.

### 4. Build

```bash
./gradlew assembleDebug
```

The APK is at `app/build/outputs/apk/debug/app-debug.apk`.

---

## Architecture

```
GlyphSynapse
├── data/
│   ├── datastore/      — UserPreferencesRepository (DataStore)
│   └── glyph/          — GlyphManagerWrapper, GlyphMap, channel definitions
├── domain/
│   ├── animation/      — AnimationDefinition interface + 8 scene implementations
│   ├── engine/         — AnimationPlayer, FrameScheduler (30fps coroutine loop)
│   ├── model/          — Preset, ScheduleWindow, ZoneAssignment
│   └── usecase/        — Preset CRUD operations
├── service/
│   ├── GlyphAnimationService        — Foreground service, screen-off trigger
│   ├── GlyphNotificationListenerService — Call/message/alarm reactions
│   ├── GlyphTileService             — Quick Settings tile
│   └── BootReceiver                 — Auto-start after reboot
└── ui/
    ├── theme/          — NothingOS colours, typography, dot-matrix renderer
    ├── components/     — GlyphPreview, AnimationCard, NothingSlider, NothingSwitch
    ├── screens/        — Home, Animations, Settings, Presets, Advanced
    └── viewmodel/      — StateFlow-based ViewModels
```

---

## Running Tests

```bash
./gradlew test
```

Unit tests cover `AnimationPlayer`, `FrameScheduler`, and all 8 `AnimationDefinition.tick()` implementations.

---

## Contributing

PRs welcome. Match the NothingOS visual language — no rounded corners above 4dp, no light theme, Space Mono for body text.
