plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.android"
version = extra["katana.plugins.version"].toString()

dependencies {
    compileOnly(libs.gradle.android)
    compileOnly(libs.gradle.compose)
    compileOnly(libs.gradle.kotlin)
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
