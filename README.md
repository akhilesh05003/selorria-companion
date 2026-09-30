# Selorria Companion — MVP v0.1

Selorria Companion is an Android MVP for a character-driven reminder experience.

## MVP workflow

1. Enter a reminder message.
2. Choose a time.
3. Tap Set Reminder.
4. At the scheduled time, the companion screen opens.
5. The companion displays the message and reads it aloud using Android Text-to-Speech.

## Current scope

- One scheduled reminder at a time
- One cute companion illustration
- Android system TTS
- Exact alarm scheduling
- Reminder screen with Snooze / I'm Up controls

## Deliberately not included yet

- Personalized voice cloning
- Calendar integration
- Cloud AI
- Multiple characters
- 3D animated model
- Conversation
- Accounts/sync

## Build

Open the project in Android Studio and let Gradle sync.

For Android 12+, the app may ask for permission to schedule exact alarms.
For Android 13+, notification permission is declared for future notification-based flows.

This is a development MVP, not a production release.


## Build APK online with GitHub Actions

1. Create a new GitHub repository.
2. Upload the contents of this project to the repository's `main` branch.
3. Open **Actions**.
4. Select **Build Selorria Companion APK**.
5. Run the workflow.
6. Open the completed workflow run.
7. Under **Artifacts**, download `selorria-companion-debug-apk`.
8. Extract the downloaded artifact and install `app-debug.apk` on your Android phone.

You do not need Android Studio for this online build.
