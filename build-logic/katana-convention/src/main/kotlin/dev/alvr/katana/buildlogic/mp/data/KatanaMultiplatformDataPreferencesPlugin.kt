package dev.alvr.katana.buildlogic.mp.data

import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.desktopMain
import dev.alvr.katana.buildlogic.mp.desktopTest
import dev.alvr.katana.buildlogic.mp.jvmBasedMain
import dev.alvr.katana.buildlogic.mp.jvmBasedTest
import dev.alvr.katana.buildlogic.mp.webBasedMain
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformDataPreferencesPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "katana.multiplatform.core")
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform(project) }
        }
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform(project: Project) {
        configureSourceSets()
        kspDependencies(project, "data-preferences")
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
            desktopMain.dependencies {
                implementation(catalogBundle("data-preferences-desktop"))
            }
            jvmBasedMain.dependencies {
                implementation(catalogBundle("data-preferences-jvm"))
            }
            webBasedMain.dependencies {
                implementation(catalogBundle("data-preferences-web"))
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
            desktopTest.dependencies {
                implementation(catalogBundle("data-preferences-desktop-test"))
            }
            jvmBasedTest.dependencies {
                implementation(catalogBundle("data-preferences-jvm-test"))
            }
        }
    }
}
