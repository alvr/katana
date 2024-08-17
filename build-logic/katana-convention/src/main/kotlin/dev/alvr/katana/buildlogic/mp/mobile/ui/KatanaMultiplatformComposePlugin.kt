@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.mp.mobile.ui

import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.kspDependencies
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
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag.Companion.IntrinsicRemember
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag.Companion.OptimizeNonSkippingGroups
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag.Companion.StrongSkipping
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "org.jetbrains.compose")
        apply(plugin = "org.jetbrains.kotlin.plugin.compose")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform(project) }
            configure<ComposeExtension> { configureComposeResources(project) }
            configure<ComposeCompilerGradlePluginExtension> { configureComposeCompiler(project) }
        }
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform(project: Project) {
        configureSourceSets()
        kspDependencies(project, "ui")
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        val compose = (this as ExtensionAware).extensions.getByType<ComposePlugin.Dependencies>()

        sourceSets {
            commonMain.dependencies {
                implementation(compose.animation)
                implementation(compose.components.resources)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(catalogBundle("ui-common"))
            }
            androidMain.dependencies {
                implementation(compose.preview)
                implementation(compose.uiTooling)
                implementation(catalogBundle("ui-android"))
            }
            iosMain.dependencies {
                implementation(catalogBundle("ui-ios"))
            }

            commonTest.dependencies {
                implementation(catalogBundle("ui-common-test"))
            }
            androidUnitTest.dependencies {
                implementation(catalogBundle("ui-android-test"))
            }
            iosTest.dependencies {
                implementation(catalogBundle("ui-ios-test"))
            }
        }
    }

    private fun ComposeCompilerGradlePluginExtension.configureComposeCompiler(project: Project) {
        featureFlags = setOf(
            IntrinsicRemember,
            OptimizeNonSkippingGroups,
            StrongSkipping,
        )

        metricsDestination = project.file(project.composePluginDir("compose-metrics"))
        reportsDestination = project.file(project.composePluginDir("compose-reports"))
    }

    private fun ComposeExtension.configureComposeResources(project: Project) {
        val resources = (this as ExtensionAware).extensions.getByType<ResourcesExtension>()
        resources.packageOfResClass = "${project.fullPackageName}.resources"
    }

    private fun Project.composePluginDir(directory: String) =
        File(layout.buildDirectory.asFile.get(), directory).absolutePath
}
