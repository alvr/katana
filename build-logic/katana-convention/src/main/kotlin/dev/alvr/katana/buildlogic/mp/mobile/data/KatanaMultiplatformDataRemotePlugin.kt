package dev.alvr.katana.buildlogic.mp.mobile.data

import com.apollographql.apollo3.gradle.api.ApolloExtension
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.fullPackageName
import dev.alvr.katana.buildlogic.kspDependencies
import dev.alvr.katana.buildlogic.mp.androidUnitTest
import dev.alvr.katana.buildlogic.mp.configureSourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformDataRemotePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "katana.multiplatform.mobile")
        apply(plugin = "com.apollographql.apollo3")

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

    context(Project)
    private fun ApolloExtension.configureApollo() {
        service("anilist") {
            generateAsInternal.set(true)
            generateDataBuilders.set(true)
            packageName.set(fullPackageName)

            if (fullPackageName.contains(BASE_PACKAGE)) {
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
