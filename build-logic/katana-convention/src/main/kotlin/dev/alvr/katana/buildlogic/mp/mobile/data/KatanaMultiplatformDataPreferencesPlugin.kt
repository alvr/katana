package dev.alvr.katana.buildlogic.mp.mobile.data

import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.mp.androidUnitTest
import dev.alvr.katana.buildlogic.mp.configureSourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformDataPreferencesPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "katana.multiplatform.mobile")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureSourceSets() }
        }
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
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
