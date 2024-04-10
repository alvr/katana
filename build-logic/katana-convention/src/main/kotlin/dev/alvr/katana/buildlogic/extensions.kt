@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic

import com.android.build.gradle.BaseExtension
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
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
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
internal fun Project.catalogVersion(alias: String) = libs.findVersion(alias).get().toString()
internal fun Project.catalogLib(alias: String) = libs.findLibrary(alias).get()
internal fun Project.catalogBundle(alias: String) = libs.findBundle(alias).get()

internal fun KotlinDependencyHandler.catalogVersion(alias: String) = project.catalogVersion(alias)
internal fun KotlinDependencyHandler.catalogLib(alias: String) = project.catalogLib(alias)
internal fun KotlinDependencyHandler.catalogBundle(alias: String) = project.catalogBundle(alias)

internal fun KotlinDependencyHandler.implementation(
    dependencyNotation: Provider<*>,
    configure: ExternalModuleDependency.() -> Unit
) {
    implementation(dependencyNotation.get().toString(), configure)
}

@Suppress("UnstableApiUsage")
internal fun DependencyHandlerScope.implementation(
    provider: Provider<*>,
    dependencyConfiguration: ExternalModuleDependency.() -> Unit = {},
) {
    "implementation"(provider, dependencyConfiguration)
}

internal fun DependencyHandlerScope.detekt(provider: Provider<*>) {
    "detektPlugins"(provider)
}

context(KotlinMultiplatformExtension)
internal fun Project.kspDependencies(catalogPrefix: String) {
    kspDependencies("", catalogPrefix)
    kspDependencies("Test", catalogPrefix)
}

internal fun BaseExtension.configureAndroid(packageName: String) {
    compileSdkVersion(KatanaConfiguration.CompileSdk)
    buildToolsVersion(KatanaConfiguration.BuildTools)

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

    with(sourceSets["main"]) {
        res.srcDirs("$AndroidDir/res", ResourcesDir)
        resources.srcDirs(ResourcesDir)
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
            vendor = JvmVendorSpec.AZUL
        }
    }

    configure<KotlinProjectExtension> {
        jvmToolchain {
            languageVersion = JavaLanguageVersion.of(KatanaConfiguration.JvmTargetStr)
            vendor = JvmVendorSpec.AZUL
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
}

internal fun KotlinCommonCompilerOptions.configureKotlinCompiler() {
    if (this is KotlinJvmCompilerOptions) {
        jvmTarget = KatanaConfiguration.JvmTarget
    }
    apiVersion = KatanaConfiguration.KotlinVersion
    languageVersion = KatanaConfiguration.KotlinVersion
    freeCompilerArgs.addAll(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xcontext-receivers",
        "-Xlambdas=indy",
        "-Xexpect-actual-classes",
    )
}

context(KotlinMultiplatformExtension)
private fun Project.kspDependencies(
    configurationNameSuffix: String,
    catalogPrefix: String,
) {
    dependencies {
        targets.forEach { target ->
            val configurationName = "ksp${target.configurationName(configurationNameSuffix)}"
            val groupName = "${target.groupName}${configurationNameSuffix.suffix}"
            val catalogAlias = "$catalogPrefix-$groupName-ksp".lowercase()

            add(configurationName, catalogBundle(catalogAlias))
        }
    }
}

private fun KotlinTarget.configurationName(suffix: String) =
    if (platformType == common) {
        "CommonMainMetadata"
    } else {
        "${targetName.capitalize()}$suffix"
    }

private val String.suffix get() = if (isNotEmpty()) "-$this" else ""

private val KotlinTarget.groupName get() = when {
    platformType == native && targetName.contains(IOS_TARGET) -> IOS_TARGET
    platformType == androidJvm -> ANDROID_TARGET
    else -> platformType.visibleName
}

private const val ANDROID_TARGET = "android"
private const val IOS_TARGET = "ios"
