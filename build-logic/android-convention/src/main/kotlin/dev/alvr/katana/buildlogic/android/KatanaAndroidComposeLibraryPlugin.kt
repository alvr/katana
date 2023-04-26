package dev.alvr.katana.buildlogic.android

import com.android.build.gradle.LibraryExtension
import com.google.devtools.ksp.gradle.KspExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.catalogLib
import dev.alvr.katana.buildlogic.implementation
import dev.alvr.katana.buildlogic.ksp
import dev.alvr.katana.buildlogic.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("UnstableApiUsage")
internal class KatanaAndroidComposeLibraryPlugin : ConventionPlugin {
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
            implementation(catalogBundle("ui-android"))
            implementation(catalogLib("koin-compose")) { excludeKoinDeps() }

            ksp(catalogBundle("ui-common-ksp"))

            testImplementation(platform(catalogLib("compose-bom")))
            testImplementation(catalogBundle("mobile-common-test"))
            testImplementation(catalogBundle("mobile-android-test"))
            testImplementation(catalogBundle("ui-android-test"))
        }
    }
}
