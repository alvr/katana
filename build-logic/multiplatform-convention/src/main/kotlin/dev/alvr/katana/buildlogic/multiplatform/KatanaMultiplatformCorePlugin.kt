package dev.alvr.katana.buildlogic.multiplatform

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformCorePlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "org.jetbrains.kotlin.native.cocoapods")
        apply(plugin = "katana.sonar.kotlin")
        apply(plugin = "com.google.devtools.ksp")
        apply(plugin = "io.kotest.multiplatform")
        apply(plugin = "org.kodein.mock.mockmp")

        with(extensions) {
            create<KatanaMultiplatformCoreExtension>(KATANA_MULTIPLATFORM_EXTENSION)

            commonExtensions()
            configure<KotlinMultiplatformExtension> { configure(project) }
        }

        with(tasks) {
            commonTasks()
            withType<Test>().configureEach { useJUnitPlatform() }
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    private fun KotlinMultiplatformExtension.configure(project: Project) {
        targetHierarchy.default()

        jvm {
            testRuns["test"].executionTask.configure { useJUnitPlatform() }
        }
        ios()
        iosSimulatorArm64()

        configureCocoapods(project)
        configureSourceSets()
    }

    @Suppress("UNUSED_VARIABLE")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(catalogBundle("core-common"))
                }
            }
            val jvmMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("core-jvm"))
                }
            }
            val iosMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("core-ios"))
                }
            }
            val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

            val commonTest by getting {
                dependencies {
                    implementation(catalogBundle("core-common-test"))
                }
            }
            val jvmTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("core-jvm-test"))
                }
            }
            val iosTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("core-ios-test"))
                }
            }
            val iosSimulatorArm64Test by getting { dependsOn(iosTest) }
        }
    }
}
