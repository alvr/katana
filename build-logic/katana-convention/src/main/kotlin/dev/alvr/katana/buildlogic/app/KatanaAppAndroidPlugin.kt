package dev.alvr.katana.buildlogic.app

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.feature.KatanaFeatureBasePlugin
import dev.alvr.katana.buildlogic.utils.ANDROID_APPLICATION_PLUGIN
import dev.alvr.katana.buildlogic.utils.AndroidDir
import dev.alvr.katana.buildlogic.utils.ResourcesDir
import dev.alvr.katana.buildlogic.utils.catalogBundle
import dev.alvr.katana.buildlogic.utils.configureAndroid
import io.sentry.android.gradle.extensions.SentryPluginExtension
import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal class KatanaAppAndroidPlugin : KatanaFeatureBasePlugin(ANDROID_APPLICATION_PLUGIN) {

    override fun apply(target: Project) = with(target) {
        super.apply(this)
        apply(plugin = "katana.feature.ui.compose")
        apply(plugin = "io.sentry.android.gradle")

        extensions.configure<SentryPluginExtension> { configureSentry() }
    }

    context(Project)
    override fun ExtensionContainer.configureAndroid() {
        configure<BaseAppModuleExtension> { configureApp() }
    }

    override fun NamedDomainObjectContainer<KotlinSourceSet>.configureSourceSets() {
        getByName("commonMain") {
            dependencies {
                implementation(catalogBundle("app-common"))
            }
        }
        getByName("androidMain") {
            dependencies {
                implementation(catalogBundle("app-android"))
            }
        }
        getByName("iosMain") {
            dependencies {
                implementation(catalogBundle("app-ios"))
            }
        }
    }

    context(Project)
    @Suppress("StringLiteralDuplication")
    private fun BaseAppModuleExtension.configureApp() {
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
        sourceSets["main"].res.srcDirs("$AndroidDir/res")
        sourceSets["main"].resources.srcDirs(ResourcesDir)
    }

    private fun SentryPluginExtension.configureSentry() {
        includeProguardMapping.set(true)
        autoUploadProguardMapping.set(true)
        dexguardEnabled.set(false)
        uploadNativeSymbols.set(false)
        includeNativeSources.set(false)
        tracingInstrumentation.enabled.set(false)
        autoInstallation.enabled.set(false)
        ignoredBuildTypes.set(setOf("debug"))
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
