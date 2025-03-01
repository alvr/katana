@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.mp.data

import com.apollographql.apollo.gradle.api.ApolloExtension
import dev.alvr.katana.buildlogic.bundleImplementation
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.desktopMain
import dev.alvr.katana.buildlogic.mp.desktopTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformDataRemotePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "katana.multiplatform.core")
        apply(plugin = "com.apollographql.apollo")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform(project) }
            configure<ApolloExtension> { configureApollo(project) }
        }
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform(project: Project) {
        configureSourceSets()
        kspDependencies(project, "data-remote")
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        sourceSets {
            commonMain.dependencies {
                bundleImplementation("data-remote-common")
            }
            androidMain.dependencies {
                bundleImplementation("data-remote-android")
            }
            iosMain.dependencies {
                bundleImplementation("data-remote-ios")
            }
            desktopMain.dependencies {
                bundleImplementation("data-remote-desktop")
            }
            jsMain.dependencies {
                bundleImplementation("data-remote-js")
            }
            wasmJsMain.dependencies {
                bundleImplementation("data-remote-wasm")
            }

            commonTest.dependencies {
                bundleImplementation("data-remote-common-test")
            }
            androidUnitTest.dependencies {
                bundleImplementation("data-remote-android-test")
            }
            iosTest.dependencies {
                bundleImplementation("data-remote-ios-test")
            }
            desktopTest.dependencies {
                bundleImplementation("data-remote-desktop-test")
            }
            jsTest.dependencies {
                bundleImplementation("data-remote-js-test")
            }
            wasmJsTest.dependencies {
                bundleImplementation("data-remote-wasm-test")
            }
        }
    }

    private fun ApolloExtension.configureApollo(project: Project) {
        service("anilist") {
            decapitalizeFields = true
            generateAsInternal = true
            generateMethods = listOf("equalsHashCode")
            packageName = project.fullPackageName
            warnOnDeprecatedUsages = true

            if (project.path == CORE_PROJECT) {
                generateApolloMetadata = true
                generateAsInternal = false
                generateDataBuilders = true
                schemaFiles.from(
                    project.file("src/commonMain/graphql/schema.graphqls"),
                    project.file("src/commonMain/graphql/extra.graphqls"),
                )

                introspection {
                    endpointUrl = "https://graphql.anilist.co"
                    schemaFile = project.file("src/commonMain/graphql/schema.graphqls")
                }
            } else {
                dependsOn(project.project(CORE_PROJECT))
            }
        }
    }
}

private const val CORE_PROJECT = ":core:remote"
