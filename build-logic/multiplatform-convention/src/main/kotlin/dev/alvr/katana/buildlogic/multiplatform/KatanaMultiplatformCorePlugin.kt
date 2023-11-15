package dev.alvr.katana.buildlogic.multiplatform

import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.kodein.mock.gradle.MocKMPGradlePlugin

internal class KatanaMultiplatformCorePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "com.google.devtools.ksp")
        apply(plugin = "io.kotest.multiplatform")
        apply(plugin = "org.jetbrains.kotlinx.kover")
        apply(plugin = "org.kodein.mock.mockmp")

        with(extensions) {
            create<KatanaMultiplatformCoreExtension>(KATANA_MULTIPLATFORM_EXTENSION)

            commonExtensions()
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
            configure<MocKMPGradlePlugin.Extension> { installWorkaround() }
        }

        with(tasks) {
            commonTasks()
            withType<Test>().configureEach { useJUnitPlatform() }
        }
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        applyDefaultHierarchyTemplate()
        jvm { testRuns["test"].executionTask.configure { useJUnitPlatform() } }
        configureIos()
        configureSourceSets()
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
            commonMain {
                kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

                dependencies {
                    implementation(catalogBundle("core-common"))
                }
            }
            jvmMain.dependencies {
                implementation(catalogBundle("core-jvm"))
            }
            iosMain.dependencies {
                implementation(catalogBundle("core-ios"))
            }

            commonTest.dependencies {
                implementation(catalogBundle("core-common-test"))
            }
            jvmTest.dependencies {
                implementation(catalogBundle("core-jvm-test"))
            }
            iosTest.dependencies {
                implementation(catalogBundle("core-ios-test"))
            }
        }
    }
}
