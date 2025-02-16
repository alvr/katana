package dev.alvr.katana.buildlogic.mp.data

import dev.alvr.katana.buildlogic.bundleImplementation
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.desktopMain
import dev.alvr.katana.buildlogic.mp.desktopTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformDataPreferencesPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "katana.multiplatform.core")
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform(project) }
        }
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform(project: Project) {
        configureSourceSets()
        kspDependencies(project, "data-preferences")
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain.dependencies {
                bundleImplementation("data-preferences-common")
            }
            androidMain.dependencies {
                bundleImplementation("data-preferences-android")
            }
            iosMain.dependencies {
                bundleImplementation("data-preferences-ios")
            }
            desktopMain.dependencies {
                bundleImplementation("data-preferences-desktop")
            }
            jsMain.dependencies {
                bundleImplementation("data-preferences-js")
            }
            wasmJsMain.dependencies {
                bundleImplementation("data-preferences-wasm")
            }

            commonTest.dependencies {
                bundleImplementation("data-preferences-common-test")
            }
            androidUnitTest.dependencies {
                bundleImplementation("data-preferences-android-test")
            }
            iosTest.dependencies {
                bundleImplementation("data-preferences-ios-test")
            }
            desktopTest.dependencies {
                bundleImplementation("data-preferences-desktop-test")
            }
            jsTest.dependencies {
                bundleImplementation("data-preferences-js-test")
            }
            wasmJsTest.dependencies {
                bundleImplementation("data-preferences-wasm-test")
            }
        }
    }
}
