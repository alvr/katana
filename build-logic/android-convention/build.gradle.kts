plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.android"
version = extra["katana.plugins.version"].toString()

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.compose)
    implementation(libs.gradle.kotlin)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("android-application") {
            id = "katana.android.application"
            implementationClass = "dev.alvr.katana.buildlogic.android.KatanaAndroidApplicationPlugin"
        }
    }
}
