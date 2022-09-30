package dev.alvr.katana.buildlogic.android

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import dagger.hilt.android.plugin.HiltExtension
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogVersion
import dev.alvr.katana.buildlogic.commonExtensions
import java.io.File
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.systemProperties
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun ExtensionContainer.commonAndroidExtensions() {
    commonExtensions()

    configure<HiltExtension> {
        enableAggregatingTask = true
    }
}

@Suppress("UnstableApiUsage")
internal fun BaseExtension.baseAndroidConfig() {
    compileSdkVersion(KatanaConfiguration.CompileSdk)
    buildToolsVersion(KatanaConfiguration.BuildTools)

    defaultConfig {
        minSdk = KatanaConfiguration.MinSdk
        targetSdk = KatanaConfiguration.TargetSdk

        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures.buildConfig = false

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
                test.jvmArgs = listOf("-noverify", "-Xmx8G")
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
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = catalogVersion("compose-compiler")
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + buildComposeParameters()
        }
    }
}

private fun Project.buildComposeParameters(): List<String> {
    val parameters = mutableListOf<String>()

    val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
    if (enableMetricsProvider.orNull == "true") {
        val metricsFolder = File(project.buildDir, "compose-metrics")
        parameters.add("-P")
        parameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath,
        )
    }

    val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
    if (enableReportsProvider.orNull == "true") {
        val reportsFolder = File(project.buildDir, "compose-reports")
        parameters.add("-P")
        parameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath,
        )
    }
    return parameters
}