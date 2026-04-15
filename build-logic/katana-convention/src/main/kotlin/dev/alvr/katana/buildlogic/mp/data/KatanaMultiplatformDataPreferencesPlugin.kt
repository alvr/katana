package dev.alvr.katana.buildlogic.mp.data

import dev.alvr.katana.buildlogic.bundleImplementation
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.androidHostTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformDataPreferencesPlugin : Plugin<Project> {

    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "katana.multiplatform.core")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            with(extensions) { configure<KotlinMultiplatformExtension> { configureMultiplatform() } }
        }

    context(project: Project)
    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        configureSourceSets()
        kspDependencies("data-preferences")
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain.dependencies { bundleImplementation("data-preferences-common") }
            androidMain.dependencies { bundleImplementation("data-preferences-android") }
            iosMain.dependencies { bundleImplementation("data-preferences-ios") }

            commonTest.dependencies { bundleImplementation("data-preferences-common-test") }
            androidHostTest.dependencies { bundleImplementation("data-preferences-android-test") }
            iosTest.dependencies { bundleImplementation("data-preferences-ios-test") }
        }
    }
}
