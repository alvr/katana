package dev.alvr.katana.buildlogic.mp

import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.configureKotlinCompiler
import java.util.Locale
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal fun KotlinMultiplatformExtension.hierarchy(
    configureAndroid: KotlinAndroidTarget.() -> Unit = { },
    configureIos: KotlinNativeTarget.() -> Unit = { },
) {
    applyDefaultHierarchyTemplate()

    configureAndroid(configureAndroid)
    configureIos(configureIos)

    configureKotlin()
}

private fun KotlinMultiplatformExtension.configureAndroid(
    configure: KotlinAndroidTarget.() -> Unit,
) {
    androidTarget {
        configure()
    }
}

private fun KotlinMultiplatformExtension.configureIos(
    configure: KotlinNativeTarget.() -> Unit,
) {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { ios ->
        ios.configure()
        compilerOptions.configureKotlinCompiler()
        ios.binaries.framework {
            baseName = project.frameworkIdentifier
            isStatic = true
        }
    }
}

private fun KotlinMultiplatformExtension.configureKotlin() {
    jvmToolchain(KatanaConfiguration.JvmTargetStr.toInt())
    sourceSets.commonMain { configureCommonLanguageSettings() }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().configureKotlinCompiler()
        }
    }
}

private fun KotlinSourceSet.configureCommonLanguageSettings() {
    languageSettings {
        apiVersion = KatanaConfiguration.KotlinVersion.version
        languageVersion = KatanaConfiguration.KotlinVersion.version
        progressiveMode = true
    }
}

private fun KotlinCompilationTask<*>.configureKotlinCompiler() {
    compilerOptions.configureKotlinCompiler()
}

private val Project.frameworkIdentifier
    get() = path.split(':')
        .joinToString(separator = "", prefix = "Katana") { it.capitalize() }

internal fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
