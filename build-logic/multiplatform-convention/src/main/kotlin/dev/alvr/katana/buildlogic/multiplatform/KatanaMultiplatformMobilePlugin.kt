package dev.alvr.katana.buildlogic.multiplatform

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.fullPackageName
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformMobilePlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "com.android.library")
        apply(plugin = "katana.sonar.mobile")

        with(extensions) {
            create<KatanaMultiplatformMobileExtension>(KATANA_MULTIPLATFORM_EXTENSION)

            commonExtensions()
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
            getByType<LibraryExtension>().configureAndroid(project.fullPackageName)
        }

        tasks.commonTasks()
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        android()
        ios()
        iosSimulatorArm64()

        configureSourceSets()
    }

    @Suppress("UNUSED_VARIABLE")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(catalogBundle("mobile-common"))
                }
            }
            val androidMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("mobile-android"))
                }
            }
            val iosMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("mobile-ios"))
                }
            }
            val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

            val commonTest by getting {
                dependencies {
                    implementation(catalogBundle("mobile-common-test"))
                }
            }
            val androidUnitTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("mobile-android-test"))
                }
            }
            val iosTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("mobile-ios-test"))
                }
            }
            val iosSimulatorArm64Test by getting { dependsOn(iosTest) }
        }
    }
}
