package dev.alvr.katana.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import dev.alvr.katana.buildlogic.mp.capitalize
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.androidJvm
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.common
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.native
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.util.visibleName

private val Project.libs get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val Test.isRelease get() = name.contains("""beta|release""".toRegex(RegexOption.IGNORE_CASE))

internal val Project.fullPackageName get() = KatanaConfiguration.PackageName + path.replace(':', '.')
internal fun Project.catalogLib(alias: String) = libs.findLibrary(alias).get()

private fun Project.optionalCatalogBundle(alias: String) = libs.findBundle(alias)
internal fun KotlinDependencyHandler.bundleImplementation(alias: String) {
    project.optionalCatalogBundle(alias).ifPresent { bundle -> implementation(bundle) }
}

internal fun KotlinDependencyHandler.implementation(
    dependencyNotation: Provider<*>,
    configure: ExternalModuleDependency.() -> Unit
) {
    implementation(dependencyNotation.get().toString(), configure)
}

internal fun DependencyHandlerScope.implementation(
    provider: Provider<*>,
    dependencyConfiguration: ExternalModuleDependency.() -> Unit = {},
) {
    "implementation"(provider, dependencyConfiguration)
}

internal fun DependencyHandlerScope.testImplementation(
    provider: Provider<*>,
    dependencyConfiguration: ExternalModuleDependency.() -> Unit = {},
) {
    "testImplementation"(provider, dependencyConfiguration)
}

internal fun DependencyHandlerScope.detekt(provider: Provider<*>) {
    "detektPlugins"(provider)
}

internal fun KotlinMultiplatformExtension.kspDependencies(project: Project, catalogPrefix: String) {
    project.dependencies {
        targets.forEach { target ->
            val configurationName = "ksp${target.configurationName()}"
            val catalogAlias = "$catalogPrefix-${target.groupName}-ksp".lowercase()

            project.optionalCatalogBundle(catalogAlias).ifPresent { bundle ->
                add(configurationName, bundle)
            }
        }
    }
}

internal fun ApplicationExtension.configureAndroid(packageName: String) {
    compileSdk { version = release(KatanaConfiguration.CompileSdk) }
    buildToolsVersion = KatanaConfiguration.BuildTools

    buildFeatures.buildConfig = false
    namespace = packageName

    defaultConfig {
        minSdk = KatanaConfiguration.MinSdk
        targetSdk = KatanaConfiguration.TargetSdk
        versionCode = KatanaConfiguration.VersionCode
        versionName = KatanaConfiguration.VersionName

        vectorDrawables.useSupportLibrary = true
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
            }
        }
    }
}

internal fun ExtensionContainer.commonExtensions() {
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(KatanaConfiguration.JvmTargetStr)
        }
    }

    configure<KotlinProjectExtension> {
        jvmToolchain {
            languageVersion = JavaLanguageVersion.of(KatanaConfiguration.JvmTargetStr)
        }
    }
}

internal fun TaskContainer.commonTasks() {
    withType<JavaCompile>().configureEach {
        sourceCompatibility = KatanaConfiguration.JvmTargetStr
        targetCompatibility = KatanaConfiguration.JvmTargetStr
    }
    withType<KotlinCompile>().configureEach {
        compilerOptions.configureKotlinCompiler()
    }
    withType<Test>().configureEach {
        useJUnitPlatform()
        failOnNoDiscoveredTests = false
    }
}

internal fun KotlinCommonCompilerOptions.configureKotlinCompiler() {
    if (this is KotlinJvmCompilerOptions) {
        jvmTarget = KatanaConfiguration.JvmTarget
    }
    apiVersion = KatanaConfiguration.KotlinVersion
    languageVersion = KatanaConfiguration.KotlinVersion
    freeCompilerArgs.addAll(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xexpect-actual-classes",
        "-Xconsistent-data-class-copy-visibility",
        "-Xcontext-parameters",
    )
}

private fun KotlinTarget.configurationName() =
    if (platformType == common) {
        "CommonMainMetadata"
    } else {
        targetName.capitalize()
    }

private val KotlinTarget.groupName get() = when (platformType) {
    native if targetName.contains(IosTarget) -> IosTarget
    androidJvm -> AndroidTarget
    else -> platformType.visibleName
}

private const val AndroidTarget = "android"
private const val IosTarget = "ios"
