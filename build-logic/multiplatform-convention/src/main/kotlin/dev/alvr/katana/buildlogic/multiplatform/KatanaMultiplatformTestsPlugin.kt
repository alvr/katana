package dev.alvr.katana.buildlogic.multiplatform

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.fullPackageName
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformTestsPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "com.android.library")

        with(extensions) {
            create<KatanaMultiplatformMobileExtension>(KATANA_MULTIPLATFORM_EXTENSION)

            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
            configure<LibraryExtension> { configureAndroid(project.fullPackageName) }
        }

        tasks.commonTasks()
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        targetHierarchy.default()

        android()
        jvm { testRuns["test"].executionTask.configure { enabled = false } }
        ios()
        iosSimulatorArm64()

        configureSourceSets()
    }

    @Suppress("UNUSED_VARIABLE")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
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
            val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
        }
    }
}
