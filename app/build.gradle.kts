plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.selorria.companion"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.selorria.companion"
        minSdk = 24
        targetSdk = 35
        versionCode = 20
        versionName = "2.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.01.00"))
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // Current 3D/GLB renderer based on Filament.
    implementation("io.github.sceneview:sceneview:4.51.0")

    implementation("androidx.core:core-ktx:1.15.0")
}
