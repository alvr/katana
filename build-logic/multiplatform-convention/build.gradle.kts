plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.multiplatform"
version = extra["katana.plugins.version"].toString()

dependencies {
    compileOnly(libs.gradle.android)
    compileOnly(libs.gradle.apollo)
    compileOnly(libs.gradle.buildconfig)
    compileOnly(libs.gradle.compose)
    compileOnly(libs.gradle.kotest)
    compileOnly(libs.gradle.kotlin)
    compileOnly(libs.gradle.ksp)
    compileOnly(libs.gradle.mockmp)
    implementation(libs.kotlinpoet)
    implementation(libs.yamlbeans)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("multiplatform-app") {
            id = "katana.multiplatform.app"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformAppPlugin"
        }
        register("multiplatform-compose") {
            id = "katana.multiplatform.compose"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformComposePlugin"
        }
        register("multiplatform-core") {
            id = "katana.multiplatform.core"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformCorePlugin"
        }
        register("multiplatform-data-preferences") {
            id = "katana.multiplatform.data.preferences"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformDataPreferencesPlugin"
        }
        register("multiplatform-data-remote") {
            id = "katana.multiplatform.data.remote"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformDataRemotePlugin"
        }
        register("multiplatform-mobile") {
            id = "katana.multiplatform.mobile"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformMobilePlugin"
        }
        register("multiplatform-tests") {
            id = "katana.multiplatform.tests"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformTestsPlugin"
        }
        register("multiplatform-ui") {
            id = "katana.multiplatform.ui"
            implementationClass = "dev.alvr.katana.buildlogic.multiplatform.KatanaMultiplatformUiPlugin"
        }
    }
}
