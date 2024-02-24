package dev.alvr.katana.buildlogic.core

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.utils.catalogBundle
import dev.alvr.katana.buildlogic.utils.commonTasks
import dev.alvr.katana.buildlogic.utils.configureAndroid
import dev.alvr.katana.buildlogic.utils.configureIos
import dev.alvr.katana.buildlogic.utils.fullPackageName
import dev.alvr.katana.buildlogic.utils.sourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaTestPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "com.android.library")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
            configure<LibraryExtension> { configureAndroid(project.fullPackageName) }
        }

        tasks.commonTasks()
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        applyDefaultHierarchyTemplate()
        androidTarget()
        jvm { testRuns["test"].executionTask.configure { enabled = false } }
        configureIos()
        configureSourceSets()
    }

    @Suppress("UnusedPrivateProperty")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(catalogBundle("core-common-test"))
                    implementation(catalogBundle("mobile-common-test"))
                }
            }
            val jvmMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("core-jvm-test"))
                }
            }
            val androidMain by getting {
                dependsOn(jvmMain)
                dependencies {
                    implementation(catalogBundle("mobile-android-test"))
                    implementation(catalogBundle("ui-android-test"))
                }
            }
            val iosMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("mobile-ios-test"))
                    implementation(catalogBundle("ui-ios-test"))
                }
            }
        }
    }
}
