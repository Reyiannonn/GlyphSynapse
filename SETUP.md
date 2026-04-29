# GlyphSynapse — Setup & Sideloading Guide

## Supported Devices

| Device | Matrix | SDK Constant |
|---|---|---|
| Nothing Phone (3) | 25 × 25 LEDs | `Glyph.DEVICE_23112` |
| Nothing Phone (4a) Pro | 13 × 13 LEDs | `Glyph.DEVICE_25111p` |

> **Min Android version:** Android 14 (API 34).  
> **Required NothingOS system version:** 20250801 or later for `setAppMatrixFrame()`.

---

## 1. Obtain the Nothing GlyphMatrix SDK

The SDK is distributed as an AAR via the official repository:

1. Go to **[github.com/Nothing-Developer-Programme/GlyphMatrix-Developer-Kit](https://github.com/Nothing-Developer-Programme/GlyphMatrix-Developer-Kit)**
2. Download `glyph-matrix-sdk-2.0.aar`
3. Place it in `app/libs/`:
   ```bash
   mkdir -p app/libs
   cp ~/Downloads/glyph-matrix-sdk-2.0.aar app/libs/
   ```

The `app/build.gradle.kts` already references all AARs in `libs/` via:
```kotlin
compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
```

> `compileOnly` is correct — the SDK classes are provided by NothingOS at runtime.

---

## 2. Add Fonts

Place in `app/src/main/res/font/`:
- `spacemono_regular.ttf` — [Google Fonts](https://fonts.google.com/specimen/Space+Mono)
- `spacemono_bold.ttf`
- `ndot55.ttf` — Nothing proprietary font (needs Nothing developer licence; app falls back to dot-matrix renderer without it)

---

## 3. Enable Developer Options

```bash
# Verify device is connected
adb devices

# Enable Glyph SDK debug mode (auto-disables after 48 h)
adb shell settings put global nt_glyph_interface_debug_enable 1
```

---

## 4. Grant Special Permissions via ADB

After installing the APK:

```bash
# Notification listener (required for notification-aware mode)
adb shell cmd notification allow_listener \
  com.glyphsynapse.app/.service.GlyphNotificationListenerService

# Glyph SDK permission
adb shell pm grant com.glyphsynapse.app \
  com.nothing.ketchum.permission.ENABLE
```

---

## 5. Build and Sideload

```bash
./gradlew assembleDebug

adb install -r app/build/outputs/apk/debug/app-debug.apk

adb shell am start -n com.glyphsynapse.app/.MainActivity
```

---

## 6. Add Quick Settings Tile

1. Pull down the notification shade twice
2. Tap the edit pencil icon
3. Find **GlyphSynapse** and drag it to your active tiles

---

## 7. Verify GlyphMatrix Service

```bash
adb shell dumpsys activity services | grep -i glyphmatrix
```

If no output: ensure you are on NothingOS system version ≥ 20250801.

---

## 8. Troubleshooting

| Symptom | Fix |
|---|---|
| `Incompatible Device` screen | Must be Nothing Phone (3) or Phone (4a) Pro |
| Animations do not appear | Check NothingOS version ≥ 20250801 and SDK debug mode enabled |
| `SecurityException` on `setAppMatrixFrame` | Re-grant Glyph permission via ADB step 4 |
| Notification reactions not working | Confirm `BIND_NOTIFICATION_LISTENER_SERVICE` granted via ADB step 4 |
| Quick tile missing | Uninstall, reinstall, reboot |
