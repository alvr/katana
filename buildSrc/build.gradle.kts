plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.kotlin)
}
