@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.app

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.configureKotlinCompiler
import dev.alvr.katana.buildlogic.implementation
import dev.alvr.katana.buildlogic.testImplementation
import io.sentry.android.gradle.extensions.SentryPluginExtension
import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal class KatanaAppAndroidPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "com.android.application")
        apply(plugin = "org.jetbrains.kotlin.android")
        apply(plugin = "org.jetbrains.compose")
        apply(plugin = "org.jetbrains.kotlin.plugin.compose")
        apply(plugin = "io.sentry.android.gradle")

        with(extensions) {
            configure<BaseAppModuleExtension> { configureApp(project) }
            configure<SentryPluginExtension> { configureSentry() }
            configure<KotlinAndroidProjectExtension> { configureKotlin() }
        }

        dependencies {
            implementation(catalogBundle("app-android"))
            implementation(catalogBundle("mobile-common"))
            implementation(catalogBundle("mobile-android"))

            testImplementation(catalogBundle("mobile-common-test"))
            testImplementation(catalogBundle("mobile-android-test"))
        }

        tasks.commonTasks()
    }

    private fun KotlinAndroidProjectExtension.configureKotlin() {
        compilerOptions.configureKotlinCompiler()
    }

    @Suppress("StringLiteralDuplication")
    private fun BaseAppModuleExtension.configureApp(project: Project) {
        configureAndroid(KatanaConfiguration.PackageName)

        defaultConfig.applicationId = KatanaConfiguration.PackageName
        lint.abortOnError = false

        with(packagingOptions.resources.excludes) {
            add("/META-INF/{AL2.0,LGPL2.1}")
            add("DebugProbesKt.bin")
        }

        signingConfigs {
            register("release") {
                val props = Properties().also { p ->
                    runCatching {
                        FileInputStream(project.rootProject.file("local.properties")).use { f ->
                            p.load(f)
                        }
                    }
                }

                enableV3Signing = true
                enableV4Signing = true

                keyAlias = props["signingAlias", "SIGNING_ALIAS"]
                keyPassword = props["signingAliasPass", "SIGNING_ALIAS_PASS"]
                storeFile = props["signingFile", "SIGNING_FILE"]?.let {
                    project.rootProject.file(it)
                }
                storePassword = props["signingFilePass", "SIGNING_FILE_PASS"]
            }
        }

        buildTypes {
            debug {
                applicationIdSuffix = ".dev"
                versionNameSuffix = "-dev"

                configure(isDebug = true)
            }

            release {
                configure(isDebug = false)

                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )

                signingConfig = signingConfigs.getByName("release")
            }

            register("beta") {
                initWith(getByName("release"))
                matchingFallbacks.add("release")

                applicationIdSuffix = ".beta"
                versionNameSuffix = "-beta"
            }
        }
    }

    private fun SentryPluginExtension.configureSentry() {
        includeProguardMapping = true
        autoUploadProguardMapping = true
        dexguardEnabled = false
        uploadNativeSymbols = false
        includeNativeSources = false
        tracingInstrumentation.enabled = false
        autoInstallation.enabled = false
        ignoredBuildTypes = setOf("debug")
    }

    private fun ApplicationBuildType.configure(isDebug: Boolean) {
        isDebuggable = isDebug
        isDefault = isDebug
        isMinifyEnabled = !isDebug
        isShrinkResources = !isDebug
        enableUnitTestCoverage = isDebug
    }

    private operator fun Properties.get(key: String, env: String) =
        getOrElse(key) { System.getenv(env) } as? String
}
