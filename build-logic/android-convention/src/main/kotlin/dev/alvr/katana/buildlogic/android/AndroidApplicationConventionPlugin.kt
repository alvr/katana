package dev.alvr.katana.buildlogic.android

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.catalogLib
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.desugaring
import dev.alvr.katana.buildlogic.implementation
import dev.alvr.katana.buildlogic.kapt
import dev.alvr.katana.buildlogic.testImplementation
import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.getByType

internal class AndroidApplicationConventionPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "com.android.application")
        apply(plugin = "org.jetbrains.kotlin.android")
        apply(plugin = "org.jetbrains.kotlin.kapt")
        apply(plugin = "com.google.dagger.hilt.android")
        apply(plugin = "katana.sonarqube.android")

        with(extensions) {
            commonAndroidExtensions()
            getByType<BaseAppModuleExtension>().configureApp(project)
        }

        tasks.commonTasks()

        configurations.getByName("debugImplementation").exclude(group = "junit", module = "junit")

        dependencies {
            implementation(platform(catalogLib("compose-bom")))
            implementation(catalogBundle("common-android"))
            implementation(catalogBundle("app"))

            desugaring(catalogLib("desugaring"))

            kapt(catalogBundle("kapt-ui"))

            testImplementation(platform(catalogLib("compose-bom")))
            testImplementation(catalogBundle("test"))
            testImplementation(catalogBundle("test-android"))
            testImplementation(catalogBundle("test-ui"))
        }
    }

    @Suppress("StringLiteralDuplication", "UnstableApiUsage")
    private fun BaseAppModuleExtension.configureApp(project: Project) {
        val rootProject = project.rootProject

        baseAndroidConfig()
        project.configureCompose(this)

        namespace = KatanaConfiguration.PackageName

        defaultConfig {
            applicationId = KatanaConfiguration.PackageName
            versionCode = KatanaConfiguration.VersionCode
            versionName = KatanaConfiguration.VersionName
        }

        signingConfigs {
            create("release") {
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

        buildFeatures.buildConfig = true

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

            create("beta") {
                initWith(getByName("release"))
                matchingFallbacks.add("release")

                applicationIdSuffix = ".beta"
                versionNameSuffix = "-beta"
            }
        }

        lint {
            abortOnError = false
        }

        packagingOptions {
            resources {
                excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            }
        }
    }

    @Suppress("UnstableApiUsage")
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
