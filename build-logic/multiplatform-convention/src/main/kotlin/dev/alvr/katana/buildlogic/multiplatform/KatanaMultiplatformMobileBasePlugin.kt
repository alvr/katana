package dev.alvr.katana.buildlogic.multiplatform

import com.android.build.gradle.internal.crash.afterEvaluate
import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.fullPackageName
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal abstract class KatanaMultiplatformMobileBasePlugin(
    private val androidPlugin: String,
) : ConventionPlugin {
    abstract fun ExtensionContainer.configureAndroid(project: Project)

    override fun Project.configure() {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = androidPlugin)
        apply(plugin = "com.github.gmazzo.buildconfig")
        apply(plugin = "com.google.devtools.ksp")
        apply(plugin = "io.kotest.multiplatform")
        apply(plugin = "org.jetbrains.kotlinx.kover")
        apply(plugin = "org.kodein.mock.mockmp")

        with(extensions) {
            create<KatanaMultiplatformMobileExtension>(KATANA_MULTIPLATFORM_EXTENSION)

            commonExtensions()
            configureAndroid(project)
            configure<BuildConfigExtension> { configureBuildConfig(project) }
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
        }

        tasks.commonTasks()
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        targetHierarchy.default()
        androidTarget()
        configureIos()
        configureSourceSets()
    }

    @Suppress("UNUSED_VARIABLE")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
            val commonMain by getting {
                kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

                dependencies {
                    implementation(catalogBundle("mobile-common"))
                }
            }
            val androidMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("mobile-android"))
                }
            }
            val iosMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("mobile-ios"))
                }
            }
            val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

            val commonTest by getting {
                dependencies {
                    implementation(catalogBundle("mobile-common-test"))
                }
            }
            val androidUnitTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("mobile-android-test"))
                }
            }
            val iosTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("mobile-ios-test"))
                }
            }
            val iosSimulatorArm64Test by getting { dependsOn(iosTest) }

            configureSourceSets()
        }
    }

    private fun BuildConfigExtension.configureBuildConfig(project: Project) {
        project.afterEvaluate {
            packageName.set(project.fullPackageName)

            project.katanaBuildConfig(
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
    }
}
