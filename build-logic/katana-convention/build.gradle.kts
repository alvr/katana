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
    implementation(libs.gradle.mockmp)
    implementation(libs.gradle.sentry)
    implementation(libs.kotlinpoet)
    implementation(libs.yamlbeans)
}

gradlePlugin {
    plugins {
        register("katana-common") {
            id = "katana.common"
            implementationClass = "dev.alvr.katana.buildlogic.common.KatanaCommonPlugin"
        }
        register("katana-detekt") {
            id = "katana.detekt"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.KatanaDetektPlugin"
        }
        register("katana-kover") {
            id = "katana.kover"
            implementationClass = "dev.alvr.katana.buildlogic.common.KatanaKoverPlugin"
        }

        register("katana-app-android") {
            id = "katana.app.android"
            implementationClass = "dev.alvr.katana.buildlogic.app.KatanaAppAndroidPlugin"
        }
        register("katana-app-ios") {
            id = "katana.app.ios"
            implementationClass = "dev.alvr.katana.buildlogic.app.KatanaAppIosPlugin"
        }

        register("katana-core") {
            id = "katana.core"
            implementationClass = "dev.alvr.katana.buildlogic.core.KatanaCorePlugin"
        }
        register("katana-test") {
            id = "katana.test"
            implementationClass = "dev.alvr.katana.buildlogic.core.KatanaTestPlugin"
        }

        register("katana-feature") {
            id = "katana.feature"
            implementationClass = "dev.alvr.katana.buildlogic.feature.KatanaFeaturePlugin"
        }

        register("katana-feature-data-preferences") {
            id = "katana.feature.data.preferences"
            implementationClass = "dev.alvr.katana.buildlogic.feature.data.KatanaDataPreferencesPlugin"
        }
        register("katana-feature-data-remote") {
            id = "katana.feature.data.remote"
            implementationClass = "dev.alvr.katana.buildlogic.feature.data.KatanaDataRemotePlugin"
        }

        register("katana-feature-ui") {
            id = "katana.feature.ui"
            implementationClass = "dev.alvr.katana.buildlogic.feature.ui.KatanaUiPlugin"
        }
        register("katana-feature-ui-compose") {
            id = "katana.feature.ui.compose"
            implementationClass = "dev.alvr.katana.buildlogic.feature.ui.KatanaComposePlugin"
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
