package dev.alvr.katana.buildlogic.multiplatform

import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.configureAndroid
import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal class KatanaMultiplatformAppPlugin : KatanaMultiplatformMobileBasePlugin(AndroidPlugin) {
    override fun ExtensionContainer.configureAndroid(project: Project) {
        configure<BaseAppModuleExtension> { configureApp(project) }
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

    @Suppress("StringLiteralDuplication")
    private fun BaseAppModuleExtension.configureApp(project: Project) {
        configureAndroid(KatanaConfiguration.PackageName)
        val rootProject = project.rootProject

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

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
        sourceSets["main"].res.srcDirs("src/androidMain/res")
        sourceSets["main"].resources.srcDirs("src/commonMain/resources")
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

    private companion object {
        const val AndroidPlugin = "com.android.application"
    }
}
