plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.multiplatform"

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.compose.multiplatform)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.ksp)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("multiplatform") {
            id = "katana.multiplatform"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.MultiplatformConventionPlugin"
        }
        register("multiplatform-android") {
            id = "katana.multiplatform.android"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.MultiplatformAndroidConventionPlugin"
        }
        register("multiplatform-compose") {
            id = "katana.multiplatform.compose"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.MultiplatformComposeConventionPlugin"
        }
    }
}
