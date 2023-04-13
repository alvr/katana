package dev.alvr.katana.buildlogic.android

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogVersion
import dev.alvr.katana.buildlogic.commonExtensions
import java.io.File
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.systemProperties
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun ExtensionContainer.commonAndroidExtensions() {
    commonExtensions()
}

internal fun BaseExtension.baseAndroidConfig() {
    compileSdkVersion(KatanaConfiguration.CompileSdk)
    buildToolsVersion(KatanaConfiguration.BuildTools)

    defaultConfig {
        minSdk = KatanaConfiguration.MinSdk
        targetSdk = KatanaConfiguration.TargetSdk

        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures.buildConfig = false
    compileOptions.isCoreLibraryDesugaringEnabled = true

    buildTypes {
        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            consumerProguardFile("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = KatanaConfiguration.UseJavaVersion
        targetCompatibility = KatanaConfiguration.UseJavaVersion
    }

    testOptions {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true
            all { test ->
                test.useJUnitPlatform()
                test.jvmArgs = listOf("-Xmx8G")
                test.systemProperties(
                    "robolectric.usePreinstrumentedJars" to "true",
                    "robolectric.logging.enabled" to "true",
                )
            }
        }
    }
}

@Suppress("UnstableApiUsage")
internal fun Project.configureCompose(commonExtension: CommonExtension<*, *, *, *>) {
    with(commonExtension) {
        buildFeatures.compose = true
        composeOptions.kotlinCompilerExtensionVersion = catalogVersion("compose-compiler")
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.set(freeCompilerArgs.get() + buildComposeParameters())
        }
    }
}

internal fun ExternalModuleDependency.excludeKoinDeps() {
    exclude(group = "androidx.appcompat", module = "appcompat")
    exclude(group = "androidx.activity", module = "activity-ktx")
    exclude(group = "androidx.fragment", module = "fragment-ktx")
    exclude(group = "androidx.lifecycle", module = "lifecycle-common-java8")
}

private fun Project.buildComposeParameters() = buildList {
    val pluginPrefix = "plugin:androidx.compose.compiler.plugins.kotlin"

    val enableMetricsProvider = providers.gradleProperty("enableComposeCompilerMetrics")
    if (enableMetricsProvider.orNull == "true") {
        val metricsFolder = File(buildDir, "compose-metrics")
        add("-P")
        add("$pluginPrefix:metricsDestination=" + metricsFolder.absolutePath)
    }

    val enableReportsProvider = providers.gradleProperty("enableComposeCompilerReports")
    if (enableReportsProvider.orNull == "true") {
        val reportsFolder = File(buildDir, "compose-reports")
        add("-P")
        add("$pluginPrefix:reportsDestination=" + reportsFolder.absolutePath)
    }
}
