# GlyphSynapse

Always-On Display controller for **Nothing Phone (3)** and **Nothing Phone (4a) Pro**.
Drive the Glyph Interface matrix with smooth, organic, and audio-reactive animations from a first-party-quality NothingOS app.

---

## Features

| Category | Feature |
|---|---|
| **Animations** | 8 premium scenes: **Vitality**, **Synapse**, **Presence**, **Cascade**, **Orbit**, **Heartbeat**, **Matrix Rain**, **Charging Fill** |
| **Audio-Reactive** | Animations accelerate and react to music (Bass, Mid, Energy). "Submerged speaker" physics for liquid effects. |
| **Stability** | Temporal integration ensures smooth motion during media playback—no phase jumps or chaotic teleporting. |
| **Control** | Speed multiplier 0.25×–4.0×, Brightness 0–100%, Stealth Mode (caps at 15%) |
| **24/7 Operation** | Optimized for Always-On usage with no hardware timeouts or service interruptions. |
| **Smart** | Charging Fill override when plugged in, notification-aware reactions (Call/Message/Alarm). |
| **Presets** | Save, load, and export/import animation configurations via Android Share Sheet. |
| **Quick Tile** | Toggle GlyphSynapse service directly from the Quick Settings panel. |

---

## Requirements

- **Nothing Phone (3)** (25×25 LED matrix) or **Nothing Phone (4a) Pro** (13×13 LED matrix).
- NothingOS system version **20250801** or later.
- Android **14+** (API 34/35).
- **Microphone Permission**: Required for audio-reactive animation features.

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

Download `glyph-matrix-sdk-2.0.aar` from [GlyphMatrix-Developer-Kit](https://github.com/Nothing-Developer-Programme/GlyphMatrix-Developer-Kit) and place it in `app/libs/`.

### 4. Build

```bash
./gradlew assembleDebug
```

The APK is located at `app/build/outputs/apk/debug/app-debug.apk`.

---

## Architecture

```
GlyphSynapse
├── data/
│   ├── datastore/      — UserPreferencesRepository (DataStore)
│   └── glyph/          — GlyphManagerWrapper (SDK interface), GlyphMatrixDevice detection
├── domain/
│   ├── animation/      — AnimationDefinition interface + 8 premium scene implementations
│   ├── engine/         — AnimationPlayer, FrameScheduler (Stable speed integration)
│   └── model/          — Preset, ScheduleWindow, PixelFrame
├── service/
│   ├── GlyphAnimationService        — Foreground service (Microphone + Special Use)
│   ├── GlyphNotificationListenerService — Call/message/alarm reactions
│   ├── GlyphTileService             — Quick Settings tile
│   └── BootReceiver                 — Auto-start after reboot
└── ui/
    ├── theme/          — NothingOS colours, typography, DotMatrixText renderer
    ├── components/     — GlyphPreview, AnimationCard, NothingControls (Optimized rendering)
    ├── screens/        — Home, Animations, Settings, Presets, Advanced
    └── viewmodel/      — StateFlow-based ViewModels
```

---

## Running Tests

```bash
./gradlew test
```

Unit tests cover the `AnimationPlayer` logic, `FrameScheduler` temporal stability, and `AnimationDefinition.tick()` implementations.

---

## Contributing

PRs welcome. Match the NothingOS visual language — no rounded corners above 4dp, no light theme, Space Mono for body text.
