@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.mp

import dev.alvr.katana.buildlogic.bundleImplementation
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.kspDependencies
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

internal fun Project.commonConfiguration(
    configureAndroid: KotlinAndroidTarget.() -> Unit = { },
    configureApple: KotlinNativeTarget.() -> Unit = { },
    configureDesktop: KotlinJvmTarget.() -> Unit = { },
    configureJs: KotlinJsTargetDsl.() -> Unit = { browser() },
    configureWasmJs: KotlinWasmJsTargetDsl.() -> Unit = { browser() },
) {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    apply(plugin = "com.google.devtools.ksp")
    apply(plugin = "io.kotest.multiplatform")
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "dev.mokkery")

    with(extensions) {
        commonExtensions()
        configure<KotlinMultiplatformExtension> {
            configureMultiplatform(
                project = project,
                configureAndroid = configureAndroid,
                configureApple = configureApple,
                configureDesktop = configureDesktop,
                configureJs = configureJs,
                configureWasmJs = configureWasmJs,
            )
        }
    }

    tasks.commonTasks()
}

private fun KotlinMultiplatformExtension.configureMultiplatform(
    project: Project,
    configureAndroid: KotlinAndroidTarget.() -> Unit = { },
    configureApple: KotlinNativeTarget.() -> Unit = { },
    configureDesktop: KotlinJvmTarget.() -> Unit = { },
    configureJs: KotlinJsTargetDsl.() -> Unit = { },
    configureWasmJs: KotlinWasmJsTargetDsl.() -> Unit = { },
) {
    hierarchy(
        configureAndroid = configureAndroid,
        configureApple = configureApple,
        configureDesktop = configureDesktop,
        configureJs = configureJs,
        configureWasmJs = configureWasmJs,
    )
    configureSourceSets()

    kspDependencies(project, "core")
}

private fun KotlinMultiplatformExtension.configureSourceSets() {
    sourceSets {
        commonMain.dependencies {
            bundleImplementation("core-common")
        }
        androidMain.dependencies {
            bundleImplementation("core-android")
        }
        iosMain.dependencies {
            bundleImplementation("core-ios")
        }
        desktopMain.dependencies {
            bundleImplementation("core-desktop")
        }
        jsMain.dependencies {
            bundleImplementation("core-js")
        }
        wasmJsMain.dependencies {
            bundleImplementation("core-wasm")
        }

        commonTest.dependencies {
            bundleImplementation("core-common-test")
        }
        androidUnitTest.dependencies {
            bundleImplementation("core-android-test")
        }
        iosTest.dependencies {
            bundleImplementation("core-ios-test")
        }
        desktopTest.dependencies {
            bundleImplementation("core-desktop-test")
        }
        jsTest.dependencies {
            bundleImplementation("core-js-test")
        }
        wasmJsTest.dependencies {
            bundleImplementation("core-wasm-test")
        }
    }
}
