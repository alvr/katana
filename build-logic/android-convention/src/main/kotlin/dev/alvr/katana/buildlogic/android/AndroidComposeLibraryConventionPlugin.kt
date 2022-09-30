package dev.alvr.katana.buildlogic.android

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.catalogLib
import dev.alvr.katana.buildlogic.debugImplementation
import dev.alvr.katana.buildlogic.implementation
import dev.alvr.katana.buildlogic.kapt
import dev.alvr.katana.buildlogic.ksp
import dev.alvr.katana.buildlogic.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("UnstableApiUsage")
internal class AndroidComposeLibraryConventionPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "katana.android.library")
        apply(plugin = "com.google.devtools.ksp")

        extensions.configure<LibraryExtension> {
            configureCompose(this)

            libraryVariants.all {
                sourceSets {
                    getByName(name) {
                        kotlin.srcDir("build/generated/ksp/$name/kotlin")
                    }
                }
            }
        }

        dependencies {
            implementation(catalogBundle("ui-compose"))

            debugImplementation(catalogLib("compose-ui-test-manifest"))

            kapt(catalogBundle("kapt-ui"))
            ksp(catalogBundle("ksp-ui"))

            testImplementation(catalogBundle("test"))
            testImplementation(catalogBundle("test-android"))
            testImplementation(catalogBundle("test-ui"))
        }
    }
}
