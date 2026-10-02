plugins {
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
}

kotlin {
    jvmToolchain(17)
}
