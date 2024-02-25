import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic"
version = extra["katana.plugins.version"].toString()

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.apollo)
    implementation(libs.gradle.buildconfig)
    implementation(libs.gradle.complete.kotlin)
    implementation(libs.gradle.compose)
    implementation(libs.gradle.detekt)
    implementation(libs.gradle.kotest)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.kover)
    implementation(libs.gradle.ksp)
    implementation(libs.gradle.mokkery)
    implementation(libs.gradle.sentry)
    implementation(libs.kotlinpoet)
    implementation(libs.yamlbeans)
}

gradlePlugin {
    plugins {
        register("common") {
            id = "katana.common"
            implementationClass = "dev.alvr.katana.buildlogic.common.KatanaCommonPlugin"
        }
        register("detekt") {
            id = "katana.detekt"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.KatanaDetektPlugin"
        }
        register("kover") {
            id = "katana.kover"
            implementationClass = "dev.alvr.katana.buildlogic.common.KatanaKoverPlugin"
        }
        register("multiplatform-app") {
            id = "katana.multiplatform.app"
            implementationClass = "dev.alvr.katana.buildlogic.mp.app.KatanaMultiplatformAppPlugin"
        }
        register("multiplatform-compose") {
            id = "katana.multiplatform.compose"
            implementationClass = "dev.alvr.katana.buildlogic.mp.mobile.ui.KatanaMultiplatformComposePlugin"
        }
        register("multiplatform-core") {
            id = "katana.multiplatform.core"
            implementationClass = "dev.alvr.katana.buildlogic.mp.core.KatanaMultiplatformCorePlugin"
        }
        register("multiplatform-data-preferences") {
            id = "katana.multiplatform.data.preferences"
            implementationClass = "dev.alvr.katana.buildlogic.mp.mobile.data.KatanaMultiplatformDataPreferencesPlugin"
        }
        register("multiplatform-data-remote") {
            id = "katana.multiplatform.data.remote"
            implementationClass = "dev.alvr.katana.buildlogic.mp.mobile.data.KatanaMultiplatformDataRemotePlugin"
        }
        register("multiplatform-mobile") {
            id = "katana.multiplatform.mobile"
            implementationClass = "dev.alvr.katana.buildlogic.mp.mobile.KatanaMultiplatformMobilePlugin"
        }
        register("multiplatform-tests") {
            id = "katana.multiplatform.tests"
            implementationClass = "dev.alvr.katana.buildlogic.mp.core.KatanaMultiplatformTestsPlugin"
        }
        register("multiplatform-ui") {
            id = "katana.multiplatform.ui"
            implementationClass = "dev.alvr.katana.buildlogic.mp.mobile.ui.KatanaMultiplatformUiPlugin"
        }
    }
}

tasks {
    withType<KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll("-Xcontext-receivers")
        }
    }
}
