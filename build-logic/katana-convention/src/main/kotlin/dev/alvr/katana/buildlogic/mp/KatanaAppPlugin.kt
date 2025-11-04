@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.mp

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.bundleImplementation
import dev.alvr.katana.buildlogic.configureAndroid
import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaAppPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "com.android.application")

        commonConfiguration()

        apply(plugin = "katana.multiplatform.compose")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
            configure<BaseAppModuleExtension> { configureAndroid(project) }
        }
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        configureSourceSets()
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            androidMain.dependencies {
                bundleImplementation("app-android")
            }
            iosMain.dependencies {
                bundleImplementation("app-ios")
            }
        }
    }

    @Suppress("StringLiteralDuplication")
    private fun BaseAppModuleExtension.configureAndroid(project: Project) {
        fun ApplicationBuildType.configure(isDebug: Boolean) {
            isDebuggable = isDebug
            isDefault = isDebug
            isMinifyEnabled = !isDebug
            isShrinkResources = !isDebug
            enableUnitTestCoverage = isDebug
        }

        configureAndroid(KatanaConfiguration.PackageName)

        compileOptions.isCoreLibraryDesugaringEnabled = true
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
                resValue("string", "app_name", "Katana Dev")
            }

            release {
                configure(isDebug = false)

                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "$AndroidDir/proguard-rules.pro",
                )

                signingConfig = signingConfigs.getByName("release")
                resValue("string", "app_name", "Katana")
            }

            register("beta") {
                initWith(getByName("release"))
                matchingFallbacks.add("release")

                applicationIdSuffix = ".beta"
                versionNameSuffix = "-beta"
                resValue("string", "app_name", "Katana Beta")
            }
        }

        sourceSets["main"].manifest.srcFile("$AndroidDir/AndroidManifest.xml")
        sourceSets["main"].res.srcDirs("$AndroidDir/res")
    }

    private operator fun Properties.get(key: String, env: String) =
        getOrElse(key) { System.getenv(env) } as? String
}

private const val AndroidDir = "src/androidMain"
