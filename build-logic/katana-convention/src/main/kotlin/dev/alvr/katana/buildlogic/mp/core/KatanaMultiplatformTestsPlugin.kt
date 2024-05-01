package dev.alvr.katana.buildlogic.mp.core

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.mp.KATANA_MULTIPLATFORM_EXTENSION
import dev.alvr.katana.buildlogic.mp.configureIos
import dev.alvr.katana.buildlogic.mp.configureSourceSets
import dev.alvr.katana.buildlogic.mp.mobile.KatanaMultiplatformMobileExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformTestsPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "com.android.library")

        with(extensions) {
            create<KatanaMultiplatformMobileExtension>(KATANA_MULTIPLATFORM_EXTENSION)

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

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
            commonMain {
                dependencies {
                    implementation(catalogBundle("core-common-test"))
                    implementation(catalogBundle("mobile-common-test"))
                }
            }
            jvmMain {
                dependsOn(commonMain.get())
                dependencies {
                    implementation(catalogBundle("core-jvm-test"))
                }
            }
            androidMain {
                dependsOn(jvmMain.get())
                dependencies {
                    implementation(catalogBundle("mobile-android-test"))
                    implementation(catalogBundle("ui-android-test"))
                }
            }
            iosMain {
                dependsOn(commonMain.get())
                dependencies {
                    implementation(catalogBundle("mobile-ios-test"))
                    implementation(catalogBundle("ui-ios-test"))
                }
            }
        }
    }
}
