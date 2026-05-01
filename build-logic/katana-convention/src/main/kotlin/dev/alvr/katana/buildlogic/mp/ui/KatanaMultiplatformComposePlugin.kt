package dev.alvr.katana.buildlogic.mp.ui

import dev.alvr.katana.buildlogic.bundleImplementation
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.androidHostTest
import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformComposePlugin : Plugin<Project> {

    override fun apply(target: Project) =
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.multiplatform")
            apply(plugin = "org.jetbrains.compose")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")
            apply(plugin = "com.github.skydoves.compose.stability.analyzer")

            with(extensions) {
                configure<KotlinMultiplatformExtension> { configureMultiplatform() }
                configure<ComposeExtension> { configureComposeResources() }
                configure<ComposeCompilerGradlePluginExtension> { configureComposeCompiler() }
            }
        }

    context(project: Project)
    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        configureSourceSets()

        kspDependencies("ui")
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain.dependencies { bundleImplementation("ui-common") }
            androidMain.dependencies { bundleImplementation("ui-android") }
            iosMain.dependencies { bundleImplementation("ui-ios") }

            commonTest.dependencies { bundleImplementation("ui-common-test") }
            androidHostTest.dependencies { bundleImplementation("ui-android-test") }
            iosTest.dependencies { bundleImplementation("ui-ios-test") }
        }
    }

    context(project: Project)
    private fun ComposeCompilerGradlePluginExtension.configureComposeCompiler() {
        metricsDestination = project.file(project.composePluginDir("compose-metrics"))
        reportsDestination = project.file(project.composePluginDir("compose-reports"))
    }

    context(project: Project)
    private fun ComposeExtension.configureComposeResources() {
        val resources = (this as ExtensionAware).extensions.getByType<ResourcesExtension>()
        resources.packageOfResClass = "${project.fullPackageName}.resources"
    }

    private fun Project.composePluginDir(directory: String) =
        File(layout.buildDirectory.asFile.get(), directory).absolutePath
}
