plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.hacrrrrrrr.nexterm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hacrrrrrrr.nexterm"
        minSdk = 23
        targetSdk = 35
        versionCode = 4
        versionName = "0.6.0"
    }

    ndkVersion = "27.2.12479018"
    buildFeatures { buildConfig = true }

    externalNativeBuild {
        cmake { path = file("src/main/cpp/CMakeLists.txt") }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(project(":nexterm-internal"))
    implementation(project(":packages:cli"))
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-ktx:1.10.0")
    implementation("androidx.webkit:webkit:1.14.0")
}
