package dev.alvr.katana.buildlogic.mp

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import dev.alvr.katana.buildlogic.bundleImplementation
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.configure
import dev.alvr.katana.buildlogic.kspDependencies
import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

internal fun Project.commonConfiguration(
    configureAndroid: KotlinMultiplatformAndroidLibraryTarget.() -> Unit = {},
    configureIos: KotlinNativeTarget.() -> Unit = {},
) {
    apply(plugin = "com.android.kotlin.multiplatform.library")
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    apply(plugin = "com.google.devtools.ksp")
    apply(plugin = "dev.zacsweers.metro")
    apply(plugin = "io.kotest")
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "dev.mokkery")

    with(extensions) {
        commonExtensions()
        configure<KotlinMultiplatformExtension> {
            configureMultiplatform(configureAndroid = configureAndroid, configureIos = configureIos)
        }
        configure<MetroPluginExtension> { configure() }
    }

    tasks.commonTasks()
}

context(project: Project)
private fun KotlinMultiplatformExtension.configureMultiplatform(
    configureAndroid: KotlinMultiplatformAndroidLibraryTarget.() -> Unit = {},
    configureIos: KotlinNativeTarget.() -> Unit = {},
) {
    hierarchy(configureAndroid = configureAndroid, configureIos = configureIos)
    configureSourceSets()

    kspDependencies("core")
}

private fun KotlinMultiplatformExtension.configureSourceSets() {
    sourceSets {
        commonMain.dependencies { bundleImplementation("core-common") }
        androidMain.dependencies { bundleImplementation("core-android") }
        iosMain.dependencies { bundleImplementation("core-ios") }

        commonTest.dependencies { bundleImplementation("core-common-test") }
        androidHostTest.dependencies { bundleImplementation("core-android-test") }
        iosTest.dependencies { bundleImplementation("core-ios-test") }
    }
}
