package dev.alvr.katana.buildlogic.multiplatform

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogVersion
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.isRelease
import java.io.File
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.systemProperties
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun KotlinMultiplatformExtension.configureSourceSets(
    block: NamedDomainObjectContainer<KotlinSourceSet>.() -> Unit
) {
    configureExtension(block)
}

internal inline fun <reified T : Any> KotlinMultiplatformExtension.configureExtension(
    noinline block: T.() -> Unit
) {
    (this as ExtensionAware).extensions.configure(block)
}

internal fun BaseExtension.configureAndroid(project: Project) {
    buildFeatures.buildConfig = false
    namespace = project.fullPackageName

    compileSdkVersion(KatanaConfiguration.CompileSdk)
    buildToolsVersion(KatanaConfiguration.BuildTools)

    defaultConfig {
        minSdk = KatanaConfiguration.MinSdk
        targetSdk = KatanaConfiguration.TargetSdk

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
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
                test.enabled = !test.isRelease
                test.jvmArgs = listOf("-Xmx8G")
                test.systemProperties(
                    "robolectric.usePreinstrumentedJars" to "true",
                    "robolectric.logging.enabled" to "true",
                )
            }
        }
    }
}

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

internal fun ExternalModuleDependency.excludeKoinDeps() {
    exclude(group = "androidx.appcompat", module = "appcompat")
    exclude(group = "androidx.activity", module = "activity-ktx")
    exclude(group = "androidx.fragment", module = "fragment-ktx")
    exclude(group = "androidx.lifecycle", module = "lifecycle-common-java8")
}
