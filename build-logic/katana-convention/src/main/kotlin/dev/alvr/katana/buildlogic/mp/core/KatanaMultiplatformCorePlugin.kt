package dev.alvr.katana.buildlogic.mp.core

import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.configureCommonLanguageSettings
import dev.alvr.katana.buildlogic.mp.configureIos
import dev.alvr.katana.buildlogic.mp.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformCorePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "com.google.devtools.ksp")
        apply(plugin = "io.kotest.multiplatform")
        apply(plugin = "org.jetbrains.kotlinx.kover")
        apply(plugin = "dev.mokkery")

        with(extensions) {
            commonExtensions()
            configure<KotlinMultiplatformExtension> { configureMultiplatform(project) }
        }

        with(tasks) {
            commonTasks()
            withType<Test>().configureEach { useJUnitPlatform() }
        }
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform(project: Project) {
        applyDefaultHierarchyTemplate()
        jvm { testRuns["test"].executionTask.configure { useJUnitPlatform() } }
        configureIos()
        configureSourceSets()

        configureKotlin()
        kspDependencies(project, "core")
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain {
                configureCommonLanguageSettings()
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
