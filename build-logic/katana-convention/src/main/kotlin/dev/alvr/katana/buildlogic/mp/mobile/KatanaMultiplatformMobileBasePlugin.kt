@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.mp.mobile

import com.github.gmazzo.buildconfig.BuildConfigExtension
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.configureIos
import dev.alvr.katana.buildlogic.mp.configureKotlin
import dev.alvr.katana.buildlogic.mp.katanaBuildConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal abstract class KatanaMultiplatformMobileBasePlugin(
    private val androidPlugin: String,
) : Plugin<Project> {
    abstract fun ExtensionContainer.configureAndroid(project: Project)

    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = androidPlugin)
        apply(plugin = "com.github.gmazzo.buildconfig")
        apply(plugin = "com.google.devtools.ksp")
        apply(plugin = "io.kotest.multiplatform")
        apply(plugin = "org.jetbrains.kotlinx.kover")
        apply(plugin = "dev.mokkery")

        extensions.commonExtensions()
        extensions.configureAndroid(this)
        extensions.configure<BuildConfigExtension> { configureBuildConfig(project) }
        extensions.configure<KotlinMultiplatformExtension> { configureMultiplatform(project) }

        tasks.commonTasks()
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform(project: Project) {
        androidTarget()
        configureIos()
        configureSourceSets()

        applyDefaultHierarchyTemplate()

        configureKotlin()

        kspDependencies(project, "mobile")
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain.dependencies {
                implementation(catalogBundle("mobile-common"))
            }
            androidMain {
                dependsOn(commonMain.get())
                dependencies {
                    implementation(catalogBundle("mobile-android"))
                }
            }
            iosMain {
                dependsOn(commonMain.get())
                dependencies {
                    implementation(catalogBundle("mobile-ios"))
                }
            }

            commonTest {
                dependencies {
                    implementation(catalogBundle("mobile-common-test"))
                }
            }
            androidUnitTest {
                dependsOn(commonTest.get())
                dependencies {
                    implementation(catalogBundle("mobile-android-test"))
                }
            }
            iosTest {
                dependsOn(commonTest.get())
                dependencies {
                    implementation(catalogBundle("mobile-ios-test"))
                }
            }

            configureSourceSets()
        }
    }

    private fun BuildConfigExtension.configureBuildConfig(project: Project) {
        val pkgName = if (project.path == ANDROID_APP_PATH) {
            project.fullPackageName.substringBeforeLast('.')
        } else {
            project.fullPackageName
        }

        packageName = pkgName

        project.afterEvaluate {
            project.katanaBuildConfig(
                android = { androidConfig ->
                    sourceSets.getByName("androidMain") {
                        className = BUILD_CONFIG_FILE
                        androidConfig.forEach { config ->
                            buildConfigField(config.type, config.name, config.value)
                        }
                    }
                },
                ios = { iosConfig ->
                    sourceSets.getByName("iosMain") {
                        className = BUILD_CONFIG_FILE
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
        const val ANDROID_APP_PATH = ":app-android"
    }
}
