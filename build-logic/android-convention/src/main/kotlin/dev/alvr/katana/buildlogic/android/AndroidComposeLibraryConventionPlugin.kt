package dev.alvr.katana.buildlogic.android

import com.android.build.gradle.LibraryExtension
import com.google.devtools.ksp.gradle.KspExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.catalogLib
import dev.alvr.katana.buildlogic.debugImplementation
import dev.alvr.katana.buildlogic.implementation
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

        with(extensions) {
            configure<KspExtension> {
                arg("compose-destinations.useComposableVisibility", "false")
            }

            configure<LibraryExtension> {
                configureCompose(this)

                libraryVariants.all {
                    sourceSets {
                        getByName(name) {
                            kotlin.srcDir("build/generated/ksp/$name/kotlin")
                        }
                    }
                }
            }
        }

        dependencies {
            implementation(platform(catalogLib("compose-bom")))
            implementation(catalogBundle("ui-compose"))
            implementation(catalogLib("koin-compose")) { isTransitive = false }

            debugImplementation(catalogLib("compose-ui-test-manifest"))

            ksp(catalogBundle("ksp-ui"))

            testImplementation(platform(catalogLib("compose-bom")))
            testImplementation(catalogBundle("test"))
            testImplementation(catalogBundle("test-android"))
            testImplementation(catalogBundle("test-ui"))
        }
    }
}
