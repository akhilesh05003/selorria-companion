buildscript {
    dependencies {
        // AGP 9.x has built-in Kotlin. Explicitly align the Kotlin compiler/runtime
        // with the Kotlin 2.4.20 stdlib used by current dependencies.
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.20")
    }
}

plugins {
    id("com.android.application") version "9.2.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.20" apply false
}
