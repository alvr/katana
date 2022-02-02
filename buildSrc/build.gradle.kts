plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.detekt)
    implementation(libs.gradle.hilt)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.serialization)
    implementation(libs.gradle.updates)
}
