package dev.alvr.katana.buildlogic.mp.mobile.data

import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.kspDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformDataPreferencesPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "katana.multiplatform.mobile")
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform(project) }
        }
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform(project: Project) {
        configureSourceSets()
        kspDependencies(project, "mobile")
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain.dependencies {
                implementation(catalogBundle("data-preferences-common"))
            }
            androidMain.dependencies {
                implementation(catalogBundle("data-preferences-android"))
            }
            iosMain.dependencies {
                implementation(catalogBundle("data-preferences-ios"))
            }

            commonTest.dependencies {
                implementation(catalogBundle("data-preferences-common-test"))
            }
            androidUnitTest.dependencies {
                implementation(catalogBundle("data-preferences-android-test"))
            }
            iosTest.dependencies {
                implementation(catalogBundle("data-preferences-ios-test"))
            }
        }
    }
}
