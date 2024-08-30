@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.mp.app

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import dev.alvr.katana.buildlogic.ANDROID_APPLICATION_PLUGIN
import dev.alvr.katana.buildlogic.AndroidDir
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.configureAndroid
import dev.alvr.katana.buildlogic.mp.mobile.KatanaMultiplatformMobileBasePlugin
import io.sentry.android.gradle.extensions.SentryPluginExtension
import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal class KatanaMultiplatformAppPlugin : KatanaMultiplatformMobileBasePlugin(ANDROID_APPLICATION_PLUGIN) {

    override fun apply(target: Project) = with(target) {
        super.apply(this)
        apply(plugin = "katana.multiplatform.compose")
        apply(plugin = "io.sentry.android.gradle")

        with(extensions) {
            configure<SentryPluginExtension> { configureSentry() }
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
        }
    }

    override fun ExtensionContainer.configureAndroid(project: Project) {
        configure<BaseAppModuleExtension> { configureApp(project) }
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        configureSourceSets()
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain.dependencies {
                implementation(catalogBundle("app-common"))
            }
            androidMain {
                dependencies {
                    implementation(catalogBundle("app-android"))
                }
            }
            iosMain {
                dependencies {
                    implementation(catalogBundle("app-ios"))
                }
            }
        }
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
                    "$AndroidDir/proguard-rules.pro",
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

        sourceSets["main"].manifest.srcFile("$AndroidDir/AndroidManifest.xml")
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
