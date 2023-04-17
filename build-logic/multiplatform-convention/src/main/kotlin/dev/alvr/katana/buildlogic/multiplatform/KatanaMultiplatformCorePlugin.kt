package dev.alvr.katana.buildlogic.multiplatform

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformCorePlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")

        with(extensions) {
            create<KatanaMultiplatformCoreExtension>(KATANA_MULTIPLATFORM_EXTENSION)

            commonExtensions()
            configure<KotlinMultiplatformExtension> { configure() }
        }

        with(tasks) {
            commonTasks()
            withType<Test>().configureEach { useJUnitPlatform() }
        }
    }

    private fun KotlinMultiplatformExtension.configure() {
        jvm()
        ios()
        iosSimulatorArm64()

        configureSourceSets()
    }

    @Suppress("UNUSED_VARIABLE")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(catalogBundle("common"))
                }
            }
            val jvmMain by getting { dependsOn(commonMain) }
            val iosMain by getting { dependsOn(commonMain) }
            val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

            val commonTest by getting {
                dependencies {
                    implementation(catalogBundle("test"))
                }
            }
            val jvmTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("test-jvm"))
                }
            }
            val iosTest by getting { dependsOn(commonTest) }
            val iosSimulatorArm64Test by getting { dependsOn(iosTest) }
        }
    }
}
