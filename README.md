# Selorria Companion — Phase 2.2

## Facial Expression + Eye Behavior Foundation

This is a complete buildable package for the Selorria Companion Phase 2.2 test.

### Included

- Android Kotlin + Jetpack Compose
- SceneView / Filament 3D renderer
- GLB character pipeline
- AlarmManager exact reminder scheduling
- Reminder receiver
- Full-screen companion alarm screen
- Android Text-to-Speech
- 3D camera orbit / pan / zoom
- Phase 2.1 idle-capable character scene
- Phase 2.2 expression controller
- Automatic blink scheduler
- Neutral / Happy / Thinking expression controls
- GitHub Actions APK build workflow
- Placeholder GLB so the project can be built and tested immediately

### Expression contract for the production lady character

The final rigged GLB should contain animation clips with these names:

- `Idle`
- `Blink`
- `Happy`
- `Thinking`
- `Talk`

The app already switches between these names. If a clip exists in the GLB,
SceneView will play it. If the current placeholder has no such facial clips,
the controller still works but there will be no facial deformation to display.

SceneView supports glTF/GLB skeletal and morph animations, and `ModelNode`
accepts an animation name, loop flag and playback speed.

### Why the real face is not faked in this package

The supplied placeholder model is not a production humanoid facial rig.
Real blinking and expressions require a character asset containing either:

1. facial bones / skeletal animation, or
2. morph targets / blend shapes.

The next asset should therefore be a rigged GLB based on the selected Selorria
lady design.

### Phase roadmap

2.0 — 3D renderer + character slot
2.1 — idle / breathing foundation
2.2 — expression controller + blink foundation
2.3 — production rig + real facial animation
2.4 — speech-driven lip sync
2.5 — gestures and emotional state
2.6 — AI-generated reminder dialogue
2.7 — consent-based personalized voice


## Build compatibility fix

Phase 2.2 is pinned to the Android 35 / AGP 8.10 toolchain for the test build.
The dependency set avoids the Compose 1.12.x / compileSdk 37 / AGP 9.1 requirement
reported by AAR metadata checks.

Gradle 8.11.1 is used because the Android Gradle Plugin requires it.
