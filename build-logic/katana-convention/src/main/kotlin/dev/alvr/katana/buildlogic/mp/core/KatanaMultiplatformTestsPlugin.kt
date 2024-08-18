package dev.alvr.katana.buildlogic.mp.core

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.mp.configureCommonLanguageSettings
import dev.alvr.katana.buildlogic.mp.configureIos
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformTestsPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "com.android.library")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
            configure<LibraryExtension> { configureAndroid(project.fullPackageName) }
        }

        tasks.commonTasks()
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        androidTarget()
        jvm { testRuns["test"].executionTask.configure { enabled = false } }
        configureIos()

        applyDefaultHierarchyTemplate {
            common {
                group("jvmBased") {
                    withJvm()
                    withAndroidTarget()
                }
            }
        }

        configureSourceSets()
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain {
                configureCommonLanguageSettings()
                dependencies {
                    implementation(catalogBundle("core-common-test"))
                    implementation(catalogBundle("mobile-common-test"))
                }
            }
            getByName("jvmBasedMain").dependencies {
                implementation(catalogBundle("core-jvm-test"))
                implementation(catalogBundle("mobile-android-test"))
                implementation(catalogBundle("ui-android-test"))
            }
            iosMain.dependencies {
                implementation(catalogBundle("mobile-ios-test"))
                implementation(catalogBundle("ui-ios-test"))
            }
        }
    }
}
