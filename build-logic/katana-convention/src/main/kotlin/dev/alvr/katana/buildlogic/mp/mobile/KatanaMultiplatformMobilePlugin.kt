package dev.alvr.katana.buildlogic.mp.mobile

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.configureCommonLanguageSettings
import dev.alvr.katana.buildlogic.mp.configureDesktop
import dev.alvr.katana.buildlogic.mp.configureIos
import dev.alvr.katana.buildlogic.mp.configureKotlin
import dev.alvr.katana.buildlogic.mp.desktopMain
import dev.alvr.katana.buildlogic.mp.desktopTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformMobilePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "com.android.library")
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "com.google.devtools.ksp")
        apply(plugin = "io.kotest.multiplatform")
        apply(plugin = "org.jetbrains.kotlinx.kover")
        apply(plugin = "dev.mokkery")
        apply(plugin = "io.sentry.kotlin.multiplatform.gradle")

        with(extensions) {
            commonExtensions()
            configure<LibraryExtension> { configureAndroid(project.fullPackageName) }
            configure<KotlinMultiplatformExtension> { configureMultiplatform(project) }
        }

        tasks.commonTasks()
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform(project: Project) {
        androidTarget()
        configureDesktop()
        configureIos()
        configureSourceSets()

        applyDefaultHierarchyTemplate()

        configureKotlin()

        kspDependencies(project, "mobile")
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain {
                configureCommonLanguageSettings()
                dependencies {
                    implementation(catalogBundle("mobile-common"))
                }
            }
            androidMain.dependencies {
                implementation(catalogBundle("mobile-android"))
            }
            iosMain.dependencies {
                implementation(catalogBundle("mobile-ios"))
            }
            desktopMain.dependencies {
                implementation(catalogBundle("mobile-jvm"))
            }

            commonTest.dependencies {
                implementation(catalogBundle("mobile-common-test"))
            }
            androidUnitTest.dependencies {
                implementation(catalogBundle("mobile-android-test"))
            }
            iosTest.dependencies {
                implementation(catalogBundle("mobile-ios-test"))
            }
            desktopTest.dependencies {
                implementation(catalogBundle("mobile-jvm-test"))
            }
        }
    }
}
