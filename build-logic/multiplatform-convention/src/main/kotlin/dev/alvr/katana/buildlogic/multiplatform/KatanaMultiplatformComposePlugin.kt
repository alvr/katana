package dev.alvr.katana.buildlogic.multiplatform

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.catalogLib
import java.io.File
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformComposePlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "katana.multiplatform.mobile")
        apply(plugin = "com.google.devtools.ksp")
        apply(plugin = "org.jetbrains.compose")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureSourceSets() }
            configure<ComposeExtension> { configureComposeMultiplatform(project) }
        }

        dependencies.kspDependencies(project)
    }

    @Suppress("UNUSED_VARIABLE")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        val compose = (this as ExtensionAware).extensions.getByName("compose") as ComposePlugin.Dependencies

        configureSourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(compose.animation)
                    implementation(compose.foundation)
                    implementation(compose.material)
                    implementation(compose.material3)
                    implementation(compose.materialIconsExtended)
                    implementation(compose.runtime)
                    implementation(compose.ui)
                    implementation(catalogBundle("ui-common"))
                }
            }
            val androidMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(compose.preview)
                    implementation(compose.uiTooling)
                    implementation(catalogBundle("ui-android"))
                }
            }
            val iosMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("ui-ios"))
                }
            }
            val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

            val commonTest by getting {
                dependencies {
                    implementation(catalogBundle("ui-common-test"))
                }
            }
            val androidUnitTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("ui-android-test"))
                }
            }
            val iosTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("ui-ios-test"))
                }
            }
            val iosSimulatorArm64Test by getting { dependsOn(iosTest) }
        }
    }

    private fun DependencyHandler.kspDependencies(project: Project) {
        add("kspCommonMainMetadata", project.catalogBundle("ui-common-ksp"))
        add("kspAndroid", project.catalogBundle("ui-android-ksp"))
        add("kspIosX64", project.catalogBundle("ui-ios-ksp"))
    }

    private fun ComposeExtension.configureComposeMultiplatform(project: Project) {
        kotlinCompilerPlugin.set(project.catalogLib("compose-compiler").get().toString())

        kotlinCompilerPluginArgs.set(
            buildList {
                if (project.composePluginEnabled("enableComposeCompilerMetrics")) {
                    add("metricsDestination=${project.composePluginDir("compose-metrics")}")
                }

                if (project.composePluginEnabled("enableComposeCompilerReports")) {
                    add("reportsDestination=${project.composePluginDir("compose-reports")}")
                }
            },
        )
    }

    private fun Project.composePluginEnabled(property: String) =
        providers.gradleProperty(property).orNull == "true"

    private fun Project.composePluginDir(directory: String) =
        File(buildDir, directory).absolutePath
}
