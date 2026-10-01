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
        versionCode = 3
        versionName = "0.3.0"
    }
    ndkVersion = "27.2.12479018"
    buildFeatures { buildConfig = true }
    externalNativeBuild {
        cmake { path = file("src/main/cpp/CMakeLists.txt") }
    }
}
dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-ktx:1.10.0")
    implementation("androidx.webkit:webkit:1.14.0")
}
