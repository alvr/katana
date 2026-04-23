package dev.alvr.katana.buildlogic.mp

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogLib
import dev.alvr.katana.buildlogic.configureKotlinCompiler
import dev.alvr.katana.buildlogic.fullPackageName
import java.util.Locale
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSourceSetConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCacheApi
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal fun KotlinMultiplatformExtension.hierarchy(
    configureAndroid: KotlinMultiplatformAndroidLibraryTarget.() -> Unit = {},
    configureIos: KotlinNativeTarget.() -> Unit = {},
) {
    applyDefaultHierarchyTemplate()

    configureAndroid(configureAndroid)
    configureIos(configureIos)

    configureKotlin()
}

private fun KotlinMultiplatformExtension.configureAndroid(
    configure: KotlinMultiplatformAndroidLibraryTarget.() -> Unit
) {
    androidLibrary {
        buildToolsVersion = KatanaConfiguration.BuildTools
        compileSdk {
            version = release(KatanaConfiguration.CompileSdk) { minorApiLevel = KatanaConfiguration.CompileSdkMinor }
        }
        minSdk = KatanaConfiguration.MinSdk
        namespace = project.fullPackageName

        androidResources.enable = false
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true

        enableCoreLibraryDesugaring = true
        project.dependencies.add("coreLibraryDesugaring", project.catalogLib("desugaring"))

        withHostTest { isIncludeAndroidResources = true }

        compilerOptions.configureKotlinCompiler()

        configure()
    }
}

@OptIn(KotlinNativeCacheApi::class)
private fun KotlinMultiplatformExtension.configureIos(configure: KotlinNativeTarget.() -> Unit) {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { ios ->
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

    targets.configureEach { compilations.all { compileTaskProvider.configure { configureKotlinCompiler() } } }
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
    get() = path.split(':').joinToString(separator = "", prefix = "Katana") { it.capitalize() }

internal fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

private fun KotlinMultiplatformExtension.androidLibrary(action: Action<KotlinMultiplatformAndroidLibraryTarget>) {
    (this as ExtensionAware).extensions.configure("androidLibrary", action)
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
internal val NamedDomainObjectContainer<KotlinSourceSet>.androidHostTest: NamedDomainObjectProvider<KotlinSourceSet> by
    KotlinSourceSetConvention
