plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.multiplatform"
version = extra["katana.plugins.version"].toString()

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.compose.multiplatform)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.ksp)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("multiplatform-core") {
            id = "katana.multiplatform.core"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformCorePlugin"
        }
        register("multiplatform-mobile") {
            id = "katana.multiplatform.mobile"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformMobilePlugin"
        }
        register("multiplatform-compose") {
            id = "katana.multiplatform.compose"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformComposePlugin"
        }
    }
}
