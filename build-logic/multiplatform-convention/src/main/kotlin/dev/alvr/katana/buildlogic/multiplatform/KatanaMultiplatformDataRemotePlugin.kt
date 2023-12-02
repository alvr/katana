package dev.alvr.katana.buildlogic.multiplatform

import com.apollographql.apollo3.gradle.api.ApolloExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.fullPackageName
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformDataRemotePlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "katana.multiplatform.mobile")
        apply(plugin = "com.apollographql.apollo3")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureSourceSets() }
            configure<ApolloExtension> { configureApollo(project) }
        }
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
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

    private fun ApolloExtension.configureApollo(project: Project) {
        val namespace = project.fullPackageName

        service("anilist") {
            generateAsInternal.set(true)
            generateDataBuilders.set(true)
            packageName.set(namespace)

            if (namespace.contains(BASE_PACKAGE)) {
                alwaysGenerateTypesMatching.set(listOf("Query", "User"))
                generateApolloMetadata.set(true)
                generateAsInternal.set(false)

                introspection {
                    endpointUrl.set("https://graphql.anilist.co")
                    schemaFile.set(project.file("src/commonMain/graphql/schema.graphqls"))
                }
            }
        }
    }

    private companion object {
        const val BASE_PACKAGE = ".base"
    }
}
