package dev.alvr.katana.buildlogic.android

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.implementation
import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class KatanaAndroidApplicationPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "com.android.application")
        apply(plugin = "org.jetbrains.kotlin.android")
        apply(plugin = "org.jetbrains.compose")
        apply(plugin = "katana.sonar.mobile")
        apply(plugin = "katana.sentry")

        with(extensions) {
            commonExtensions()
            configure<BaseAppModuleExtension> { configureApp(project) }
        }

        tasks.commonTasks()

        dependencies { implementation(catalogBundle("app")) }
    }

    @Suppress("StringLiteralDuplication")
    private fun BaseAppModuleExtension.configureApp(project: Project) {
        configureAndroid(KatanaConfiguration.PackageName)

        val rootProject = project.rootProject

        signingConfigs {
            register("release") {
                val props = Properties().also { p ->
                    runCatching {
                        FileInputStream(rootProject.file("local.properties")).use { f ->
                            p.load(f)
                        }
                    }
                }

                enableV3Signing = true
                enableV4Signing = true

                keyAlias = props.getValue("signingAlias", "SIGNING_ALIAS")
                keyPassword = props.getValue("signingAliasPass", "SIGNING_ALIAS_PASS")
                storeFile = props.getValue("signingFile", "SIGNING_FILE")?.let {
                    rootProject.file(it)
                }
                storePassword = props.getValue("signingFilePass", "SIGNING_FILE_PASS")
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

    private fun ApplicationBuildType.configure(isDebug: Boolean) {
        isDebuggable = isDebug
        isDefault = isDebug
        isMinifyEnabled = !isDebug
        isShrinkResources = !isDebug
        enableUnitTestCoverage = isDebug
    }

    private fun Properties.getValue(key: String, env: String) =
        getOrElse(key) { System.getenv(env) } as? String
}
