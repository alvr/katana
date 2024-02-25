package dev.alvr.katana.buildlogic.feature.data

import dev.alvr.katana.buildlogic.utils.androidUnitTest
import dev.alvr.katana.buildlogic.utils.catalogBundle
import dev.alvr.katana.buildlogic.utils.kspDependencies
import dev.alvr.katana.buildlogic.utils.sourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaDataPreferencesPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "katana.feature")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
        }
    }

    context(Project)
    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        configureSourceSets()
        kspDependencies("mobile")
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
