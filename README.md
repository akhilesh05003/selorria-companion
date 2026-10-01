# Selorria Companion Phase 2.2 — Clean Android Build

This package is a clean replacement for the previous mixed-source repository.

## Important

Replace the contents of the GitHub repository with this package. Do not merge individual Kotlin files into the old repository.

The previous build log showed three separate problems:

1. Old `MainActivity.kt` references (`statusText`, `timePicker`, `saveButton`) that do not belong to the current Compose implementation.
2. A duplicate `ReminderReceiver` declaration.
3. Kotlin compiler/runtime mismatch: compiler 2.2.x versus Kotlin stdlib 2.4.20.

This package removes the stale sources and aligns Kotlin to 2.4.20.

## Build stack

- Android Gradle Plugin: 9.2.0
- Gradle: 9.4.1
- Java: 17
- Kotlin: 2.4.20
- Compose Compiler plugin: 2.4.20
- compileSdk: 37
- targetSdk: 36
- minSdk: 24
- SceneView: 4.51.0

## GitHub Actions

The workflow installs Gradle 9.4.1 directly and runs `gradle :app:assembleDebug`. It intentionally does not depend on a stale `gradlew` wrapper from the old repository.

## Package

The Android namespace and application ID are:

`com.selorria.companion`

There should be exactly one `ReminderReceiver` and no `ReminderActivity` in this Phase 2.2 package.

## APK

After GitHub Actions succeeds, download the artifact:

`selorria-companion-phase2-debug-apk`
