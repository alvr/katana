package dev.alvr.katana.buildlogic.multiplatform

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.ResourcesDir
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.catalogLib
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.multiplatform.tasks.GenerateResourcesFileTask
import java.io.File
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformComposePlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "katana.multiplatform.mobile")
        apply(plugin = "org.jetbrains.compose")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureSourceSets() }
            configure<ComposeExtension> { configureComposeMultiplatform(project) }
        }

        generateResourcesTask()
        kspDependencies()
    }

    @Suppress("UNUSED_VARIABLE")
    @OptIn(ExperimentalComposeLibrary::class)
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        val compose = (this as ExtensionAware).extensions.getByName("compose") as ComposePlugin.Dependencies

        configureSourceSets {
            val commonMain by getting {
                kotlin.srcDirs("build/$GeneratedResourcesDir")

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

    private fun ComposeExtension.configureComposeMultiplatform(project: Project) {
        kotlinCompilerPlugin.set(project.catalogLib("compose-compiler").get().toString())

        kotlinCompilerPluginArgs.set(
            buildList {
                if (project.composePluginEnabled("katana.enableComposeCompilerMetrics")) {
                    add("metricsDestination=${project.composePluginDir("compose-metrics")}")
                }

                if (project.composePluginEnabled("katana.enableComposeCompilerReports")) {
                    add("reportsDestination=${project.composePluginDir("compose-reports")}")
                }
            },
        )
    }

    private fun Project.kspDependencies() {
        dependencies {
            addProvider("kspCommonMainMetadata", catalogBundle("ui-common-ksp"))
            addProvider("kspAndroid", catalogBundle("ui-android-ksp"))
            addProvider("kspIosArm64", catalogBundle(UI_IOS_KSP))
            addProvider("kspIosSimulatorArm64", catalogBundle(UI_IOS_KSP))
            addProvider("kspIosX64", catalogBundle(UI_IOS_KSP))
        }
    }

    private fun Project.generateResourcesTask() {
        val generateResourcesFile by tasks.registering(GenerateResourcesFileTask::class) {
            packageName.set(fullPackageName)
            inputFiles.from(layout.projectDirectory.dir(ResourcesDir).asFileTree)
            outputDir.set(layout.buildDirectory.dir(GeneratedResourcesDir))
        }

        tasks.named("preBuild").configure { dependsOn(generateResourcesFile) }
    }

    private fun Project.composePluginEnabled(property: String) =
        providers.gradleProperty(property).orNull == "true"

    private fun Project.composePluginDir(directory: String) =
        File(layout.buildDirectory.asFile.get(), directory).absolutePath

    private companion object {
        const val UI_IOS_KSP = "ui-ios-ksp"

        const val GeneratedResourcesDir = "generated/sources/katana/main"
    }
}
