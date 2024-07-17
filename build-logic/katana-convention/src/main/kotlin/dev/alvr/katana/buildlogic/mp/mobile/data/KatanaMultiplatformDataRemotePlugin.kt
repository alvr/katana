@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.mp.mobile.data

import com.apollographql.apollo.gradle.api.ApolloExtension
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.androidUnitTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformDataRemotePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "katana.multiplatform.mobile")
        apply(plugin = "com.apollographql.apollo")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureMultiplatform() }
            configure<ApolloExtension> { configureApollo() }
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
                implementation(catalogBundle("data-remote-common"))
            }
            androidMain.dependencies {
                implementation(catalogBundle("data-remote-android"))
            }
            iosMain.dependencies {
                implementation(catalogBundle("data-remote-ios"))
            }

            commonTest.dependencies {
                implementation(catalogBundle("data-remote-common-test"))
            }
            androidUnitTest.dependencies {
                implementation(catalogBundle("data-remote-android-test"))
            }
            iosTest.dependencies {
                implementation(catalogBundle("data-remote-ios-test"))
            }
        }
    }

    context(Project)
    private fun ApolloExtension.configureApollo() {
        service("anilist") {
            decapitalizeFields = true
            generateAsInternal = true
            generateMethods = listOf("equalsHashCode")
            packageName = fullPackageName
            warnOnDeprecatedUsages = true

            if (path == CORE_PROJECT) {
                generateApolloMetadata = true
                generateAsInternal = false
                generateDataBuilders = true
                schemaFiles.from(
                    file("src/commonMain/graphql/schema.graphqls"),
                    file("src/commonMain/graphql/extra.graphqls"),
                )

                introspection {
                    endpointUrl = "https://graphql.anilist.co"
                    schemaFile = file("src/commonMain/graphql/schema.graphqls")
                }
            } else {
                dependsOn(project(CORE_PROJECT))
            }
        }
    }

    private companion object {
        const val CORE_PROJECT = ":core:remote"
    }
}
