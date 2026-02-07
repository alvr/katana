package dev.alvr.katana.buildlogic.mp.core

import dev.alvr.katana.buildlogic.bundleImplementation
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.mp.hierarchy
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformTestsPlugin : Plugin<Project> {

    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "com.android.kotlin.multiplatform.library")
            apply(plugin = "org.jetbrains.kotlin.multiplatform")

            extensions.configure<KotlinMultiplatformExtension> { configureMultiplatform() }
            tasks.commonTasks()
        }

    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        hierarchy()
        configureSourceSets()
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain.dependencies {
                bundleImplementation("core-common-test")
                bundleImplementation("data-preferences-common-test")
                bundleImplementation("data-remote-common-test")
                bundleImplementation("ui-common-test")
            }
            androidMain.dependencies {
                bundleImplementation("core-android-test")
                bundleImplementation("data-preferences-android-test")
                bundleImplementation("data-remote-android-test")
                bundleImplementation("ui-android-test")
            }
            iosMain.dependencies {
                bundleImplementation("core-ios-test")
                bundleImplementation("data-preferences-ios-test")
                bundleImplementation("data-remote-ios-test")
                bundleImplementation("ui-ios-test")
            }
        }
    }
}
