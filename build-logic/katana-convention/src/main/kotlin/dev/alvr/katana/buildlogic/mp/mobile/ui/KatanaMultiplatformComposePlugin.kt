package dev.alvr.katana.buildlogic.mp.mobile.ui

import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.catalogLib
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.androidUnitTest
import dev.alvr.katana.buildlogic.mp.configureSourceSets
import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "org.jetbrains.compose")

        group = fullPackageName.substringBeforeLast('.')

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
            configure<ComposeExtension> { configureComposeMultiplatform() }
        }
    }

    context(Project)
    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        configureSourceSets()
        kspDependencies("ui")
    }

    @OptIn(ExperimentalComposeLibrary::class)
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        val compose = (this as ExtensionAware).extensions
            .getByName("compose") as ComposePlugin.Dependencies

        configureSourceSets {
            commonMain {
                dependencies {
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

    context(Project)
    private fun ComposeExtension.configureComposeMultiplatform() {
        kotlinCompilerPlugin.set(catalogLib("compose-compiler").get().toString())

        kotlinCompilerPluginArgs.set(
            buildList {
                if (composePluginEnabled("katana.enableComposeCompilerMetrics")) {
                    add("metricsDestination=${composePluginDir("compose-metrics")}")
                }

                if (composePluginEnabled("katana.enableComposeCompilerReports")) {
                    add("reportsDestination=${composePluginDir("compose-reports")}")
                }
            },
        )
    }

    private fun Project.composePluginEnabled(property: String) =
        providers.gradleProperty(property).orNull == "true"

    private fun Project.composePluginDir(directory: String) =
        File(layout.buildDirectory.asFile.get(), directory).absolutePath
}
