package dev.alvr.katana.buildlogic.feature

import com.github.gmazzo.buildconfig.BuildConfigExtension
import dev.alvr.katana.buildlogic.utils.androidUnitTest
import dev.alvr.katana.buildlogic.utils.catalogBundle
import dev.alvr.katana.buildlogic.utils.commonExtensions
import dev.alvr.katana.buildlogic.utils.commonTasks
import dev.alvr.katana.buildlogic.utils.configureIos
import dev.alvr.katana.buildlogic.utils.configureKotlin
import dev.alvr.katana.buildlogic.utils.fullPackageName
import dev.alvr.katana.buildlogic.utils.katanaBuildConfig
import dev.alvr.katana.buildlogic.utils.kspDependencies
import dev.alvr.katana.buildlogic.utils.sourceSets
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.kodein.mock.gradle.MocKMPGradlePlugin

internal abstract class KatanaFeatureBasePlugin(
    private val androidPlugin: String,
) : Plugin<Project> {
    context(Project)
    abstract fun ExtensionContainer.configureAndroid()

    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = androidPlugin)
        apply(plugin = "com.github.gmazzo.buildconfig")
        apply(plugin = "com.google.devtools.ksp")
        apply(plugin = "io.kotest.multiplatform")
        apply(plugin = "org.jetbrains.kotlinx.kover")
        apply(plugin = "org.kodein.mock.mockmp")

        with(extensions) {
            commonExtensions()
            configureAndroid()
            configure<BuildConfigExtension> { configureBuildConfig() }
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
            configure<MocKMPGradlePlugin.Extension> { installWorkaround() }
        }

        tasks.commonTasks()
    }

    context(Project)
    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        applyDefaultHierarchyTemplate()
        androidTarget()
        configureIos()
        configureSourceSets()

        configureKotlin()
        kspDependencies("mobile")
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain.dependencies {
                implementation(catalogBundle("mobile-common"))
            }
            androidMain.dependencies {
                implementation(catalogBundle("mobile-android"))
            }
            iosMain.dependencies {
                implementation(catalogBundle("mobile-ios"))
            }

            commonTest.dependencies {
                implementation(catalogBundle("mobile-common-test"))
            }
            androidUnitTest.dependencies {
                implementation(catalogBundle("mobile-android-test"))
            }
            iosTest.dependencies {
                implementation(catalogBundle("mobile-ios-test"))
            }

            configureSourceSets()
        }
    }

    context(Project)
    private fun BuildConfigExtension.configureBuildConfig() {
        packageName.set(fullPackageName.removeSuffix(APP_PACKAGE))

        afterEvaluate {
            katanaBuildConfig(
                android = { androidConfig ->
                    sourceSets.getByName("androidMain") {
                        className.set(BUILD_CONFIG_FILE)
                        androidConfig.forEach { config ->
                            buildConfigField(config.type, config.name, config.value)
                        }
                    }
                },
                ios = { iosConfig ->
                    sourceSets.getByName("iosMain") {
                        className.set(BUILD_CONFIG_FILE)
                        iosConfig.forEach { config ->
                            buildConfigField(config.type, config.name, config.value)
                        }
                    }
                },
            )
        }
    }

    protected open fun NamedDomainObjectContainer<KotlinSourceSet>.configureSourceSets() {}

    private companion object {
        const val BUILD_CONFIG_FILE = "KatanaBuildConfig"
        const val APP_PACKAGE = ".app"
    }
}
